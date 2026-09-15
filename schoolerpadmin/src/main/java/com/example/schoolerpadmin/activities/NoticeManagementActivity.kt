package com.example.schoolerpadmin.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.schoolerpadmin.R
import com.example.schoolerpadmin.databinding.ActivityNoticeManagementBinding
import com.example.schoolerpadmin.model.NoticeModel
import com.example.schoolerpadmin.viewModel.NoticeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NoticeManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoticeManagementBinding
    private lateinit var viewModel: NoticeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[NoticeViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.etNoticeDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnStudents.setOnClickListener {
            createNotice("student")
        }

        binding.btnTeachers.setOnClickListener {
            createNotice("teacher")
        }

        binding.btnBoth.setOnClickListener {
            createNotice("both")
        }


    }

    private fun showDatePicker() {

        val calendar = Calendar.getInstance()

        DatePickerDialog(
            this,
            { _, year, month, day ->

                binding.etNoticeDate.setText(
                    "$day/${month + 1}/$year"
                )

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun createNotice(
        target : String
    ){
        val title = binding.etNoticeTitle.text.toString().trim()
        val description = binding.etNoticeDescription.text.toString().trim()
        val date = binding.etNoticeDate.text.toString().trim()

        // Validation...
        if (title.isEmpty() ||
            description.isEmpty() ||
            date.isEmpty())
        {
            Toast.makeText(this, "Please complete the notice", Toast.LENGTH_SHORT).show()
            return
        }
        // Set data into data class...
        val notice = NoticeModel(
            noticeId = "",
            title = title,
            description = description,
            date = date,
            time = SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            ).format(Date()),
            noticeType = target
        )

        // save to database through viewmodel....
        viewModel.addNotice(notice)
            .addOnSuccessListener {
                Toast.makeText(this, "Notice published successfully",
                    Toast.LENGTH_SHORT).show()
                binding.etNoticeTitle.text?.clear()
                binding.etNoticeDescription.text?.clear()
                binding.etNoticeDate.text?.clear()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message ?: "Failed to publish notice",
                    Toast.LENGTH_SHORT).show()
            }
    }
}