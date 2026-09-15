package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schoolerpadmin.adapter.NoticeAdapter
import com.example.schoolerpadmin.databinding.ActivityViewAllNoticesBinding
import com.example.schoolerpadmin.viewModel.NoticeViewModel

class ViewAllNoticesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewAllNoticesBinding
    private lateinit var viewModel: NoticeViewModel
    private lateinit var adapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewAllNoticesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[NoticeViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        setupRecyclerView()
        loadNotices()

        binding.btnSearch.setOnClickListener {
            searchNotices()
        }
    }

    private fun searchNotices() {

        val title =
            binding.etSearchNotice.text
                .toString()
                .trim()

        if (title.isEmpty()) {

            loadNotices()

            Toast.makeText(
                this,
                "Showing all notices",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        viewModel.searchNotice(title) { noticeList ->

            if (noticeList.isNotEmpty()) {

                adapter.updateList(noticeList)

                Toast.makeText(
                    this,
                    "Found ${noticeList.size} notice(s)",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                adapter.updateList(emptyList())

                Toast.makeText(
                    this,
                    "Notice not found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadNotices() {

        viewModel.getAllNotices { noticeList ->
            adapter.updateList(noticeList)
        }
    }

    private fun setupRecyclerView() {

        adapter = NoticeAdapter(

            onUpdateClick = { notice ->

                val intent = Intent(
                    this,
                    UpdateNoticeActivity::class.java
                )

                intent.putExtra(
                    "noticeId",
                    notice.noticeId
                )

                startActivityForResult(
                    intent,
                    100
                )
            },

            onDeleteClick = { notice ->

                AlertDialog.Builder(this)
                    .setTitle("Delete Notice")
                    .setMessage(
                        "Are you sure you want to delete this notice?"
                    )
                    .setPositiveButton("Delete") { _, _ ->

                        viewModel.deleteNotice(
                            notice.noticeId
                        )
                            .addOnSuccessListener {

                                Toast.makeText(
                                    this,
                                    "Notice deleted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                loadNotices()
                            }
                            .addOnFailureListener {

                                Toast.makeText(
                                    this,
                                    it.message ?: "Delete failed",
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

        binding.rvNotices.layoutManager =
            LinearLayoutManager(this)

        binding.rvNotices.adapter =
            adapter
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (
            requestCode == 100 &&
            resultCode == RESULT_OK
        ) {
            loadNotices()
        }
    }
}