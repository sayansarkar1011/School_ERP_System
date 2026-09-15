package com.example.schoolerpteacher.repository

import com.example.schoolerpteacher.firebase.TeacherNoticeFirebaseManager
import com.example.schoolerpteacher.model.NoticeModel

class NoticeRepository {

    private  val firebaseManager = TeacherNoticeFirebaseManager()

    fun getTeacherNoticesRealtime(
        onResult: (List<NoticeModel>) -> Unit
    ){
        firebaseManager.getTeacherNoticesRealtime(onResult)
    }
}