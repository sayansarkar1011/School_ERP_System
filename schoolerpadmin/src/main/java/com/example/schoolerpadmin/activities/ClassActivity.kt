package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schoolerpadmin.adapter.ClassAdapter
import com.example.schoolerpadmin.databinding.ActivityClassBinding
import com.example.schoolerpadmin.model.ClassModel
import com.example.schoolerpadmin.viewModel.ClassViewModel

class ClassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassBinding
    private lateinit var viewModel: ClassViewModel
    private lateinit var adapter: ClassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ClassViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        setupRecyclerView()
        loadClasses()

        binding.btnAddClass.setOnClickListener {
            addClass()
        }
    }

    private fun setupRecyclerView() {

        adapter = ClassAdapter(

            onClassClick = { classModel ->

                val intent = Intent(
                    this,
                    SectionActivity::class.java
                )

                intent.putExtra(
                    "className",
                    classModel.className
                )

                startActivity(intent)
            },

            onDeleteClick = { classModel ->

                AlertDialog.Builder(this)
                    .setTitle("Delete Class")
                    .setMessage(
                        "Are you sure you want to delete ${classModel.className}?"
                    )
                    .setPositiveButton("Delete") { _, _ ->

                        viewModel.deleteClass(
                            classModel.className
                        )
                            .addOnSuccessListener {

                                Toast.makeText(
                                    this,
                                    "Class deleted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                loadClasses()
                            }
                            .addOnFailureListener {

                                Toast.makeText(
                                    this,
                                    it.message ?: "Failed to delete class",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )

        binding.rvClasses.layoutManager =
            LinearLayoutManager(this)

        binding.rvClasses.adapter =
            adapter
    }

    private fun loadClasses() {

        viewModel.getAllClasses { classList ->

            adapter.updateList(classList)
        }
    }

    private fun addClass() {

        val editText = android.widget.EditText(this).apply {
            hint = "Enter class name"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or
                    android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            isSingleLine = true
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Class")
            .setView(editText)
            .setPositiveButton("Add", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {

            editText.requestFocus()

            dialog.window?.setSoftInputMode(
                android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            )

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener {

                    val className =
                        editText.text.toString().trim()

                    if (className.isEmpty()) {

                        editText.error = "Please enter class name"
                        return@setOnClickListener
                    }

                    val classModel = ClassModel(
                        className = className
                    )

                    viewModel.addClass(classModel)
                        .addOnSuccessListener {

                            Toast.makeText(
                                this,
                                "Class added successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            dialog.dismiss()
                            loadClasses()
                        }
                        .addOnFailureListener {

                            Toast.makeText(
                                this,
                                it.message ?: "Failed to add class",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
        }

        dialog.show()
    }
}