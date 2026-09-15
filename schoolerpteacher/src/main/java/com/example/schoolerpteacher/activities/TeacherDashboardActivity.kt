package com.example.schoolerpteacher.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolerpteacher.databinding.ActivityTeacherDashboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TeacherDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewDetailsCard.setOnClickListener {
            startActivity(Intent(this, ViewTeacherDetailsActivity::class.java))
        }

        binding.noticesCard.setOnClickListener {
            startActivity(Intent(this, ViewTeacherNoticesActivity::class.java))
        }

        binding.assignedClassCard.setOnClickListener {
            startActivity(Intent(this, CheckAssignedClassActivity::class.java))
        }

        loadTeacherCount()
        loadNoticeCount()
    }

    private fun loadTeacherCount(){
        FirebaseDatabase.getInstance().getReference("teachers")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.tvTotalTeachers.text = snapshot.childrenCount.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@TeacherDashboardActivity,error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadNoticeCount() {

        FirebaseDatabase.getInstance()
            .getReference("notices")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    var count = 0

                    for (noticeSnapshot in snapshot.children) {

                        val target =
                            noticeSnapshot.child("noticeType")
                                .getValue(String::class.java)
                                ?: ""

                        if (
                            target.equals("teacher", true) ||
                            target.equals("both", true)
                        ) {
                            count++
                        }
                    }

                    binding.tvTotalNotices.text =
                        count.toString()
                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(
                        this@TeacherDashboardActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}