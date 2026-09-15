package com.example.schoolerpadmin.activities

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.schoolerpadmin.databinding.ActivityUpdateResultsBinding
import com.example.schoolerpadmin.model.ResultModel
import com.example.schoolerpadmin.model.SubjectResultModel
import com.example.schoolerpadmin.viewModel.ResultViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class UpdateResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateResultsBinding
    private lateinit var viewModel: ResultViewModel

    private var oldClass = ""
    private var oldSection = ""
    private var oldRoll = ""
    private var oldYear = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityUpdateResultsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this)[ResultViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        oldClass =
            intent.getStringExtra("studentClass") ?: ""

        oldSection =
            intent.getStringExtra("studentSection") ?: ""

        oldRoll =
            intent.getStringExtra("studentRoll") ?: ""

        oldYear =
            intent.getStringExtra("studentYear") ?: ""

        if (
            oldClass.isEmpty() ||
            oldSection.isEmpty() ||
            oldRoll.isEmpty() ||
            oldYear.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Invalid result details",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        loadResultDetails()

        binding.btnUpdate.setOnClickListener {
            updateResult()
        }

        binding.btnAddSubject.setOnClickListener {
            addSubjectRow()
        }

        binding.btnRemoveSubject.setOnClickListener {
            removeSubjectRow()
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

    private fun loadResultDetails() {

        viewModel.getResult(
            oldClass,
            oldSection,
            oldRoll,
            oldYear
        ) { result ->

            if (result == null) {

                Toast.makeText(
                    this,
                    "Result not found",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

                return@getResult
            }

            binding.etStudentName.setText(
                result.studentName
            )

            binding.etStudentRoll.setText(
                result.studentRoll
            )

            binding.etClass.setText(
                result.studentClass
                    .replace("Class ", "", true)
                    .trim()
            )

            binding.etSection.setText(
                result.studentSection
            )

            binding.etExamName.setText(
                result.examName
            )

            binding.etYear.setText(
                result.year
            )

            binding.etTotalMarks.setText(
                result.totalMarks.toString()
            )

            binding.etObtainedMarks.setText(
                result.obtainedMarks.toString()
            )

            binding.etPercentage.setText(
                String.format(
                    "%.2f",
                    result.percentage
                )
            )

            binding.etGrade.setText(
                result.grade
            )

            binding.subjectContainer.removeAllViews()

            if (result.subjects.isEmpty()) {

                addSubjectRow()

            } else {

                result.subjects.forEach { subject ->
                    addSubjectRow(subject)
                }
            }
        }
    }

    private fun updateResult() {

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

        val year =
            binding.etYear.text
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

            val obtainedMarks =
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

                    return
                }

                if (obtainedMarks > fullMarks) {

                    Toast.makeText(
                        this,
                        "$subjectName marks cannot exceed full marks",
                        Toast.LENGTH_SHORT
                    ).show()

                    return
                }

                subjectList.add(
                    SubjectResultModel(
                        subjectName = subjectName,
                        fullMarks = fullMarks,
                        obtainedMarks = obtainedMarks,
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

            return
        }

        val resultModel =
            ResultModel(
                studentRoll = studentRoll,
                studentName = studentName,
                studentClass = finalClass,
                studentSection = studentSection,
                examName = examName,
                subjects = subjectList,
                totalMarks = totalMarks,
                obtainedMarks = obtainedMarks,
                percentage = percentage,
                grade = grade,
                year = year
            )

        viewModel.updateResult(resultModel)
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Result updated successfully",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    it.message ?: "Failed to update result",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun addSubjectRow(
        subject: SubjectResultModel? = null
    ) {

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

                setText(
                    subject?.subjectName ?: ""
                )
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

                setText(
                    if (subject != null)
                        subject.fullMarks.toString()
                    else
                        ""
                )
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

                setText(
                    if (subject != null)
                        subject.obtainedMarks.toString()
                    else
                        ""
                )
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

                setText(
                    subject?.grade ?: ""
                )
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