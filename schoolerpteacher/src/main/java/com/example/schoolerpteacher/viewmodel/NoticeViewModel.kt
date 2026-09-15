package com.example.schoolerpteacher.viewmodel

import androidx.lifecycle.ViewModel
import com.example.schoolerpteacher.model.NoticeModel
import com.example.schoolerpteacher.repository.NoticeRepository

class NoticeViewModel : ViewModel() {
    private val repository = NoticeRepository()
    fun getTeacherNoticesRealtime(
        onResult: (List<NoticeModel>) -> Unit
    ) {
        repository.getTeacherNoticesRealtime(onResult)
    }
}