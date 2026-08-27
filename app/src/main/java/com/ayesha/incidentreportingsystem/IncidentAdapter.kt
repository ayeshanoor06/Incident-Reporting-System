package com.ayesha.incidentreportingsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        holder.textViewIncidentId.text = incident.incidentId
        holder.textViewIncidentDescription.text = incident.description
        holder.textViewIncidentStatus.text = incident.status

        if (incident.createdAt != null) {

            val dateFormat = SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            )

            holder.textViewIncidentDate.text =
                dateFormat.format(incident.createdAt.toDate())

        } else {

            holder.textViewIncidentDate.text = "Date unavailable"
        }
    }

    override fun getItemCount(): Int {
        return incidentList.size
    }
}