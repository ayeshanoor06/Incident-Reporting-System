package com.ayesha.incidentreportingsystem

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class TicketTrackingActivity : AppCompatActivity() {

    private lateinit var recyclerViewIncidents: RecyclerView

    private val firestoreDatabase = FirebaseFirestore.getInstance()

    private val incidentList = mutableListOf<Incident>()

    private lateinit var incidentAdapter: IncidentAdapter

    private var incidentListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_tracking)

        recyclerViewIncidents = findViewById(R.id.recyclerViewIncidents)

        incidentAdapter = IncidentAdapter(incidentList)

        recyclerViewIncidents.layoutManager =
            LinearLayoutManager(this)

        recyclerViewIncidents.adapter =
            incidentAdapter

        listenForIncidentUpdates()
    }

    private fun listenForIncidentUpdates() {

        incidentListener = firestoreDatabase
            .collection("incidents")
            .orderBy("createdAt")
            .addSnapshotListener { documents, error ->

                if (error != null) {

                    Toast.makeText(
                        this,
                        "Failed to load incidents",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addSnapshotListener
                }

                if (documents != null) {

                    incidentList.clear()

                    for (document in documents) {

                        val incident = Incident(
                            incidentId =
                                document.getString("incidentId") ?: "",

                            description =
                                document.getString("description") ?: "",

                            imageUrl =
                                document.getString("imageUrl") ?: "",

                            status =
                                document.getString("status") ?: "Pending",

                            createdAt =
                                document.getTimestamp("createdAt")
                        )

                        incidentList.add(incident)
                    }

                    incidentAdapter.notifyDataSetChanged()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()

        incidentListener?.remove()
    }
}