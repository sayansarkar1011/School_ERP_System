package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schoolerpadmin.adapter.StudentAdapter
import com.example.schoolerpadmin.databinding.ActivityManageStudentsBinding
import com.example.schoolerpadmin.viewModel.StudentViewModel

class ManageStudentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageStudentsBinding
    private lateinit var viewModel: StudentViewModel
    private lateinit var adapter: StudentAdapter

    private var isSearchMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityManageStudentsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this)[StudentViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        adapter = StudentAdapter(

            onUpdateClick = { student ->

                val intent =
                    Intent(
                        this,
                        UpdateStudentActivity::class.java
                    )

                intent.putExtra(
                    "studentClass",
                    student.studentClass
                )

                intent.putExtra(
                    "studentSection",
                    student.studentSection
                )

                intent.putExtra(
                    "studentRoll",
                    student.studentRoll
                )

                intent.putExtra(
                    "studentYear",
                    student.studentYear
                )

                startActivity(intent)
            },

            onDeleteClick = { student ->

                AlertDialog.Builder(this)
                    .setTitle("Delete Student")
                    .setMessage(
                        "Delete ${student.studentName}?"
                    )
                    .setPositiveButton("Delete") { _, _ ->

                        viewModel.deleteStudent(
                            student.studentClass,
                            student.studentSection,
                            student.studentRoll,
                            student.studentYear
                        )
                            .addOnSuccessListener {

                                Toast.makeText(
                                    this,
                                    "Student deleted",
                                    Toast.LENGTH_SHORT
                                ).show()

                                loadStudents()
                            }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )

        binding.rvStudents.layoutManager =
            LinearLayoutManager(this)

        binding.rvStudents.adapter = adapter

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnSearch.setOnClickListener {
            searchStudent()
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {

                    if (isSearchMode) {

                        binding.etClassSearch.text?.clear()
                        binding.etSectionSearch.text?.clear()
                        binding.etRollSearch.text?.clear()
                        binding.etYearSearch.text?.clear()

                        binding.etClassSearch.clearFocus()
                        binding.etSectionSearch.clearFocus()
                        binding.etRollSearch.clearFocus()
                        binding.etYearSearch.clearFocus()

                        binding.root.requestFocus()

                        loadStudents()

                        isSearchMode = false

                    } else {

                        finish()
                    }
                }
            }
        )

        loadStudents()
    }

    override fun onResume() {
        super.onResume()

        loadStudents()
    }

    private fun loadStudents() {

        viewModel.getAllStudents { studentList ->

            adapter.updateList(studentList)
        }
    }

    private fun searchStudent() {

        val studentClass =
            binding.etClassSearch.text
                .toString()
                .trim()
                .uppercase()

        val studentSection =
            binding.etSectionSearch.text
                .toString()
                .trim()
                .uppercase()

        val studentRoll =
            binding.etRollSearch.text
                .toString()
                .trim()

        val studentYear =
            binding.etYearSearch.text
                .toString()
                .trim()

        if (
            studentClass.isEmpty() ||
            studentSection.isEmpty() ||
            studentRoll.isEmpty() ||
            studentYear.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Please fill all the details",
                Toast.LENGTH_SHORT
            ).show()

            loadStudents()
            return
        }

        viewModel.searchStudent(
            studentClass,
            studentSection,
            studentRoll,
            studentYear
        ) { student ->

            if (student != null) {

                adapter.updateList(
                    listOf(student)
                )

                Toast.makeText(
                    this,
                    "Student found",
                    Toast.LENGTH_SHORT
                ).show()

                isSearchMode = true

            } else {

                Toast.makeText(
                    this,
                    "Student not found",
                    Toast.LENGTH_SHORT
                ).show()

                isSearchMode = true

                adapter.updateList(
                    emptyList()
                )
            }
        }
    }
}