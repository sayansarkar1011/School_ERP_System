package com.example.schoolerpadmin.viewModel

import androidx.lifecycle.ViewModel
import com.example.schoolerpadmin.model.NoticeModel
import com.example.schoolerpadmin.repository.NoticeRepository

class NoticeViewModel : ViewModel() {
    private val noticeRepository = NoticeRepository()

    fun addNotice(
        notice: NoticeModel
    ) = noticeRepository.addNotice(notice)

    fun updateNotice(
        notice: NoticeModel
    ) = noticeRepository.updateNotice(notice)

    fun deleteNotice(
        noticeId: String
    ) = noticeRepository.deleteNotice(noticeId)

    fun getNotice(
        noticeId: String,
        onResult: (NoticeModel?) -> Unit
    ) {
        noticeRepository.getNotice(noticeId, onResult)
    }

    fun getAllNotices(
        onResult: (List<NoticeModel>) -> Unit
    ) {
        noticeRepository.getAllNotices(onResult)
    }

    fun searchNotice(
        title: String,
        onResult: (List<NoticeModel>) -> Unit
    ) {
        noticeRepository.searchNotice(
            title,
            onResult
        )
    }
}