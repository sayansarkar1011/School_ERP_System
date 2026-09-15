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
import com.example.schoolerpadmin.databinding.ActivityUpdateNoticeBinding
import com.example.schoolerpadmin.model.NoticeModel
import com.example.schoolerpadmin.viewModel.NoticeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UpdateNoticeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoticeBinding
    private lateinit var viewModel: NoticeViewModel

    private var oldNoticeId = ""
    private var oldNoticeType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[NoticeViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        oldNoticeId = intent.getStringExtra("noticeId") ?: ""

        loadDetails(oldNoticeId)

        binding.etNoticeDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnUpdate.setOnClickListener {
            updateNotice()
        }
    }

    private fun loadDetails(
        noticeId: String
    ) {

        viewModel.getNotice(noticeId) { notice ->

            notice ?: return@getNotice

            oldNoticeType = notice.noticeType

            binding.etNoticeTitle.setText(notice.title)
            binding.etNoticeDescription.setText(notice.description)
            binding.etNoticeDate.setText(notice.date)
        }
    }

    private fun updateNotice() {

        val title =
            binding.etNoticeTitle.text.toString().trim()

        val description =
            binding.etNoticeDescription.text.toString().trim()

        val date =
            binding.etNoticeDate.text.toString().trim()

        if (
            title.isEmpty() ||
            description.isEmpty() ||
            date.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Please complete the notice",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val notice = NoticeModel(
            noticeId = oldNoticeId,
            title = title,
            description = description,
            date = date,
            time = SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            ).format(Date()),
            noticeType = oldNoticeType
        )

        viewModel.updateNotice(notice)
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Notice updated successfully",
                    Toast.LENGTH_SHORT
                ).show()

                setResult(RESULT_OK)
                finish()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    it.message ?: "Failed to update notice",
                    Toast.LENGTH_SHORT
                ).show()
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
}