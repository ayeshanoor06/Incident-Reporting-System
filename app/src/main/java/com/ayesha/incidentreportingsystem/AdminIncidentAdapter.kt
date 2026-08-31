package com.ayesha.incidentreportingsystem

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminIncidentAdapter(
    private val incidentList: MutableList<Incident>
) : RecyclerView.Adapter<AdminIncidentAdapter.AdminIncidentViewHolder>() {

    private val firestoreDatabase =
        FirebaseFirestore.getInstance()

    private val statusOptions = arrayOf(
        "Pending",
        "In Progress",
        "Resolved"
    )

    class AdminIncidentViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val textViewAdminIncidentId: TextView =
            itemView.findViewById(
                R.id.textViewAdminIncidentId
            )

        val textViewAdminIncidentDescription: TextView =
            itemView.findViewById(
                R.id.textViewAdminIncidentDescription
            )

        val imageViewAdminIncidentScreenshot: ImageView =
            itemView.findViewById(
                R.id.imageViewAdminIncidentScreenshot
            )

        val spinnerIncidentStatus: Spinner =
            itemView.findViewById(
                R.id.spinnerIncidentStatus
            )

        val buttonUpdateIncidentStatus: Button =
            itemView.findViewById(
                R.id.buttonUpdateIncidentStatus
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminIncidentViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_admin_incident,
                parent,
                false
            )

        return AdminIncidentViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AdminIncidentViewHolder,
        position: Int
    ) {

        val incident = incidentList[position]

        holder.textViewAdminIncidentId.text =
            incident.incidentId

        holder.textViewAdminIncidentDescription.text =
            incident.description

        displayIncidentScreenshot(
            holder.imageViewAdminIncidentScreenshot,
            incident.imageUrl
        )

        val statusAdapter = ArrayAdapter(
            holder.itemView.context,
            android.R.layout.simple_spinner_item,
            statusOptions
        )

        statusAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        holder.spinnerIncidentStatus.adapter =
            statusAdapter

        val currentStatusPosition =
            statusOptions.indexOf(incident.status)

        if (currentStatusPosition >= 0) {

            holder.spinnerIncidentStatus.setSelection(
                currentStatusPosition
            )
        }

        holder.buttonUpdateIncidentStatus.setOnClickListener {

            val selectedStatus =
                holder.spinnerIncidentStatus
                    .selectedItem
                    .toString()

            updateIncidentStatus(
                incident,
                selectedStatus,
                holder.itemView
            )
        }
    }

    private fun displayIncidentScreenshot(
        imageViewAdminIncidentScreenshot: ImageView,
        imageUrl: String
    ) {

        if (imageUrl.isEmpty()) {

            imageViewAdminIncidentScreenshot.visibility =
                View.GONE

            return
        }

        try {

            val imageBytes =
                Base64.decode(
                    imageUrl,
                    Base64.DEFAULT
                )

            val bitmap =
                BitmapFactory.decodeByteArray(
                    imageBytes,
                    0,
                    imageBytes.size
                )

            if (bitmap != null) {

                imageViewAdminIncidentScreenshot.visibility =
                    View.VISIBLE

                imageViewAdminIncidentScreenshot
                    .setImageBitmap(bitmap)

            } else {

                imageViewAdminIncidentScreenshot.visibility =
                    View.GONE
            }

        } catch (exception: Exception) {

            imageViewAdminIncidentScreenshot.visibility =
                View.GONE
        }
    }

    private fun updateIncidentStatus(
        incident: Incident,
        selectedStatus: String,
        itemView: View
    ) {

        firestoreDatabase
            .collection("incidents")
            .whereEqualTo(
                "incidentId",
                incident.incidentId
            )
            .get()
            .addOnSuccessListener { documents ->

                if (documents.isEmpty) {

                    Toast.makeText(
                        itemView.context,
                        "Incident not found",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                val document =
                    documents.documents[0]

                document.reference
                    .update(
                        "status",
                        selectedStatus
                    )
                    .addOnSuccessListener {

                        val incidentIndex =
                            incidentList.indexOf(incident)

                        if (incidentIndex >= 0) {

                            incidentList[incidentIndex] =
                                incident.copy(
                                    status = selectedStatus
                                )

                            notifyItemChanged(
                                incidentIndex
                            )
                        }

                        Toast.makeText(
                            itemView.context,
                            "Status updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {

                        Toast.makeText(
                            itemView.context,
                            "Failed to update status",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .addOnFailureListener {

                Toast.makeText(
                    itemView.context,
                    "Failed to find incident",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun getItemCount(): Int {
        return incidentList.size
    }
}