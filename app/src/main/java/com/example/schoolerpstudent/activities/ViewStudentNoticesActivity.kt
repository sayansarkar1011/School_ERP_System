package com.example.schoolerpstudent.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schoolerpstudent.adapter.NoticeAdapter
import com.example.schoolerpstudent.databinding.ActivityViewStudentNoticesBinding
import com.example.schoolerpstudent.model.NoticeModel
import com.example.schoolerpstudent.viewmodel.NoticeViewModel

class ViewStudentNoticesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewStudentNoticesBinding
    private lateinit var viewModel: NoticeViewModel
    private lateinit var adapter: NoticeAdapter

    private var allNotices = listOf<NoticeModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewStudentNoticesBinding.inflate(layoutInflater)
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

    private fun setupRecyclerView() {

        adapter = NoticeAdapter()

        binding.rvNotices.layoutManager =
            LinearLayoutManager(this)

        binding.rvNotices.adapter = adapter
    }

    private fun loadNotices() {

        viewModel.getStudentNoticesRealtime { noticeList ->

            allNotices = noticeList

            adapter.updateList(
                noticeList
            )
        }
    }

    private fun searchNotices() {

        val query = binding.etSearchNotice
            .text
            .toString()
            .trim()

        if (query.isEmpty()) {

            adapter.updateList(allNotices)

            return
        }

        val filteredList = allNotices.filter {

            it.title.contains(
                query,
                ignoreCase = true
            )
        }

        adapter.updateList(filteredList)

        if (filteredList.isEmpty()) {

            Toast.makeText(
                this,
                "No notice found",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}