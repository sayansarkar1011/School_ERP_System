package com.example.schoolerpadmin.activities

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.schoolerpadmin.databinding.ActivityManageResultsBinding
import com.example.schoolerpadmin.model.ResultModel
import com.example.schoolerpadmin.model.StudentModel
import com.example.schoolerpadmin.model.SubjectResultModel
import com.example.schoolerpadmin.viewModel.ResultViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class ManageResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageResultsBinding
    private lateinit var viewModel: ResultViewModel

    private val studentDatabase =
        FirebaseDatabase.getInstance()
            .getReference("students")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityManageResultsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this)[ResultViewModel::class.java]

        addSubjectRow()

        binding.btnAddSubject.setOnClickListener {
            addSubjectRow()
        }

        binding.btnRemoveSubject.setOnClickListener {
            removeSubjectRow()
        }

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnSaveResult.setOnClickListener {
            saveResult()
        }

        binding.etTotalMarks.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                calculatePercentageAndGrade()
            }
        }

        binding.etObtainedMarks.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                calculatePercentageAndGrade()
            }
        }
    }

    private fun saveResult() {

        val studentName =
            binding.etStudentName.text
                .toString()
                .trim()

        val studentRoll =
            binding.etStudentRoll.text
                .toString()
                .trim()

        val studentClass =
            binding.etClass.text
                .toString()
                .trim()
                .replace("class", "", true)
                .trim()

        val finalClass =
            "Class $studentClass"

        val studentSection =
            binding.etSection.text
                .toString()
                .trim()
                .uppercase()

        val examName =
            binding.etExamName.text
                .toString()
                .trim()

        val totalMarks =
            binding.etTotalMarks.text
                .toString()
                .trim()
                .toIntOrNull() ?: 0

        val obtainedMarks =
            binding.etObtainedMarks.text
                .toString()
                .trim()
                .toIntOrNull() ?: 0

        val percentage =
            binding.etPercentage.text
                .toString()
                .trim()
                .toDoubleOrNull() ?: 0.0

        val grade =
            binding.etGrade.text
                .toString()
                .trim()

        val year =
            binding.etYear.text
                .toString()
                .trim()

        if (
            studentName.isEmpty() ||
            studentRoll.isEmpty() ||
            studentClass.isEmpty() ||
            studentSection.isEmpty() ||
            examName.isEmpty() ||
            year.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Please fill all required fields",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // FIRST: Check whether this exact student exists
        checkStudentExists(
            studentClass = studentClass,
            studentSection = studentSection,
            studentRoll = studentRoll,
            studentYear = year
        ) { studentSnapshot ->

            if (studentSnapshot == null) {

                Toast.makeText(
                    this,
                    "This student does not exist",
                    Toast.LENGTH_SHORT
                ).show()

                return@checkStudentExists
            }

            val student =
                studentSnapshot.getValue(StudentModel::class.java)

            if (student == null) {

                Toast.makeText(
                    this,
                    "This student does not exist",
                    Toast.LENGTH_SHORT
                ).show()

                return@checkStudentExists
            }

            /*
             * Extra validation:
             * The entered class, section, roll and year
             * must also match the actual StudentModel data.
             */

            val actualClass =
                student.studentClass
                    .trim()
                    .replace("class", "", true)
                    .trim()

            val actualSection =
                student.studentSection
                    .trim()
                    .uppercase()

            val actualRoll =
                student.studentRoll
                    .trim()

            val actualYear =
                student.studentYear
                    .trim()

            if (
                actualClass != studentClass ||
                actualSection != studentSection ||
                actualRoll != studentRoll ||
                actualYear != year
            ) {

                Toast.makeText(
                    this,
                    "Student details do not match",
                    Toast.LENGTH_SHORT
                ).show()

                return@checkStudentExists
            }

            /*
             * Student exists and details match.
             * Now collect subjects.
             */

            val subjectList =
                mutableListOf<SubjectResultModel>()

            for (
            i in 0 until binding.subjectContainer.childCount
            ) {

                val row =
                    binding.subjectContainer
                        .getChildAt(i) as LinearLayout

                val subjectName =
                    (((row.getChildAt(0) as TextInputLayout)
                        .editText) as TextInputEditText)
                        .text
                        .toString()
                        .trim()

                val fullMarks =
                    (((row.getChildAt(1) as TextInputLayout)
                        .editText) as TextInputEditText)
                        .text
                        .toString()
                        .trim()
                        .toIntOrNull() ?: 0

                val subjectMarks =
                    (((row.getChildAt(2) as TextInputLayout)
                        .editText) as TextInputEditText)
                        .text
                        .toString()
                        .trim()
                        .toIntOrNull() ?: 0

                val subjectGrade =
                    (((row.getChildAt(3) as TextInputLayout)
                        .editText) as TextInputEditText)
                        .text
                        .toString()
                        .trim()

                if (subjectName.isNotEmpty()) {

                    if (fullMarks <= 0) {

                        Toast.makeText(
                            this,
                            "$subjectName full marks must be greater than 0",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@checkStudentExists
                    }

                    if (subjectMarks > fullMarks) {

                        Toast.makeText(
                            this,
                            "$subjectName marks cannot exceed full marks",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@checkStudentExists
                    }

                    subjectList.add(
                        SubjectResultModel(
                            subjectName = subjectName,
                            fullMarks = fullMarks,
                            obtainedMarks = subjectMarks,
                            grade = subjectGrade
                        )
                    )
                }
            }

            if (subjectList.isEmpty()) {

                Toast.makeText(
                    this,
                    "Add at least one subject",
                    Toast.LENGTH_SHORT
                ).show()

                return@checkStudentExists
            }

            val resultModel =
                ResultModel(
                    studentRoll = actualRoll,
                    studentName = student.studentName,
                    studentClass = "Class $actualClass",
                    studentSection = actualSection,
                    examName = examName,
                    subjects = subjectList,
                    totalMarks = totalMarks,
                    obtainedMarks = obtainedMarks,
                    percentage = percentage,
                    grade = grade,
                    year = actualYear
                )

            viewModel.addResult(resultModel)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Result saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    clearForm()
                }
                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        it.message ?: "Failed to save result",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun checkStudentExists(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        studentYear: String,
        onResult: (DataSnapshot?) -> Unit
    ) {

        studentDatabase
            .child(studentClass)
            .child(studentSection)
            .child(studentRoll)
            .child(studentYear)
            .get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {
                    onResult(snapshot)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed to verify student",
                    Toast.LENGTH_SHORT
                ).show()

                onResult(null)
            }
    }

    private fun addSubjectRow() {

        val rowLayout =
            LinearLayout(this).apply {

                layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin = 12
                    }

                orientation =
                    LinearLayout.HORIZONTAL

                gravity =
                    Gravity.CENTER_VERTICAL
            }

        val subjectInput =
            TextInputLayout(this).apply {

                layoutParams =
                    LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2f
                    )

                hint = "Subject"

                setBoxBackgroundMode(
                    TextInputLayout.BOX_BACKGROUND_OUTLINE
                )
            }

        val subjectEditText =
            TextInputEditText(this).apply {

                inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_FLAG_CAP_WORDS

                maxLines = 1
            }

        subjectInput.addView(
            subjectEditText
        )

        val fullMarksInput =
            TextInputLayout(this).apply {

                layoutParams =
                    LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    ).apply {
                        marginStart = 8
                    }

                hint = "Full Marks"

                setBoxBackgroundMode(
                    TextInputLayout.BOX_BACKGROUND_OUTLINE
                )
            }

        val fullMarksEditText =
            TextInputEditText(this).apply {

                inputType =
                    InputType.TYPE_CLASS_NUMBER

                maxLines = 1
            }

        fullMarksInput.addView(
            fullMarksEditText
        )

        val obtainedMarksInput =
            TextInputLayout(this).apply {

                layoutParams =
                    LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    ).apply {
                        marginStart = 8
                    }

                hint = "Marks"

                setBoxBackgroundMode(
                    TextInputLayout.BOX_BACKGROUND_OUTLINE
                )
            }

        val obtainedMarksEditText =
            TextInputEditText(this).apply {

                inputType =
                    InputType.TYPE_CLASS_NUMBER

                maxLines = 1
            }

        obtainedMarksInput.addView(
            obtainedMarksEditText
        )

        val gradeInput =
            TextInputLayout(this).apply {

                layoutParams =
                    LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    ).apply {
                        marginStart = 8
                    }

                hint = "Grade"

                setBoxBackgroundMode(
                    TextInputLayout.BOX_BACKGROUND_OUTLINE
                )
            }

        val gradeEditText =
            TextInputEditText(this).apply {

                inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS

                maxLines = 1
            }

        gradeInput.addView(
            gradeEditText
        )

        rowLayout.addView(subjectInput)
        rowLayout.addView(fullMarksInput)
        rowLayout.addView(obtainedMarksInput)
        rowLayout.addView(gradeInput)

        binding.subjectContainer.addView(
            rowLayout
        )
    }

    private fun calculatePercentageAndGrade() {

        val totalMarks =
            binding.etTotalMarks.text
                .toString()
                .trim()
                .toDoubleOrNull()

        val obtainedMarks =
            binding.etObtainedMarks.text
                .toString()
                .trim()
                .toDoubleOrNull()

        if (
            totalMarks == null ||
            obtainedMarks == null ||
            totalMarks <= 0 ||
            obtainedMarks > totalMarks
        ) {

            binding.etPercentage.setText("")
            binding.etGrade.setText("")

            return
        }

        val percentage =
            (obtainedMarks / totalMarks) * 100

        val grade =
            when {
                percentage >= 90 -> "O"
                percentage >= 80 -> "A+"
                percentage >= 60 -> "A"
                percentage >= 45 -> "B+"
                percentage >= 35 -> "B"
                percentage >= 25 -> "C"
                else -> "D"
            }

        binding.etPercentage.setText(
            String.format(
                "%.2f",
                percentage
            )
        )

        binding.etGrade.setText(
            grade
        )
    }

    private fun clearForm() {

        binding.etStudentName.setText("")
        binding.etStudentRoll.setText("")
        binding.etClass.setText("")
        binding.etSection.setText("")
        binding.etExamName.setText("")
        binding.etTotalMarks.setText("")
        binding.etObtainedMarks.setText("")
        binding.etPercentage.setText("")
        binding.etGrade.setText("")
        binding.etYear.setText("")

        binding.subjectContainer.removeAllViews()

        addSubjectRow()
    }

    private fun removeSubjectRow() {

        val subjectCount =
            binding.subjectContainer.childCount

        if (subjectCount > 1) {

            binding.subjectContainer.removeViewAt(
                subjectCount - 1
            )

        } else {

            Toast.makeText(
                this,
                "At least one subject is required",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}