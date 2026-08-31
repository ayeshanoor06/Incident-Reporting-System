package com.ayesha.incidentreportingsystem

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
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
    private val incidentList: List<Incident>
) : RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder>() {

    class IncidentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textViewIncidentId: TextView =
            itemView.findViewById(R.id.textViewIncidentId)

        val textViewIncidentDescription: TextView =
            itemView.findViewById(R.id.textViewIncidentDescription)

        val imageViewIncidentScreenshot: ImageView =
            itemView.findViewById(R.id.imageViewIncidentScreenshot)

        val textViewIncidentStatus: TextView =
            itemView.findViewById(R.id.textViewIncidentStatus)

        val textViewIncidentDate: TextView =
            itemView.findViewById(R.id.textViewIncidentDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IncidentViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_incident, parent, false)

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

        if (incident.imageUrl.isNotEmpty()) {

            try {

                val imageBytes =
                    Base64.decode(
                        incident.imageUrl,
                        Base64.DEFAULT
                    )

                val bitmap =
                    BitmapFactory.decodeByteArray(
                        imageBytes,
                        0,
                        imageBytes.size
                    )

                if (bitmap != null) {

                    holder.imageViewIncidentScreenshot.visibility =
                        View.VISIBLE

                    holder.imageViewIncidentScreenshot
                        .setImageBitmap(bitmap)

                } else {

                    holder.imageViewIncidentScreenshot.visibility =
                        View.GONE
                }

            } catch (exception: Exception) {

                holder.imageViewIncidentScreenshot.visibility =
                    View.GONE
            }

        } else {

            holder.imageViewIncidentScreenshot.visibility =
                View.GONE
        }

        if (incident.createdAt != null) {

            val dateFormat = SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            )

            holder.textViewIncidentDate.text =
                dateFormat.format(
                    incident.createdAt.toDate()
                )

        } else {

            holder.textViewIncidentDate.text =
                "Date unavailable"
        }
    }

    override fun getItemCount(): Int {
        return incidentList.size
    }
}