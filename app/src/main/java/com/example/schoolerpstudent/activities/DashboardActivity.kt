package com.example.schoolerpstudent.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolerpstudent.databinding.ActivityDashboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadStudentCount()
        loadNoticeCount()

        binding.btnViewDetails.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ViewStudentDetailsActivity::class.java
                )
            )
        }

        binding.btnViewNotices.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ViewStudentNoticesActivity::class.java
                )
            )
        }

        binding.btnCheckClassTeacher.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    CheckClassTeacherActivity::class.java
                )
            )
        }

        binding.btnCheckResult.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    CheckResultsActivity::class.java
                )
            )
        }
    }

    private fun loadStudentCount() {

        FirebaseDatabase.getInstance()
            .getReference("students")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(
                    snapshot: DataSnapshot
                ) {

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

                override fun onCancelled(
                    error: DatabaseError
                ) {

                    Toast.makeText(
                        this@DashboardActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun loadNoticeCount() {

        FirebaseDatabase.getInstance()
            .getReference("notices")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(
                    snapshot: DataSnapshot
                ) {

                    var count = 0

                    for (noticeSnapshot in snapshot.children) {

                        val target =
                            noticeSnapshot.child("noticeType")
                                .getValue(String::class.java)
                                ?: ""

                        if (
                            target.equals("student", true) ||
                            target.equals("both", true)
                        ) {
                            count++
                        }
                    }

                    binding.tvTotalNotices.text =
                        count.toString()
                }

                override fun onCancelled(
                    error: DatabaseError
                ) {

                    Toast.makeText(
                        this@DashboardActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}