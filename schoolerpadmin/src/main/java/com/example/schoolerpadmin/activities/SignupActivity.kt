package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.schoolerpadmin.databinding.ActivitySignupBinding
import com.example.schoolerpadmin.model.AdminModel
import com.example.schoolerpadmin.utils.UidGenerator
import com.example.schoolerpadmin.viewModel.AuthViewModel

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
    viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        viewModel.isAdminExists { exists ->

            if (exists) {

                Toast.makeText(
                    this,
                    "Admin already exists. Please login.",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(this, LoginActivity::class.java)
                )

                finish()
            }
        }

        binding.btnSignup.setOnClickListener {
            signup()
        }
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun signup() {
        val adminName = binding.etAdminName.text.toString().trim()
        val adminEmail = binding.etAdminEmail.text.toString().trim()
        val schoolName = binding.etSchoolName.text.toString().trim()
        val schoolAddress = binding.etSchoolAddress.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        if (adminName.isEmpty() ||
            adminEmail.isEmpty() ||
            schoolName.isEmpty() ||
            schoolAddress.isEmpty() ||
            password.isEmpty() ||
            confirmPassword.isEmpty()
        ) {
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword){
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show()
            return
        }
            val admin = AdminModel(
                adminUid = "",
                adminEmail = adminEmail,
                adminName = adminName,
                schoolName = schoolName,
                schoolAddress = schoolAddress,
                schoolUid = UidGenerator.generateSchoolUid()

            )

        viewModel.isAdminExists { exists ->

            if (exists) {
                Toast.makeText(
                    this,
                    "Admin account already exists",
                    Toast.LENGTH_SHORT
                ).show()

                return@isAdminExists
            }

            viewModel.signup(
                adminEmail,
                password,
                admin
            ).addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Signup Successful",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(this, LoginActivity::class.java)
                )

                finish()

            }.addOnFailureListener {

                Toast.makeText(
                    this,
                    it.message ?: "Signup Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}