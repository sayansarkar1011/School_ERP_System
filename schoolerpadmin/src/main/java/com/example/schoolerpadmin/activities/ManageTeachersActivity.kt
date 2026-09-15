package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schoolerpadmin.R
import com.example.schoolerpadmin.adapter.TeacherAdapter
import com.example.schoolerpadmin.databinding.ActivityManageTeachersBinding
import com.example.schoolerpadmin.viewModel.TeacherViewModel

class ManageTeachersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageTeachersBinding
    private lateinit var adapter: TeacherAdapter
    private var isSearchMode = false
    private lateinit var viewModel: TeacherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageTeachersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TeacherViewModel::class.java]

        adapter = TeacherAdapter(
            onUpdateClick = { teacher ->

                val intent = Intent(this, UpdateTeacherActivity::class.java)
                intent.putExtra("teacherPhone",
                    teacher.teacherPhone)

                startActivity(intent)

            },
            onDeleteClick = { teacher ->

                AlertDialog.Builder(this)
                    .setTitle("Delete Teacher")
                    .setMessage(
                        "Delete ${teacher.teacherName}?"
                    )
                    .setPositiveButton("Delete") { _, _ ->

                        viewModel.deleteTeacher(
                            teacher.teacherPhone,
                            )
                            .addOnSuccessListener {

                                Toast.makeText(
                                    this,
                                    "Teacher deleted",
                                    Toast.LENGTH_SHORT
                                ).show()

                                loadTeachers()
                            }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )
        binding.rvTeachers.layoutManager =
            LinearLayoutManager(this)

        binding.rvTeachers.adapter = adapter

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnSearch.setOnClickListener {
            searchTeacher()
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {

                    if (isSearchMode) {

                        binding.etPhoneSearch.text?.clear()


                        binding.etPhoneSearch.clearFocus()


                        binding.root.requestFocus()

                        loadTeachers()

                        isSearchMode = false

                    } else {

                        finish()
                    }
                }
            }
        )

        loadTeachers()
    }

    override fun onResume() {
        super.onResume()

        loadTeachers()
    }

    private fun loadTeachers() {
        viewModel.getAllTeachers { teacherList ->
            adapter.updateList(teacherList)
        }
    }

    private fun searchTeacher() {
        val teacherPhone = binding.etPhoneSearch.text.toString().trim()

        if (teacherPhone.isEmpty()) {
            Toast.makeText(this, "Please add phone number", Toast.LENGTH_SHORT).show()
            return
        }
        if (teacherPhone.length < 10 || teacherPhone.length > 10){
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
        }

        viewModel.searchTeacher(
            teacherPhone
        ) { teacher ->

            if (teacher != null) {
                adapter.updateList(
                    listOf(teacher)
                )
                Toast.makeText(this, "Teacher found", Toast.LENGTH_SHORT).show()
                isSearchMode = true
            } else {

                Toast.makeText(
                    this,
                    "Teacher not found",
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