package com.example.schoolerpstudent.viewmodel

import androidx.lifecycle.ViewModel
import com.example.schoolerpstudent.repository.NoticeRepository
import com.example.schoolerpstudent.model.NoticeModel

class NoticeViewModel : ViewModel() {

    private val repository =
        NoticeRepository()

    fun getStudentNoticesRealtime(
        onResult: (List<NoticeModel>) -> Unit
    ) {
        repository.getStudentNoticesRealtime(onResult)
    }
}