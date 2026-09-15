package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schoolerpadmin.adapter.ResultAdapter
import com.example.schoolerpadmin.databinding.ActivityViewAllResultsBinding
import com.example.schoolerpadmin.viewModel.ResultViewModel

class ViewAllResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewAllResultsBinding
    private lateinit var viewModel: ResultViewModel
    private lateinit var adapter: ResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityViewAllResultsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this)[ResultViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        adapter = ResultAdapter(

            onUpdateClick = { result ->

                val intent =
                    Intent(
                        this,
                        UpdateResultsActivity::class.java
                    )

                intent.putExtra(
                    "studentClass",
                    result.studentClass
                )

                intent.putExtra(
                    "studentSection",
                    result.studentSection
                )

                intent.putExtra(
                    "studentRoll",
                    result.studentRoll
                )

                intent.putExtra(
                    "studentYear",
                    result.year
                )

                startActivity(intent)
            },

            onDeleteClick = { result ->

                AlertDialog.Builder(this)
                    .setTitle("Delete Result")
                    .setMessage(
                        "Delete ${result.studentName}?"
                    )
                    .setPositiveButton("Delete") { _, _ ->

                        viewModel.deleteResult(
                            result.studentClass,
                            result.studentSection,
                            result.studentRoll,
                            result.year
                        )
                            .addOnSuccessListener {

                                Toast.makeText(
                                    this,
                                    "Result deleted",
                                    Toast.LENGTH_SHORT
                                ).show()

                                loadResults()
                            }
                            .addOnFailureListener {

                                Toast.makeText(
                                    this,
                                    it.message
                                        ?: "Failed to delete result",
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

        binding.rvStudents.layoutManager =
            LinearLayoutManager(this)

        binding.rvStudents.adapter =
            adapter

        binding.btnSearch.setOnClickListener {
            searchResults()
        }
    }

    override fun onResume() {
        super.onResume()

        loadResults()
    }

    private fun loadResults() {

        viewModel.getAllResults { resultList ->

            adapter.updateList(
                resultList
            )
        }
    }

    private fun searchResults() {

        val studentClassInput =
            binding.etClassSearch.text
                .toString()
                .trim()

        val studentClass =
            studentClassInput
                .replace("class", "", true)
                .trim()

        val finalClass =
            "Class $studentClass"

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
                "Please enter all the details",
                Toast.LENGTH_SHORT
            ).show()

            loadResults()

            return
        }

        viewModel.searchResults(
            finalClass,
            studentSection,
            studentRoll,
            studentYear
        ) { result ->

            if (result != null) {

                adapter.updateList(
                    listOf(result)
                )

                Toast.makeText(
                    this,
                    "Result found",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                adapter.updateList(
                    emptyList()
                )

                Toast.makeText(
                    this,
                    "Result not found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}