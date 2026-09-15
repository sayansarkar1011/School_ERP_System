package com.example.schoolerpteacher.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolerpteacher.databinding.ActivityCheckAssignedClassBinding
import com.google.firebase.database.FirebaseDatabase

class CheckAssignedClassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckAssignedClassBinding

    private val database =
        FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityCheckAssignedClassBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnSearch.setOnClickListener {
            searchClassSection()
        }

        binding.classDetailsCard.visibility =
            View.GONE
    }

    private fun searchClassSection() {

        val teacherPhone =
            binding.etTeacherPhone.text
                .toString()
                .trim()

        if (teacherPhone.isEmpty()) {

            Toast.makeText(
                this,
                "Please enter phone number",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (teacherPhone.length != 10) {

            Toast.makeText(
                this,
                "Please enter valid phone number",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        database
            .getReference("classSections")
            .get()
            .addOnSuccessListener { snapshot ->

                var foundClass = ""
                var foundSection = ""

                for (classSnapshot in snapshot.children) {

                    val className =
                        classSnapshot.key ?: continue

                    for (sectionSnapshot in classSnapshot.children) {

                        val sectionName =
                            sectionSnapshot.key ?: continue

                        val assignedPhone =
                            sectionSnapshot
                                .child("classTeacherPhone")
                                .getValue(String::class.java)

                        if (
                            assignedPhone?.trim() ==
                            teacherPhone
                        ) {

                            foundClass =
                                className

                            foundSection =
                                sectionName

                            break
                        }
                    }

                    if (foundClass.isNotEmpty()) {
                        break
                    }
                }

                if (foundClass.isEmpty()) {

                    binding.classDetailsCard.visibility =
                        View.GONE

                    Toast.makeText(
                        this,
                        "No class assigned to this teacher",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                binding.tvClass.text =
                    "Class: $foundClass"

                binding.tvSection.text =
                    "Section: $foundSection"

                binding.classDetailsCard.visibility =
                    View.VISIBLE
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    it.message
                        ?: "Failed to search assigned class",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}