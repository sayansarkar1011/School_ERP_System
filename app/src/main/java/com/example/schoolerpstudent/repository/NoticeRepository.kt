package com.example.schoolerpstudent.repository

import com.example.schoolerpstudent.firebase.NoticeFirebaseManager
import com.example.schoolerpstudent.model.NoticeModel

class NoticeRepository {

    private val firebase =
        NoticeFirebaseManager()

    fun getStudentNoticesRealtime(
        onResult: (List<NoticeModel>) -> Unit
    ) {
        firebase.getStudentNoticesRealtime(onResult)
    }
}