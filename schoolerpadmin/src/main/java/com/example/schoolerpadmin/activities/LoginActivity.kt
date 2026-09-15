package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.schoolerpadmin.databinding.ActivityLoginBinding
import com.example.schoolerpadmin.viewModel.AuthViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.tvForgotPassword.setOnClickListener {
            forgotPassword()
        }
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

    }
    private fun login () {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty() ||
            password.isEmpty())
        {
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.login(
            email,password
        ).addOnSuccessListener {
            Toast.makeText(this, "Logged in Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(this, "Logging failed", Toast.LENGTH_SHORT).show()
            }

    }

    private fun forgotPassword() {
        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()){
            Toast.makeText(this, "Please add your email", Toast.LENGTH_SHORT).show()
        }

        viewModel.forgotPassword(email).addOnSuccessListener {
            Toast.makeText(this, "Please check your mail, Password change link has been sent to your email", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener { Toast.makeText(this, "Please enter a valid registered email", Toast.LENGTH_SHORT).show() }
    }
}