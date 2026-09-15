package com.example.schoolerpadmin.firebase

import com.example.schoolerpadmin.model.NoticeModel
import com.google.firebase.database.FirebaseDatabase

class NoticeFirebaseManager {
    private val noticeDatabase = FirebaseDatabase.getInstance()
    private val noticeReference = noticeDatabase.getReference("notices")


    fun addNotice(
        notice: NoticeModel
    ) = run {

        val noticeId = noticeReference.push().key ?: ""

        notice.noticeId = noticeId

        noticeReference
            .child(noticeId)
            .setValue(notice)
    }

    fun getAllNotices(
        onResult: (List<NoticeModel>) -> Unit
    ) {
        noticeReference.get()
            .addOnSuccessListener { snapshot ->

                val noticeList = mutableListOf<NoticeModel>()
                for (noticeSnapshot in snapshot.children) {

                    val notice = noticeSnapshot.getValue(NoticeModel::class.java)
                        ?: continue

                    noticeList.add(notice)
                }

                onResult(noticeList.reversed())

            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getNotice(
        noticeId: String,
        onResult: (NoticeModel?) -> Unit
    ){
        noticeReference.child(noticeId)
            .get()
            .addOnSuccessListener { snapshot ->
                onResult(
                    snapshot.getValue(
                        NoticeModel::class.java
                    )
                )
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun updateNotice(
        notice: NoticeModel
    ) = noticeReference
        .child(notice.noticeId)
        .setValue(notice)

    fun deleteNotice(
        noticeId: String
    )= noticeReference
        .child(noticeId)
        .removeValue()

    fun searchNotice(
        title: String,
        onResult: (List<NoticeModel>) -> Unit
    ) {

        noticeReference.get()
            .addOnSuccessListener { snapshot ->

                val noticeList = mutableListOf<NoticeModel>()

                for (noticeSnapshot in snapshot.children) {

                    val notice =
                        noticeSnapshot.getValue(
                            NoticeModel::class.java
                        ) ?: continue

                    if (
                        notice.title.contains(
                            title,
                            ignoreCase = true
                        )
                    ) {
                        noticeList.add(notice)
                    }
                }

                onResult(noticeList.reversed())
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}