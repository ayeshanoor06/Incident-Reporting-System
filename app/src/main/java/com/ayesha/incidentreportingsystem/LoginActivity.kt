package com.ayesha.incidentreportingsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var editTextLoginEmail: TextInputEditText
    private lateinit var editTextLoginPassword: TextInputEditText

    private lateinit var buttonLogin: Button
    private lateinit var textViewCreateAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            openMainActivity()
            return
        }

        setContentView(R.layout.activity_login)

        editTextLoginEmail =
            findViewById(R.id.editTextLoginEmail)

        editTextLoginPassword =
            findViewById(R.id.editTextLoginPassword)

        buttonLogin =
            findViewById(R.id.buttonLogin)

        textViewCreateAccount =
            findViewById(R.id.textViewCreateAccount)

        buttonLogin.setOnClickListener {
            loginUser()
        }

        textViewCreateAccount.setOnClickListener {
            openRegisterActivity()
        }
    }

    private fun loginUser() {

        val email =
            editTextLoginEmail.text
                ?.toString()
                ?.trim()
                ?: ""

        val password =
            editTextLoginPassword.text
                ?.toString()
                ?: ""

        if (email.isEmpty()) {

            editTextLoginEmail.error =
                "Please enter your email"

            editTextLoginEmail.requestFocus()

            return
        }

        if (password.isEmpty()) {

            editTextLoginPassword.error =
                "Please enter your password"

            editTextLoginPassword.requestFocus()

            return
        }

        buttonLogin.isEnabled = false

        firebaseAuth
            .signInWithEmailAndPassword(
                email,
                password
            )
            .addOnCompleteListener { task ->

                buttonLogin.isEnabled = true

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Login successful",
                        Toast.LENGTH_SHORT
                    ).show()

                    openMainActivity()

                } else {

                    Toast.makeText(
                        this,
                        "Invalid email or password",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun openRegisterActivity() {

        val intent =
            Intent(
                this,
                RegisterActivity::class.java
            )

        startActivity(intent)
    }

    private fun openMainActivity() {

        val intent =
            Intent(
                this,
                MainActivity::class.java
            )

        startActivity(intent)

        finish()
    }
}