package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolerpadmin.R
import com.example.schoolerpadmin.databinding.ActivityDownloadBinding

class DownloadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDownloadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityDownloadBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.bottomNavigation.selectedItemId =
            R.id.nav_download

        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_home -> {

                    startActivity(
                        Intent(
                            this,
                            DashboardActivity::class.java
                        )
                    )

                    finish()

                    true
                }

                R.id.nav_download -> {
                    true
                }

                else -> false
            }
        }

        binding.btnDownloadStudentDetails.setOnClickListener {
            startActivity(Intent(this, DownloadStudentActivity::class.java))
        }
        binding.btnDownloadTeacherDetails.setOnClickListener {
            startActivity(Intent(this, DownloadTeacherActivity::class.java))
        }
    }
}