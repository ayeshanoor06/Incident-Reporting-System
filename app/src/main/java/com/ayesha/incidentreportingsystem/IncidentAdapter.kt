package com.ayesha.incidentreportingsystem

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

data class Incident(
    val incidentId: String,
    val description: String,
    val imageUrl: String,
    val status: String,
    val createdAt: Timestamp?
)

class IncidentAdapter(
    private val incidentList: MutableList<Incident>
) : RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder>() {

    private val firestoreDatabase =
        FirebaseFirestore.getInstance()

    class IncidentViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val textViewIncidentId: TextView =
            itemView.findViewById(
                R.id.textViewIncidentId
            )

        val textViewIncidentDescription: TextView =
            itemView.findViewById(
                R.id.textViewIncidentDescription
            )

        val imageViewIncidentScreenshot: ImageView =
            itemView.findViewById(
                R.id.imageViewIncidentScreenshot
            )

        val textViewIncidentStatus: TextView =
            itemView.findViewById(
                R.id.textViewIncidentStatus
            )

        val textViewIncidentDate: TextView =
            itemView.findViewById(
                R.id.textViewIncidentDate
            )

        val buttonDeleteIncident: Button =
            itemView.findViewById(
                R.id.buttonDeleteIncident
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IncidentViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_incident,
                parent,
                false
            )

        return IncidentViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: IncidentViewHolder,
        position: Int
    ) {

        val incident = incidentList[position]

        holder.textViewIncidentId.text =
            incident.incidentId

        holder.textViewIncidentDescription.text =
            incident.description

        holder.textViewIncidentStatus.text =
            incident.status

        displayIncidentScreenshot(
            holder.imageViewIncidentScreenshot,
            incident.imageUrl
        )

        displayIncidentDate(
            holder.textViewIncidentDate,
            incident.createdAt
        )

        holder.buttonDeleteIncident.setOnClickListener {

            val currentPosition =
                holder.bindingAdapterPosition

            if (currentPosition != RecyclerView.NO_POSITION) {

                val selectedIncident =
                    incidentList[currentPosition]

                showDeleteConfirmationDialog(
                    holder.itemView,
                    selectedIncident,
                    currentPosition
                )
            }
        }
    }

    private fun displayIncidentScreenshot(
        imageViewIncidentScreenshot: ImageView,
        imageUrl: String
    ) {

        if (imageUrl.isEmpty()) {

            imageViewIncidentScreenshot.visibility =
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

                imageViewIncidentScreenshot.visibility =
                    View.VISIBLE

                imageViewIncidentScreenshot
                    .setImageBitmap(bitmap)

            } else {

                imageViewIncidentScreenshot.visibility =
                    View.GONE
            }

        } catch (exception: Exception) {

            imageViewIncidentScreenshot.visibility =
                View.GONE
        }
    }

    private fun displayIncidentDate(
        textViewIncidentDate: TextView,
        createdAt: Timestamp?
    ) {

        if (createdAt != null) {

            val dateFormat =
                SimpleDateFormat(
                    "dd MMM yyyy, hh:mm a",
                    Locale.getDefault()
                )

            textViewIncidentDate.text =
                dateFormat.format(
                    createdAt.toDate()
                )

        } else {

            textViewIncidentDate.text =
                "Date unavailable"
        }
    }

    private fun showDeleteConfirmationDialog(
        itemView: View,
        incident: Incident,
        position: Int
    ) {

        val alertDialog =
            AlertDialog.Builder(itemView.context)
                .setTitle("Delete Incident?")
                .setMessage(
                    "Are you sure you want to delete this incident? " +
                            "This action cannot be undone."
                )
                .setNegativeButton(
                    "Cancel",
                    null
                )
                .setPositiveButton(
                    "Delete",
                    null
                )
                .create()

        alertDialog.setOnShowListener {

            alertDialog.getButton(
                AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener {

                deleteIncidentFromFirestore(
                    itemView,
                    incident,
                    position,
                    alertDialog
                )
            }
        }

        alertDialog.show()
    }

    private fun deleteIncidentFromFirestore(
        itemView: View,
        incident: Incident,
        position: Int,
        alertDialog: AlertDialog
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
                    .delete()
                    .addOnSuccessListener {

                        if (position >= 0 &&
                            position < incidentList.size
                        ) {

                            incidentList.removeAt(position)

                            notifyItemRemoved(position)
                        }

                        alertDialog.dismiss()

                        Toast.makeText(
                            itemView.context,
                            "Incident deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {

                        Toast.makeText(
                            itemView.context,
                            "Failed to delete incident",
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