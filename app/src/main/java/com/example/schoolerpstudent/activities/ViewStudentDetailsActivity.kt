package com.example.schoolerpstudent.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolerpstudent.databinding.ActivityViewStudentDetailsBinding
import com.example.schoolerpstudent.model.StudentModel
import com.google.firebase.database.FirebaseDatabase

class ViewStudentDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewStudentDetailsBinding

    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnSearch.setOnClickListener {
            searchStudent()
        }
    }

    private fun searchStudent() {

        val studentClass =
            binding.etClass.text
                .toString()
                .trim()
                .uppercase()

        val studentSection =
            binding.etSection.text
                .toString()
                .trim()
                .uppercase()

        val studentRoll =
            binding.etRoll.text
                .toString()
                .trim()

        val studentYear = binding.etYear.text.toString().trim()

        if (
            studentClass.isEmpty() ||
            studentSection.isEmpty() ||
            studentRoll.isEmpty() ||
            studentYear.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Please enter class, section, roll and year",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        database.getReference("students")
            .child(studentClass)
            .child(studentSection)
            .child(studentRoll)
            .child(studentYear)
            .get()
            .addOnSuccessListener { snapshot ->

                if (!snapshot.exists()) {

                    clearStudentDetails()

                    Toast.makeText(
                        this,
                        "Student not found",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                val student =
                    snapshot.getValue(StudentModel::class.java)

                if (student == null) {

                    clearStudentDetails()

                    Toast.makeText(
                        this,
                        "Failed to load student details",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                showStudentDetails(student)

                Toast.makeText(
                    this,
                    "Student found",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {

                clearStudentDetails()

                Toast.makeText(
                    this,
                    it.message ?: "Failed to load student details",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun showStudentDetails(
        student: StudentModel
    ) {

        binding.tvStudentName.text =
            "Student Name: ${student.studentName}"

        binding.tvStudentDOB.text =
            "Date Of Birth: ${student.studentDOB}"

        binding.tvStudentRoll.text =
            "Roll: ${student.studentRoll}"

        binding.tvStudentClass.text =
            "Class: ${student.studentClass}"

        binding.tvStudentSection.text =
            "Section: ${student.studentSection}"

        binding.tvStudentAddress.text =
            "Address: ${student.studentAddress}"

        binding.tvStudentYear.text =
            "Year: ${student.studentYear}"

        binding.tvFatherName.text =
            "Father Name: ${student.studentFatherName}"

        binding.tvMotherName.text =
            "Mother Name: ${student.studentMotherName}"

        binding.tvGuardianName.text =
            "Guardian Name: ${student.studentGuardianName}"
    }

    private fun clearStudentDetails() {

        binding.tvStudentName.text =
            "Student Name: "

        binding.tvStudentDOB.text =
            "Date Of Birth: "

        binding.tvStudentRoll.text =
            "Roll: "

        binding.tvStudentClass.text =
            "Class: "

        binding.tvStudentSection.text =
            "Section: "

        binding.tvStudentAddress.text =
            "Address: "

        binding.tvStudentYear.text =
            "Year: "

        binding.tvFatherName.text =
            "Father Name: "

        binding.tvMotherName.text =
            "Mother Name: "

        binding.tvGuardianName.text =
            "Guardian Name: "
    }
}