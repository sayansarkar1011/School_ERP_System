package com.example.schoolerpteacher.firebase

import com.example.schoolerpteacher.model.NoticeModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TeacherNoticeFirebaseManager {

    private val noticeReference = FirebaseDatabase.getInstance().getReference("notices")

    fun getTeacherNoticesRealtime(
        onResult: (List<NoticeModel>) -> Unit
    ) {

        noticeReference.addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(
                    snapshot: DataSnapshot
                ) {

                    val noticeList =
                        mutableListOf<NoticeModel>()

                    for (child in snapshot.children) {

                        val notice =
                            child.getValue(
                                NoticeModel::class.java
                            ) ?: continue

                        if (
                            notice.noticeType == "teacher" ||
                            notice.noticeType == "both"
                        ) {
                            noticeList.add(notice)
                        }
                    }

                    onResult(noticeList.reversed())
                }

                override fun onCancelled(
                    error: DatabaseError
                ) {
                    onResult(emptyList())
                }
            }
        )
    }
}