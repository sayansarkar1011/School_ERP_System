package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.schoolerpadmin.R
import com.example.schoolerpadmin.databinding.ActivityDashboardBinding
import com.example.schoolerpadmin.model.AdminModel
import com.example.schoolerpadmin.viewModel.AuthViewModel
import com.google.firebase.database.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor =
            resources.getColor(
                android.R.color.transparent,
                theme
            )

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    } else {
                        finish()
                    }
                }
            })

        loadAdmin()
        loadStudentCount()
        loadTeacherCount()

        // Side Navigation
        binding.navigationView.setNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.navProfile -> {
                    startActivity(
                        Intent(
                            this,
                            ProfileActivity::class.java
                        )
                    )
                }

                R.id.navLogout -> {
                    logout()
                }
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        binding.btnAddStudent.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AddStudentActivity::class.java
                )
            )
        }

        binding.btnManageStudent.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ManageStudentsActivity::class.java
                )
            )
        }

        binding.btnAddTeacher.setOnClickListener {
            startActivity(Intent(this, AddTeacherActivity::class.java))
        }

        binding.btnManageTeacher.setOnClickListener {
            startActivity(Intent(this, ManageTeachersActivity::class.java))
        }

        binding.btnManageNotices.setOnClickListener {
            startActivity(Intent(this, NoticeManagementActivity::class.java))
        }
        binding.btnViewAllNotices.setOnClickListener {
            startActivity(Intent(this, ViewAllNoticesActivity::class.java))
        }
        binding.btnManageClasses.setOnClickListener {
            startActivity(Intent(this, ClassActivity::class.java))
        }
        binding.btnManageResults.setOnClickListener {
            startActivity(Intent(this, ManageResultsActivity::class.java))
        }
        binding.btnViewAllResults.setOnClickListener {
            startActivity(Intent(this, ViewAllResultsActivity::class.java))
        }
        binding.btnViewClassTeachers.setOnClickListener {
            startActivity(Intent(this, ViewClassTeachersActivity::class.java))
        }


        binding.bottomNavigation.selectedItemId =
            R.id.nav_home

        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_home -> {

                    binding.homeContent.visibility =
                        android.view.View.VISIBLE

                    binding.downloadContent.visibility =
                        android.view.View.GONE

                    true
                }

                R.id.nav_download -> {

                    startActivity(
                        Intent(
                            this,
                            DownloadActivity::class.java
                        )
                    )

                    // Home selected
                    binding.bottomNavigation.selectedItemId =
                        R.id.nav_home

                    false
                }

                else -> false
            }
        }
    }

    private fun loadAdmin() {

        viewModel.getAdmin()

            .addOnSuccessListener { snapshot ->

                val admin =
                    snapshot.getValue(AdminModel::class.java)

                binding.tvWelcome.text =
                    "Welcome, ${admin?.adminName ?: "Admin"}"

                admin?.let {

                    val headerView =
                        binding.navigationView.getHeaderView(0)

                    headerView.findViewById<TextView>(
                        R.id.tvHeaderName
                    ).text = it.adminName

                    headerView.findViewById<TextView>(
                        R.id.tvHeaderEmail
                    ).text = it.adminEmail
                }
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed to load profile",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun loadStudentCount() {

        FirebaseDatabase.getInstance()
            .getReference("students")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    var totalStudents = 0

                    for (classSnapshot in snapshot.children) {

                        for (sectionSnapshot in classSnapshot.children) {

                            for (studentSnapshot in sectionSnapshot.children) {

                                totalStudents +=
                                    studentSnapshot.childrenCount.toInt()
                            }
                        }
                    }

                    binding.tvTotalStudents.text =
                        totalStudents.toString()
                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(
                        this@DashboardActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun loadTeacherCount() {

        FirebaseDatabase.getInstance()
            .getReference("teachers")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    binding.tvTotalTeachers.text =
                        snapshot.childrenCount.toString()
                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(
                        this@DashboardActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun logout() {

        viewModel.logout()

        Toast.makeText(
            this,
            "Logged Out",
            Toast.LENGTH_SHORT
        ).show()

        startActivity(
            Intent(
                this,
                LoginActivity::class.java
            ).apply {

                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        )

        finish()
    }
}