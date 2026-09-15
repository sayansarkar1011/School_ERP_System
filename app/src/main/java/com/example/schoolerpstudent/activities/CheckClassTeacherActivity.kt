package com.example.schoolerpstudent.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolerpstudent.databinding.ActivityCheckClassTeacherBinding
import com.google.firebase.database.FirebaseDatabase

class CheckClassTeacherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckClassTeacherBinding

    private val database =
        FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityCheckClassTeacherBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnSearch.setOnClickListener {
            searchTeacher()
        }

        binding.teacherDetailsCard.visibility =
            View.GONE
    }

    private fun searchTeacher() {

        val enteredClass =
            binding.etClass.text
                .toString()
                .trim()

        val section =
            binding.etSection.text
                .toString()
                .trim()
                .uppercase()

        if (
            enteredClass.isEmpty() ||
            section.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Please enter all the details",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // Supports both:
        // 9
        // Class 9
        val className =
            if (
                enteredClass.startsWith(
                    "class",
                    ignoreCase = true
                )
            ) {
                enteredClass
            } else {
                "Class $enteredClass"
            }

        searchClassTeacher()
    }

    private fun searchClassTeacher() {

        val enteredClass =
            binding.etClass.text
                .toString()
                .trim()

        val sectionName =
            binding.etSection.text
                .toString()
                .trim()
                .uppercase()

        if (
            enteredClass.isEmpty() ||
            sectionName.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Please enter all the details",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        database
            .getReference("classSections")
            .get()
            .addOnSuccessListener { snapshot ->

                val enteredClassNumber =
                    enteredClass
                        .filter { it.isDigit() }

                var matchedClassName: String? = null

                for (classSnapshot in snapshot.children) {

                    val firebaseClassName =
                        classSnapshot.key ?: continue

                    val firebaseClassNumber =
                        firebaseClassName
                            .filter { it.isDigit() }

                    if (
                        firebaseClassNumber ==
                        enteredClassNumber
                    ) {
                        matchedClassName = firebaseClassName
                        break
                    }
                }

                if (matchedClassName == null) {

                    binding.teacherDetailsCard.visibility =
                        View.GONE

                    Toast.makeText(
                        this,
                        "Class not found",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                database
                    .getReference("classSections")
                    .child(matchedClassName)
                    .child(sectionName)
                    .child("classTeacherPhone")
                    .get()
                    .addOnSuccessListener { teacherSnapshot ->

                        val teacherPhone =
                            teacherSnapshot
                                .getValue(String::class.java)

                        if (teacherPhone.isNullOrEmpty()) {

                            binding.teacherDetailsCard.visibility =
                                View.GONE

                            Toast.makeText(
                                this,
                                "Class teacher not assigned",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@addOnSuccessListener
                        }

                        database
                            .getReference("teachers")
                            .child(teacherPhone)
                            .child("teacherName")
                            .get()
                            .addOnSuccessListener { nameSnapshot ->

                                val teacherName =
                                    nameSnapshot
                                        .getValue(String::class.java)

                                if (teacherName.isNullOrEmpty()) {

                                    binding.teacherDetailsCard.visibility =
                                        View.GONE

                                    Toast.makeText(
                                        this,
                                        "Teacher not found",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    return@addOnSuccessListener
                                }

                                binding.tvTeacherName.text =
                                    "Teacher Name: $teacherName"

                                binding.teacherDetailsCard.visibility =
                                    View.VISIBLE
                            }
                    }
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    it.message ?: "Failed to search class teacher",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}