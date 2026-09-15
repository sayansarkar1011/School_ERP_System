package com.example.schoolerpadmin.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.schoolerpadmin.databinding.ActivityViewClassTeachersBinding
import com.example.schoolerpadmin.viewModel.TeacherViewModel
import com.google.firebase.database.FirebaseDatabase

class ViewClassTeachersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewClassTeachersBinding
    private lateinit var teacherViewModel: TeacherViewModel

    private val sectionDatabase =
        FirebaseDatabase.getInstance()
            .getReference("classSections")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityViewClassTeachersBinding.inflate(layoutInflater)

        setContentView(binding.root)

        teacherViewModel =
            ViewModelProvider(this)[TeacherViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnSearch.setOnClickListener {
            searchClassTeacher()
        }
    }

    private fun searchClassTeacher() {

        val classInput =
            binding.etClassSearch.text
                ?.toString()
                ?.trim()
                ?: ""

        val studentClass =
            classInput
                .replace("class", "", true)
                .trim()

        val finalClass =
            "Class $studentClass"

        val section =
            binding.etSectionSearch.text
                ?.toString()
                ?.trim()
                ?.uppercase()
                ?: ""

        val year =
            binding.etYearSearch.text
                ?.toString()
                ?.trim()
                ?: ""

        if (studentClass.isEmpty()) {
            binding.etClassSearch.error = "Enter class"
            return
        }

        if (section.isEmpty()) {
            binding.etSectionSearch.error = "Enter section"
            return
        }

        if (year.isEmpty()) {
            binding.etYearSearch.error = "Enter year"
            return
        }

        // Clear previous result
        binding.tvTeacherName.text = ""
        binding.tvAssignedClass.text = ""
        binding.tvAssignedSection.text = ""
        binding.tvAssignedYear.text = ""

        sectionDatabase
            .child(finalClass)
            .child(section)
            .child(year)
            .child("classTeacherPhone")
            .get()
            .addOnSuccessListener { snapshot ->

                val teacherPhone =
                    snapshot.getValue(String::class.java)

                if (teacherPhone.isNullOrEmpty()) {

                    Toast.makeText(
                        this,
                        "Class teacher not assigned",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                teacherViewModel.getTeacher(
                    teacherPhone
                ) { teacher ->

                    if (teacher == null) {

                        Toast.makeText(
                            this,
                            "Teacher not found",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@getTeacher
                    }

                    binding.tvTeacherName.text =
                        "Teacher Name: ${teacher.teacherName}"

                    binding.tvAssignedClass.text =
                        "Class: $finalClass"

                    binding.tvAssignedSection.text =
                        "Section: $section"

                    binding.tvAssignedYear.text =
                        "Year: $year"
                }
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    it.message
                        ?: "Failed to search class teacher",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}