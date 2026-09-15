package com.example.schoolerpadmin.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.schoolerpadmin.R
import com.example.schoolerpadmin.databinding.ActivityAddTeacherBinding
import com.example.schoolerpadmin.model.TeacherModel
import com.example.schoolerpadmin.viewModel.TeacherViewModel
import java.util.Calendar
import kotlin.time.Duration.Companion.milliseconds

class AddTeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTeacherBinding
    private lateinit var viewModel: TeacherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TeacherViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.etTeacherDob.setOnClickListener {
            teacherDOB()
        }

        binding.etJoiningDate.setOnClickListener {
            teacherJoining()
        }

        binding.btnAddTeacher.setOnClickListener {
            addTeacher()
        }



    }

    private fun teacherDOB() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->

                val dob =
                    "$selectedDay/${selectedMonth + 1}/$selectedYear"

                binding.etTeacherDob.setText(dob)

            },
            year,
            month,
            day
        ).show()
    }

    private fun teacherJoining () {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->

                val dob =
                    "$selectedDay/${selectedMonth + 1}/$selectedYear"

                binding.etJoiningDate.setText(dob)

            },
            year,
            month,
            day
        ).show()
    }

    private fun addTeacher() {
        val teacherName = binding.etTeacherName.text.toString().trim()
        val teacherEmail = binding.etTeacherEmail.text.toString().trim()
        val teacherPhone = binding.etTeacherPhone.text.toString().trim()
        val teacherGender = binding.etTeacherGender.text.toString().trim()
        val teacherDOB =binding.etTeacherDob.text.toString().trim()
        val teacherSubject = binding.etTeacherSubject.text.toString().trim()
        val teacherQualification = binding.etTeacherQualification.text.toString().trim()
        val teacherAddress = binding.etTeacherAddress.text.toString().trim()
        val teacherJoining = binding.etJoiningDate.text.toString().trim()
        val teacherSalary = binding.etSalary.text.toString().trim().toIntOrNull() ?: 0

        // Validation.....

        if (
            teacherName.isEmpty() ||
            teacherEmail.isEmpty() ||
            teacherPhone.isEmpty() ||
            teacherAddress.isEmpty() ||
            teacherJoining.isEmpty() ||
            teacherDOB.isEmpty() ||
            teacherGender.isEmpty() ||
            teacherSubject.isEmpty() ||
            teacherQualification.isEmpty() ||
            binding.etSalary.text.toString().trim().isEmpty()
            ){
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(teacherEmail)
                .matches()
        ) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (
           teacherPhone.length != 10
            ) {
            Toast.makeText(
                this,
                "Enter valid phone number",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (teacherSalary < 0 ) {
            Toast.makeText(
                this,
                "Income can't be negative",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // data put into data class with checking the duplicate data

        viewModel.isTeacherExists(
            teacherPhone
        ) { exists ->
            if (exists) {

                Toast.makeText(
                    this,
                    "Teacher already exists",
                    Toast.LENGTH_SHORT
                ).show()

                return@isTeacherExists
            }

            val teacher = TeacherModel(
                teacherName = teacherName,
                teacherPhone = teacherPhone,
                teacherEmail = teacherEmail,
                teacherGender = teacherGender,
                teacherDob = teacherDOB,
                teacherSubject = teacherSubject,
                teacherQualification = teacherQualification,
                teacherAddress = teacherAddress,
                joiningDate = teacherJoining,
                salary = teacherSalary
            )

            viewModel.addTeacher(
                teacher
            )
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Teacher added successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.etTeacherName.text?.clear()
                    binding.etTeacherPhone.text?.clear()
                    binding.etTeacherEmail.text?.clear()
                    binding.etTeacherGender.text?.clear()
                    binding.etTeacherDob.text?.clear()
                    binding.etTeacherSubject.text?.clear()
                    binding.etTeacherAddress.text?.clear()
                    binding.etTeacherQualification.text?.clear()
                    binding.etJoiningDate.text?.clear()
                    binding.etSalary.text?.clear()
                }
                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        it.message ?: "Can't add teacher",
                        Toast.LENGTH_SHORT
                    ).show()
                }


        }
    }
}