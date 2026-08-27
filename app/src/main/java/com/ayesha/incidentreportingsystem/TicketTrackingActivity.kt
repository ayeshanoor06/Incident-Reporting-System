package com.ayesha.incidentreportingsystem

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class TicketTrackingActivity : AppCompatActivity() {

    private lateinit var recyclerViewIncidents: RecyclerView

    private val firestoreDatabase = FirebaseFirestore.getInstance()

    private val incidentList = mutableListOf<Incident>()

    private lateinit var incidentAdapter: IncidentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_tracking)

        recyclerViewIncidents = findViewById(R.id.recyclerViewIncidents)

        incidentAdapter = IncidentAdapter(incidentList)

        recyclerViewIncidents.layoutManager =
            LinearLayoutManager(this)

        recyclerViewIncidents.adapter = incidentAdapter

        loadIncidentsFromFirestore()
    }

    private fun loadIncidentsFromFirestore() {

        firestoreDatabase
            .collection("incidents")
            .orderBy("createdAt")
            .get()
            .addOnSuccessListener { documents ->

                incidentList.clear()

                for (document in documents) {

                    val incident = Incident(
                        incidentId = document.getString("incidentId") ?: "",
                        description = document.getString("description") ?: "",
                        imageUrl = document.getString("imageUrl") ?: "",
                        status = document.getString("status") ?: "Pending",
                        createdAt = document.getTimestamp("createdAt")
                    )

                    incidentList.add(incident)
                }

                incidentAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed to load incidents",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}