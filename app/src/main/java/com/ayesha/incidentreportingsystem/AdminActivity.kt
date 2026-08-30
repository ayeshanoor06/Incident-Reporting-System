package com.ayesha.incidentreportingsystem

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private lateinit var recyclerViewAdminIncidents: RecyclerView

    private val firestoreDatabase = FirebaseFirestore.getInstance()

    private val incidentList = mutableListOf<Incident>()

    private lateinit var adminIncidentAdapter: AdminIncidentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        recyclerViewAdminIncidents =
            findViewById(R.id.recyclerViewAdminIncidents)

        adminIncidentAdapter =
            AdminIncidentAdapter(incidentList)

        recyclerViewAdminIncidents.layoutManager =
            LinearLayoutManager(this)

        recyclerViewAdminIncidents.adapter =
            adminIncidentAdapter

        loadIncidentsForAdmin()
    }

    private fun loadIncidentsForAdmin() {

        firestoreDatabase
            .collection("incidents")
            .orderBy("createdAt")
            .get()
            .addOnSuccessListener { documents ->

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

                adminIncidentAdapter.notifyDataSetChanged()
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