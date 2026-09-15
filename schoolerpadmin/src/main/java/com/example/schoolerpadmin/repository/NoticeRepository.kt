package com.example.schoolerpadmin.repository

import com.example.schoolerpadmin.firebase.NoticeFirebaseManager
import com.example.schoolerpadmin.model.NoticeModel

class NoticeRepository {
    private val noticeFirebaseManager = NoticeFirebaseManager()

    fun addNotice(
        notice: NoticeModel
    ) = noticeFirebaseManager.addNotice(notice)

    fun updateNotice(
        notice: NoticeModel
    ) = noticeFirebaseManager.updateNotice(notice)

    fun deleteNotice(
        noticeId: String
    ) = noticeFirebaseManager.deleteNotice(noticeId)

    fun getNotice(
        noticeId: String,
        onResult: (NoticeModel?) -> Unit
    ) {
        noticeFirebaseManager.getNotice(noticeId, onResult)
    }

    fun getAllNotices(
        onResult: (List<NoticeModel>) -> Unit
    ) {
        noticeFirebaseManager.getAllNotices(onResult)
    }

    fun searchNotice(
        title: String,
        onResult: (List<NoticeModel>) -> Unit
    ) {
        noticeFirebaseManager.searchNotice(
            title,
            onResult
        )
    }
}