package com.ayesha.incidentreportingsystem

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ReportIncidentActivity : AppCompatActivity() {

    private lateinit var editTextDescription: EditText
    private lateinit var imageViewScreenshot: ImageView
    private lateinit var buttonSelectScreenshot: Button
    private lateinit var buttonSubmitIncident: Button

    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

            if (uri != null) {
                selectedImageUri = uri

                Glide.with(this)
                    .load(uri)
                    .into(imageViewScreenshot)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_incident)

        editTextDescription = findViewById(R.id.editTextDescription)
        imageViewScreenshot = findViewById(R.id.imageViewScreenshot)
        buttonSelectScreenshot = findViewById(R.id.buttonSelectScreenshot)
        buttonSubmitIncident = findViewById(R.id.buttonSubmitIncident)

        buttonSelectScreenshot.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }
    }
}