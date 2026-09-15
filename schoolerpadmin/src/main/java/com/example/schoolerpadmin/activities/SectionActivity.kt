package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schoolerpadmin.adapter.SectionAdapter
import com.example.schoolerpadmin.databinding.ActivitySectionBinding
import com.example.schoolerpadmin.model.SectionModel
import com.example.schoolerpadmin.viewModel.SectionViewModel

class SectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySectionBinding
    private lateinit var viewModel: SectionViewModel
    private lateinit var adapter: SectionAdapter

    private var className = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this)[SectionViewModel::class.java]

        className =
            intent.getStringExtra("className") ?: ""

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        setupRecyclerView()
        loadSections()

        binding.btnAddSection.setOnClickListener {
            addSection()
        }
    }

    private fun setupRecyclerView() {

        adapter = SectionAdapter(
            className = className,

            onSectionClick = { className, sectionModel ->

                val intent = Intent(
                    this,
                    SectionScreenActivity::class.java
                )

                intent.putExtra(
                    "className",
                    className
                )

                intent.putExtra(
                    "sectionName",
                    sectionModel.sectionName
                )

                startActivity(intent)
            },

            onDeleteClick = { sectionModel ->

                AlertDialog.Builder(this)
                    .setTitle("Delete Section")
                    .setMessage(
                        "Are you sure you want to delete ${sectionModel.sectionName}?"
                    )
                    .setPositiveButton("Delete") { _, _ ->

                        viewModel.deleteSection(
                            className,
                            sectionModel.sectionName
                        )
                            .addOnSuccessListener {

                                Toast.makeText(
                                    this,
                                    "Section deleted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                loadSections()
                            }
                            .addOnFailureListener {

                                Toast.makeText(
                                    this,
                                    it.message
                                        ?: "Failed to delete section",
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
        )

        binding.rvSections.layoutManager =
            LinearLayoutManager(this)

        binding.rvSections.adapter =
            adapter
    }

    private fun loadSections() {

        viewModel.getAllSections(
            className
        ) { sectionList ->

            adapter.updateList(
                sectionList
            )
        }
    }

    private fun addSection() {

        val editText = EditText(this).apply {

            hint = "Enter section name"

            inputType =
                InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS

            isSingleLine = true
        }

        val dialog =
            AlertDialog.Builder(this)
                .setTitle("Add Section")
                .setView(editText)
                .setPositiveButton(
                    "Add",
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

                val sectionName =
                    editText.text
                        .toString()
                        .trim()
                        .uppercase()

                if (sectionName.isEmpty()) {

                    editText.error =
                        "Please enter section name"

                    return@setOnClickListener
                }

                val sectionModel =
                    SectionModel(
                        sectionName = sectionName,
                        className = className
                    )

                viewModel.addSection(
                    sectionModel
                )
                    .addOnSuccessListener {

                        Toast.makeText(
                            this,
                            "Section added successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        dialog.dismiss()

                        loadSections()
                    }
                    .addOnFailureListener {

                        Toast.makeText(
                            this,
                            it.message
                                ?: "Failed to add section",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

        dialog.show()
    }
}