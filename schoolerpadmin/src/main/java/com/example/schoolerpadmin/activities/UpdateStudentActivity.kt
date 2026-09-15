package com.example.schoolerpadmin.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.schoolerpadmin.databinding.ActivityUpdateStudentBinding
import com.example.schoolerpadmin.model.StudentModel
import com.example.schoolerpadmin.viewModel.StudentViewModel
import java.util.Calendar
import java.util.Locale

class UpdateStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateStudentBinding
    private lateinit var viewModel: StudentViewModel

    private var oldClass = ""
    private var oldSection = ""
    private var oldRoll = ""
    private var oldYear = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[StudentViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        oldClass = intent.getStringExtra("studentClass") ?: ""
        oldSection = intent.getStringExtra("studentSection") ?: ""
        oldRoll = intent.getStringExtra("studentRoll") ?: ""
        oldYear = intent.getStringExtra("studentYear") ?: ""

        loadStudentDetails(
            oldClass,
            oldSection,
            oldRoll,
            oldYear
        )

        binding.etStudentDOB.setOnClickListener {
            showDatePicker { date ->
                binding.etStudentDOB.setText(date)
            }
        }

        binding.btnUpdateStudent.setOnClickListener {
            updateStudent()
        }
    }

    private fun showDatePicker(
        onDateSelected: (String) -> Unit
    ) {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->

                val formattedDate = String.format(
                    Locale.getDefault(),
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
        ).show()
    }

    private fun loadStudentDetails(
        oldClass: String,
        oldSection: String,
        oldRoll: String,
        oldYear: String
    ) {
        viewModel.getStudent(
            oldClass,
            oldSection,
            oldRoll,
            oldYear
        ) { student ->

            student ?: return@getStudent

            binding.etStudentName.setText(student.studentName)
            binding.etStudentDOB.setText(student.studentDOB)
            binding.etStudentRoll.setText(student.studentRoll)
            binding.etStudentClass.setText(student.studentClass)
            binding.etStudentSection.setText(student.studentSection)
            binding.etStudentAddress.setText(student.studentAddress)
            binding.etYear.setText(student.studentYear)

            binding.etFatherName.setText(student.studentFatherName)
            binding.etFatherPhone.setText(student.studentFatherPhone)
            binding.etFatherOccupation.setText(student.studentFatherOccupation)
            binding.etFatherIncome.setText(student.studentFatherIncome.toString())

            binding.etMotherName.setText(student.studentMotherName)
            binding.etMotherPhone.setText(student.studentMotherPhone)
            binding.etMotherOccupation.setText(student.studentMotherOccupation)
            binding.etMotherIncome.setText(student.studentMotherIncome.toString())

            binding.etGuardianName.setText(student.studentGuardianName)
            binding.etGuardianPhone.setText(student.studentGuardianPhone)
            binding.etGuardianEmail.setText(student.studentGuardianEmail)
        }
    }

    private fun updateStudent() {

        val studentName = binding.etStudentName.text.toString().trim()
        val studentDOB = binding.etStudentDOB.text.toString().trim()
        val studentRoll = binding.etStudentRoll.text.toString().trim()
        val studentYear = binding.etYear.text.toString().trim()

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

        val studentAddress =
            binding.etStudentAddress.text.toString().trim()

        val studentFatherName =
            binding.etFatherName.text.toString().trim()

        val studentFatherPhone =
            binding.etFatherPhone.text.toString().trim()

        val studentFatherOccupation =
            binding.etFatherOccupation.text.toString().trim()

        val fatherIncomeText =
            binding.etFatherIncome.text.toString().trim()

        val studentFatherIncome =
            fatherIncomeText.toIntOrNull() ?: 0

        val studentMotherName =
            binding.etMotherName.text.toString().trim()

        val studentMotherPhone =
            binding.etMotherPhone.text.toString().trim()

        val studentMotherOccupation =
            binding.etMotherOccupation.text.toString().trim()

        val motherIncomeText =
            binding.etMotherIncome.text.toString().trim()

        val studentMotherIncome =
            motherIncomeText.toIntOrNull() ?: 0

        val studentGuardianName =
            binding.etGuardianName.text.toString().trim()

        val studentGuardianPhone =
            binding.etGuardianPhone.text.toString().trim()

        val studentGuardianEmail =
            binding.etGuardianEmail.text.toString().trim()

        if (
            studentName.isEmpty() ||
            studentDOB.isEmpty() ||
            studentRoll.isEmpty() ||
            studentClass.isEmpty() ||
            studentSection.isEmpty() ||
            studentAddress.isEmpty() ||
            studentYear.isEmpty() ||
            studentFatherName.isEmpty() ||
            studentFatherPhone.isEmpty() ||
            studentFatherOccupation.isEmpty() ||
            fatherIncomeText.isEmpty() ||
            studentMotherName.isEmpty() ||
            studentMotherPhone.isEmpty() ||
            studentMotherOccupation.isEmpty() ||
            motherIncomeText.isEmpty() ||
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

        if (!Patterns.EMAIL_ADDRESS.matcher(studentGuardianEmail).matches()) {
            Toast.makeText(
                this,
                "Invalid email address",
                Toast.LENGTH_SHORT
            ).show()
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

        if (
            studentFatherIncome < 0 ||
            studentMotherIncome < 0
        ) {
            Toast.makeText(
                this,
                "Income can't be negative",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val keyChanged =
            oldClass != studentClass ||
                    oldSection != studentSection ||
                    oldRoll != studentRoll ||
                    oldYear != studentYear

        if (keyChanged) {

            viewModel.isStudentExists(
                studentClass,
                studentSection,
                studentRoll,
                studentYear
            ) { exists ->

                if (exists) {
                    Toast.makeText(
                        this,
                        "Another student already exists with this Class, Section, Roll and Year",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@isStudentExists
                }

                saveUpdatedStudent(
                    studentName = studentName,
                    studentDOB = studentDOB,
                    studentRoll = studentRoll,
                    studentClass = studentClass,
                    studentSection = studentSection,
                    studentAddress = studentAddress,
                    studentYear = studentYear,
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
                    studentGuardianEmail = studentGuardianEmail
                )
            }

        } else {

            saveUpdatedStudent(
                studentName = studentName,
                studentDOB = studentDOB,
                studentRoll = studentRoll,
                studentClass = studentClass,
                studentSection = studentSection,
                studentAddress = studentAddress,
                studentYear = studentYear,
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
                studentGuardianEmail = studentGuardianEmail
            )
        }
    }

    private fun saveUpdatedStudent(
        studentName: String,
        studentDOB: String,
        studentRoll: String,
        studentClass: String,
        studentSection: String,
        studentAddress: String,
        studentYear: String,
        studentFatherName: String,
        studentFatherPhone: String,
        studentFatherOccupation: String,
        studentFatherIncome: Int,
        studentMotherName: String,
        studentMotherPhone: String,
        studentMotherOccupation: String,
        studentMotherIncome: Int,
        studentGuardianName: String,
        studentGuardianPhone: String,
        studentGuardianEmail: String
    ) {

        val student = StudentModel(
            studentName = studentName,
            studentDOB = studentDOB,
            studentRoll = studentRoll,
            studentClass = studentClass,
            studentSection = studentSection,
            studentAddress = studentAddress,
            studentYear = studentYear,
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
            studentGuardianEmail = studentGuardianEmail
        )

        val keyChanged =
            oldClass != studentClass ||
                    oldSection != studentSection ||
                    oldRoll != studentRoll ||
                    oldYear != studentYear

        if (keyChanged) {

            viewModel.updateStudent(student)
                .addOnSuccessListener {

                    viewModel.deleteStudent(
                        oldClass,
                        oldSection,
                        oldRoll,
                        oldYear
                    )
                        .addOnSuccessListener {

                            oldClass = studentClass
                            oldSection = studentSection
                            oldRoll = studentRoll
                            oldYear = studentYear

                            Toast.makeText(
                                this,
                                "Student updated successfully",
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
                        it.message ?: "Can't update student",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        } else {

            viewModel.updateStudent(student)
                .addOnSuccessListener {

                    oldClass = studentClass
                    oldSection = studentSection
                    oldRoll = studentRoll
                    oldYear = studentYear

                    Toast.makeText(
                        this,
                        "Student updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    setResult(RESULT_OK)
                    finish()
                }
                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        it.message ?: "Can't update student",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}