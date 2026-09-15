package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolerpadmin.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            checkLogin()
        },1000)

    }

    private fun checkLogin() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null){
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        else{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}