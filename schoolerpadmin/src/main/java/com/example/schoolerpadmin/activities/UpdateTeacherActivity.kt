package com.example.schoolerpadmin.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.schoolerpadmin.R
import com.example.schoolerpadmin.databinding.ActivityUpdateTeacherBinding
import com.example.schoolerpadmin.model.TeacherModel
import com.example.schoolerpadmin.viewModel.TeacherViewModel

class UpdateTeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateTeacherBinding
    private lateinit var viewModel: TeacherViewModel
    private var oldPhone = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[TeacherViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
        oldPhone = intent.getStringExtra("teacherPhone") ?: ""

        loadTeacherDetails(oldPhone)

        binding.etTeacherDob.setOnClickListener {
            showDatePicker { date ->
                binding.etTeacherDob.setText(date)
            }
        }

        binding.etJoiningDate.setOnClickListener {
            showDatePicker { date ->
                binding.etJoiningDate.setText(date)
            }
        }

        binding.btnUpdateTeacher.setOnClickListener {
            updateTeacher()
        }


    }

    private fun loadTeacherDetails(oldPhone: String) {
        viewModel.getTeacher(oldPhone) { teacher ->

            teacher ?: return@getTeacher

            binding.etTeacherName.setText(teacher.teacherName)
            binding.etTeacherPhone.setText(teacher.teacherPhone)
            binding.etTeacherEmail.setText(teacher.teacherEmail)
            binding.etTeacherGender.setText(teacher.teacherGender)
            binding.etTeacherDob.setText(teacher.teacherDob)
            binding.etTeacherSubject.setText(teacher.teacherSubject)
            binding.etTeacherQualification.setText(teacher.teacherQualification)
            binding.etTeacherAddress.setText(teacher.teacherAddress)
            binding.etJoiningDate.setText(teacher.joiningDate)
            binding.etSalary.setText(teacher.salary.toString())
        }
    }

    private fun showDatePicker(
        onDateSelected: (String) -> Unit
    ) {

        val calendar = java.util.Calendar.getInstance()

        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH)
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

        val datePickerDialog = android.app.DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->

                val formattedDate = String.format(
                    java.util.Locale.getDefault(),
                    "%02d/%02d/%04d",
                    selectedDay,
                    selectedMonth + 1,
                    selectedYear
                )

                onDateSelected(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun updateTeacher() {
        val teacherName = binding.etTeacherName.text.toString().trim()
        val teacherEmail = binding.etTeacherEmail.text.toString().trim()
        val teacherPhone = binding.etTeacherPhone.text.toString().trim()
        val teacherGender = binding.etTeacherGender.text.toString().trim()
        val teacherDOB = binding.etTeacherDob.text.toString().trim()
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
        ) {
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

        if (teacherSalary < 0) {
            Toast.makeText(
                this,
                "Income can't be negative",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // data put into data class with checking the duplicate data

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
        if (oldPhone != teacherPhone){

            viewModel.updateTeacher(teacher)
                .addOnSuccessListener {

                    viewModel.deleteTeacher(
                        oldPhone
                    )
                        .addOnSuccessListener {

                            oldPhone = teacherPhone
                            Toast.makeText(
                                this,
                                "Teacher updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            setResult(RESULT_OK)
                            finish()
                        }
                        .addOnFailureListener {

                            Toast.makeText(
                                this,
                                it.message ?: "Can't delete old record",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        it.message ?: "Can't update teacher",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
        else {

            viewModel.updateTeacher(teacher)
                .addOnSuccessListener {

                    oldPhone = teacherPhone
                    Toast.makeText(
                        this,
                        "Teacher updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    setResult(RESULT_OK)
                    finish()
                }
                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        it.message ?: "Can't update teacher",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        }
    }
