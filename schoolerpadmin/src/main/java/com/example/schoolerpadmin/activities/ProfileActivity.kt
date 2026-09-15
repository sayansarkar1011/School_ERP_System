package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.schoolerpadmin.databinding.ActivityProfileBinding
import com.example.schoolerpadmin.model.AdminModel
import com.example.schoolerpadmin.viewModel.AuthViewModel

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        loadProfile()

        binding.topAppBar.setNavigationOnClickListener {

            startActivity(
                Intent(
                    this,
                    DashboardActivity::class.java
                )
            )

            finish()
        }
    }

    private fun loadProfile() {

        viewModel.getAdmin()
            .addOnSuccessListener { snapshot ->

                val admin =
                    snapshot.getValue(AdminModel::class.java)

                if (admin != null) {

                    binding.etAdminName.setText(
                        admin.adminName
                    )

                    binding.etAdminEmail.setText(
                        admin.adminEmail
                    )

                    binding.etSchoolName.setText(
                        admin.schoolName
                    )

                    binding.etSchoolAddress.setText(
                        admin.schoolAddress
                    )

                    binding.etSchoolUid.setText(
                        admin.schoolUid
                    )
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Unable to fetch Admin Details", Toast.LENGTH_SHORT).show()
            }
    }
}