package com.example.schoolerpadmin.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.ViewModelProvider

import com.example.schoolerpadmin.databinding.ActivityAddStudentBinding
import com.example.schoolerpadmin.model.StudentModel
import com.example.schoolerpadmin.viewModel.StudentViewModel
import java.util.Calendar

class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudentBinding
    private lateinit var viewModel: StudentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[StudentViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnAddStudent.setOnClickListener {
            addStudent()
        }

        binding.etStudentDOB.setOnClickListener {

            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->

                    val dob =
                        "$selectedDay/${selectedMonth + 1}/$selectedYear"

                    binding.etStudentDOB.setText(dob)

                },
                year,
                month,
                day
            ).show()
        }
    }

    private fun addStudent() {

        val studentName = binding.etStudentName.text.toString().trim()
        val studentDOB = binding.etStudentDOB.text.toString().trim()
        val studentRoll = binding.etStudentRoll.text.toString().trim()
        val studentClass =
            binding.etStudentClass.text
                .toString()
                .trim()
                .uppercase()

        val studentSection =
            binding.etStudentSection.text
                .toString()
                .trim()
                .uppercase()
        val studentAddress = binding.etStudentAddress.text.toString().trim()
        val studentYear = binding.etYear.text.toString().trim()
        val studentFatherName = binding.etFatherName.text.toString().trim()
        val studentFatherPhone = binding.etFatherPhone.text.toString().trim()
        val studentFatherOccupation = binding.etFatherOccupation.text.toString().trim()
        val studentFatherIncome =
            binding.etFatherIncome.text.toString()
                .trim()
                .toIntOrNull() ?: 0
        val studentMotherName = binding.etMotherName.text.toString().trim()
        val studentMotherPhone = binding.etMotherPhone.text.toString().trim()
        val studentMotherOccupation = binding.etMotherOccupation.text.toString().trim()
        val studentMotherIncome =
            binding.etMotherIncome.text.toString()
                .trim()
                .toIntOrNull() ?: 0
        val studentGuardianName = binding.etGuardianName.text.toString().trim()
        val studentGuardianPhone = binding.etGuardianPhone.text.toString().trim()
        val studentGuardianEmail = binding.etGuardianEmail.text.toString().trim()


        if (
            studentName.isEmpty() ||
            studentDOB.isEmpty() ||
            studentRoll.isEmpty() ||
            studentClass.isEmpty() ||
            studentSection.isEmpty() ||
            studentAddress.isEmpty() ||
            studentFatherName.isEmpty() ||
            studentFatherPhone.isEmpty() ||
            studentYear.isEmpty() ||
            binding.etFatherIncome.text.toString().trim().isEmpty() ||
            studentFatherOccupation.isEmpty() ||
            studentMotherName.isEmpty() ||
            studentMotherPhone.isEmpty() ||
            studentMotherOccupation.isEmpty() ||
            binding.etMotherIncome.text.toString().trim().isEmpty() ||
            studentGuardianName.isEmpty() ||
            studentGuardianPhone.isEmpty() ||
            studentGuardianEmail.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Please fill all the details",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(studentGuardianEmail)
                .matches()
        ) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (
            studentFatherPhone.length != 10 ||
            studentMotherPhone.length != 10 ||
            studentGuardianPhone.length != 10
        ) {
            Toast.makeText(
                this,
                "Enter valid phone number",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (studentRoll.toIntOrNull() == null) {
            Toast.makeText(
                this,
                "Invalid roll number",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (studentFatherIncome < 0 || studentMotherIncome < 0) {
            Toast.makeText(
                this,
                "Income can't be negative",
                Toast.LENGTH_SHORT
            ).show()
            return
        }


        viewModel.isStudentExists(
            studentClass,
            studentSection,
            studentRoll,
            studentYear
        ) { exists ->

            if (exists) {

                Toast.makeText(
                    this,
                    "Student already exists",
                    Toast.LENGTH_SHORT
                ).show()

                return@isStudentExists
            }

            val student = StudentModel(

                studentName = studentName,
                studentDOB = studentDOB,
                studentRoll = studentRoll,
                studentClass = studentClass,
                studentSection = studentSection,
                studentAddress = studentAddress,
                studentFatherName = studentFatherName,
                studentFatherPhone = studentFatherPhone,
                studentFatherOccupation = studentFatherOccupation,
                studentFatherIncome = studentFatherIncome,
                studentMotherName = studentMotherName,
                studentMotherPhone = studentMotherPhone,
                studentMotherOccupation = studentMotherOccupation,
                studentMotherIncome = studentMotherIncome,
                studentGuardianName = studentGuardianName,
                studentGuardianPhone = studentGuardianPhone,
                studentGuardianEmail = studentGuardianEmail,
                studentYear = studentYear
            )

            viewModel.addStudent(
                student
            )
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Student added successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.etStudentName.text?.clear()
                    binding.etStudentDOB.text?.clear()
                    binding.etStudentRoll.text?.clear()
                    binding.etStudentClass.text?.clear()
                    binding.etStudentSection.text?.clear()
                    binding.etStudentAddress.text?.clear()
                    binding.etYear.text?.clear()
                    binding.etFatherName.text?.clear()
                    binding.etFatherPhone.text?.clear()
                    binding.etFatherOccupation.text?.clear()
                    binding.etFatherIncome.text?.clear()
                    binding.etMotherName.text?.clear()
                    binding.etMotherPhone.text?.clear()

                    binding.etMotherOccupation.text?.clear()
                    binding.etMotherIncome.text?.clear()
                    binding.etGuardianName.text?.clear()
                    binding.etGuardianPhone.text?.clear()
                    binding.etGuardianEmail.text?.clear()


                }
                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        it.message ?: "Can't add student",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}