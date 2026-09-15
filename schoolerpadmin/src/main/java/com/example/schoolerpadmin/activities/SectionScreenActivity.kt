package com.example.schoolerpadmin.activities

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.schoolerpadmin.databinding.ActivitySectionScreenBinding
import com.example.schoolerpadmin.viewModel.TeacherViewModel
import com.google.firebase.database.FirebaseDatabase

class SectionScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySectionScreenBinding
    private lateinit var teacherViewModel: TeacherViewModel

    private val sectionDatabase =
        FirebaseDatabase.getInstance()
            .getReference("classSections")

    private var className = ""
    private var sectionName = ""
    private var year = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivitySectionScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        teacherViewModel =
            ViewModelProvider(this)[TeacherViewModel::class.java]

        className =
            intent.getStringExtra("className") ?: ""

        sectionName =
            intent.getStringExtra("sectionName") ?: ""

        binding.tvSectionName.text =
            "$className - Section $sectionName"

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnAddClassTeacher.setOnClickListener {

            year = getEnteredYear()

            if (year.isNotEmpty()) {
                addClassTeacher()
            }
        }

        binding.btnUpdateClassTeacher.setOnClickListener {

            year = getEnteredYear()

            if (year.isNotEmpty()) {
                updateClassTeacher()
            }
        }

        binding.btnRemoveClassTeacher.setOnClickListener {

            year = getEnteredYear()

            if (year.isNotEmpty()) {
                removeClassTeacher()
            }
        }

        binding.etYear.setOnFocusChangeListener { _, hasFocus ->

            if (!hasFocus) {

                val enteredYear =
                    binding.etYear.text
                        .toString()
                        .trim()

                if (enteredYear.isNotEmpty()) {

                    year = enteredYear

                    loadAssignedTeacher()
                }
            }
        }

        binding.tvAssignedTeacherName.text =
            "Not Assigned"

        binding.tvAssignedTeacherPhone.text =
            "Phone: Not Available"
    }

    // ---------------------------------------------------------
    // YEAR
    // ---------------------------------------------------------

    private fun getEnteredYear(): String {

        val enteredYear =
            binding.etYear.text
                .toString()
                .trim()

        if (enteredYear.isEmpty()) {

            binding.etYear.error =
                "Enter year"

            return ""
        }

        year = enteredYear

        return year
    }

    // ---------------------------------------------------------
    // ADD CLASS TEACHER
    // ---------------------------------------------------------

    private fun addClassTeacher() {

        sectionDatabase
            .child(className)
            .child(sectionName)
            .child(year)
            .child("classTeacherPhone")
            .get()
            .addOnSuccessListener { snapshot ->

                val existingPhone =
                    snapshot.getValue(String::class.java)

                if (!existingPhone.isNullOrEmpty()) {

                    Toast.makeText(
                        this,
                        "Class teacher already assigned",
                        Toast.LENGTH_SHORT
                    ).show()

                    loadAssignedTeacher()

                    return@addOnSuccessListener
                }

                showTeacherDialog(
                    "Add Class Teacher",
                    "Assign"
                )
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    it.message
                        ?: "Failed to check teacher",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // ---------------------------------------------------------
    // UPDATE CLASS TEACHER
    // ---------------------------------------------------------

    private fun updateClassTeacher() {

        showTeacherDialog(
            "Update Class Teacher",
            "Update"
        )
    }

    // ---------------------------------------------------------
    // TEACHER DIALOG
    // ---------------------------------------------------------

    private fun showTeacherDialog(
        title: String,
        buttonText: String
    ) {

        val editText =
            EditText(this).apply {

                hint =
                    "Enter teacher phone number"

                inputType =
                    InputType.TYPE_CLASS_PHONE

                isSingleLine = true
            }

        val dialog =
            AlertDialog.Builder(this)
                .setTitle(title)
                .setView(editText)
                .setPositiveButton(
                    buttonText,
                    null
                )
                .setNegativeButton(
                    "Cancel",
                    null
                )
                .create()

        dialog.setOnShowListener {

            editText.requestFocus()

            dialog.window?.setSoftInputMode(
                android.view.WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_VISIBLE
            )

            dialog.getButton(
                AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener {

                val phone =
                    editText.text
                        .toString()
                        .trim()

                if (phone.isEmpty()) {

                    editText.error =
                        "Enter teacher phone number"

                    return@setOnClickListener
                }

                if (phone.length != 10) {

                    editText.error =
                        "Enter valid phone number"

                    return@setOnClickListener
                }

                teacherViewModel.getTeacher(
                    phone
                ) { teacher ->

                    if (teacher == null) {

                        Toast.makeText(
                            this,
                            "Teacher not found",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@getTeacher
                    }

                    sectionDatabase
                        .child(className)
                        .child(sectionName)
                        .child(year)
                        .child("classTeacherPhone")
                        .setValue(phone)
                        .addOnSuccessListener {

                            binding.tvAssignedTeacherName.text =
                                teacher.teacherName

                            binding.tvAssignedTeacherPhone.text =
                                "Phone: ${teacher.teacherPhone}"

                            Toast.makeText(
                                this,
                                if (buttonText == "Assign") {
                                    "Class teacher assigned"
                                } else {
                                    "Class teacher updated"
                                },
                                Toast.LENGTH_SHORT
                            ).show()

                            dialog.dismiss()
                        }
                        .addOnFailureListener {

                            Toast.makeText(
                                this,
                                it.message
                                    ?: "Failed to save teacher",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }

        dialog.show()
    }

    // ---------------------------------------------------------
    // LOAD ASSIGNED TEACHER
    // ---------------------------------------------------------

    private fun loadAssignedTeacher() {

        if (year.isEmpty()) {

            binding.tvAssignedTeacherName.text =
                "Not Assigned"

            binding.tvAssignedTeacherPhone.text =
                "Phone: Not Available"

            return
        }

        sectionDatabase
            .child(className)
            .child(sectionName)
            .child(year)
            .child("classTeacherPhone")
            .get()
            .addOnSuccessListener { snapshot ->

                val phone =
                    snapshot.getValue(String::class.java)

                if (phone.isNullOrEmpty()) {

                    binding.tvAssignedTeacherName.text =
                        "Not Assigned"

                    binding.tvAssignedTeacherPhone.text =
                        "Phone: Not Available"

                    return@addOnSuccessListener
                }

                teacherViewModel.getTeacher(
                    phone
                ) { teacher ->

                    if (teacher != null) {

                        binding.tvAssignedTeacherName.text =
                            teacher.teacherName

                        binding.tvAssignedTeacherPhone.text =
                            "Phone: ${teacher.teacherPhone}"

                    } else {

                        binding.tvAssignedTeacherName.text =
                            "Teacher Not Found"

                        binding.tvAssignedTeacherPhone.text =
                            "Phone: $phone"
                    }
                }
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    it.message
                        ?: "Failed to load teacher",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // ---------------------------------------------------------
    // REMOVE CLASS TEACHER
    // ---------------------------------------------------------

    private fun removeClassTeacher() {

        AlertDialog.Builder(this)
            .setTitle("Remove Class Teacher")
            .setMessage(
                "Are you sure you want to remove the class teacher?"
            )
            .setPositiveButton("Remove") { _, _ ->

                sectionDatabase
                    .child(className)
                    .child(sectionName)
                    .child(year)
                    .child("classTeacherPhone")
                    .removeValue()
                    .addOnSuccessListener {

                        binding.tvAssignedTeacherName.text =
                            "Not Assigned"

                        binding.tvAssignedTeacherPhone.text =
                            "Phone: Not Available"

                        Toast.makeText(
                            this,
                            "Class teacher removed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {

                        Toast.makeText(
                            this,
                            it.message
                                ?: "Failed to remove teacher",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .setNegativeButton(
                "Cancel",
                null
            )
            .show()
    }
}