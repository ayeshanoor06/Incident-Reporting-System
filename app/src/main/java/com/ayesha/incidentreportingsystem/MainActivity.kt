package com.ayesha.incidentreportingsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var buttonReportIncident: Button
    private lateinit var buttonTrackTickets: Button
    private lateinit var buttonAdminPanel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonReportIncident = findViewById(R.id.buttonReportIncident)
        buttonTrackTickets = findViewById(R.id.buttonTrackTickets)
        buttonAdminPanel = findViewById(R.id.buttonAdminPanel)

        buttonReportIncident.setOnClickListener {
            val intent = Intent(this, ReportIncidentActivity::class.java)
            startActivity(intent)
        }

        buttonTrackTickets.setOnClickListener {
            val intent = Intent(this, TicketTrackingActivity::class.java)
            startActivity(intent)
        }

        buttonAdminPanel.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }
    }
}