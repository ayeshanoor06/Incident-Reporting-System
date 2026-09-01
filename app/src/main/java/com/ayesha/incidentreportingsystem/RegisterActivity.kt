package com.ayesha.incidentreportingsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var editTextRegisterName: TextInputEditText
    private lateinit var editTextRegisterEmail: TextInputEditText
    private lateinit var editTextRegisterPassword: TextInputEditText
    private lateinit var editTextConfirmPassword: TextInputEditText

    private lateinit var buttonCreateAccount: Button
    private lateinit var textViewAlreadyHaveAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()

        editTextRegisterName =
            findViewById(R.id.editTextRegisterName)

        editTextRegisterEmail =
            findViewById(R.id.editTextRegisterEmail)

        editTextRegisterPassword =
            findViewById(R.id.editTextRegisterPassword)

        editTextConfirmPassword =
            findViewById(R.id.editTextConfirmPassword)

        buttonCreateAccount =
            findViewById(R.id.buttonCreateAccount)

        textViewAlreadyHaveAccount =
            findViewById(R.id.textViewAlreadyHaveAccount)

        buttonCreateAccount.setOnClickListener {
            createUserAccount()
        }

        textViewAlreadyHaveAccount.setOnClickListener {
            openLoginActivity()
        }
    }

    private fun createUserAccount() {

        val name =
            editTextRegisterName.text
                ?.toString()
                ?.trim()
                ?: ""

        val email =
            editTextRegisterEmail.text
                ?.toString()
                ?.trim()
                ?: ""

        val password =
            editTextRegisterPassword.text
                ?.toString()
                ?: ""

        val confirmPassword =
            editTextConfirmPassword.text
                ?.toString()
                ?: ""

        if (name.isEmpty()) {

            editTextRegisterName.error =
                "Please enter your name"

            editTextRegisterName.requestFocus()

            return
        }

        if (email.isEmpty()) {

            editTextRegisterEmail.error =
                "Please enter your email"

            editTextRegisterEmail.requestFocus()

            return
        }

        if (password.isEmpty()) {

            editTextRegisterPassword.error =
                "Please enter a password"

            editTextRegisterPassword.requestFocus()

            return
        }

        if (password.length < 6) {

            editTextRegisterPassword.error =
                "Password must be at least 6 characters"

            editTextRegisterPassword.requestFocus()

            return
        }

        if (confirmPassword.isEmpty()) {

            editTextConfirmPassword.error =
                "Please confirm your password"

            editTextConfirmPassword.requestFocus()

            return
        }

        if (password != confirmPassword) {

            editTextConfirmPassword.error =
                "Passwords do not match"

            editTextConfirmPassword.requestFocus()

            return
        }

        buttonCreateAccount.isEnabled = false

        firebaseAuth
            .createUserWithEmailAndPassword(
                email,
                password
            )
            .addOnCompleteListener { task ->

                buttonCreateAccount.isEnabled = true

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Account created successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    openMainActivity()

                } else {

                    val errorMessage =
                        task.exception?.message
                            ?: "Account creation failed"

                    Toast.makeText(
                        this,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun openLoginActivity() {

        val intent =
            Intent(
                this,
                LoginActivity::class.java
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