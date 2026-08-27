package com.ayesha.incidentreportingsystem

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import java.util.Locale
import java.util.UUID

class ReportIncidentActivity : AppCompatActivity() {

    private lateinit var editTextDescription: EditText
    private lateinit var imageViewScreenshot: ImageView
    private lateinit var buttonSelectScreenshot: Button
    private lateinit var buttonSubmitIncident: Button

    private var selectedImageUri: Uri? = null

    private val firestoreDatabase = FirebaseFirestore.getInstance()

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

        buttonSubmitIncident.setOnClickListener {
            validateIncidentForm()
        }
    }

    private fun validateIncidentForm() {

        val description = editTextDescription.text.toString().trim()

        if (description.isEmpty()) {
            editTextDescription.error = "Please describe the issue"
            editTextDescription.requestFocus()
            return
        }

        if (selectedImageUri == null) {
            Toast.makeText(
                this,
                "Please select a screenshot",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        saveIncidentToFirestore(description)
    }

    private fun saveIncidentToFirestore(description: String) {

        val incidentId = generateIncidentId()

        val incidentData = hashMapOf(
            "incidentId" to incidentId,
            "description" to description,
            "imageUrl" to "",
            "status" to "Pending",
            "createdAt" to FieldValue.serverTimestamp()
        )

        firestoreDatabase
            .collection("incidents")
            .add(incidentData)
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Incident submitted successfully",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed to submit incident",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun generateIncidentId(): String {

        val randomPart = UUID.randomUUID()
            .toString()
            .replace("-", "")
            .substring(0, 6)
            .uppercase(Locale.getDefault())

        return "INC-$randomPart"
    }
}