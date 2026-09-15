package com.example.schoolerpadmin.firebase

import com.example.schoolerpadmin.model.TeacherItem
import com.example.schoolerpadmin.model.TeacherModel
import com.google.firebase.database.FirebaseDatabase

class TeacherFirebaseManager {

    private val teacherDatabase = FirebaseDatabase.getInstance()

    fun addTeacher(
        teacher: TeacherModel
    ) = teacherDatabase.getReference("teachers")
        .child(teacher.teacherPhone)
        .setValue(teacher)

    fun isTeacherExists(
        teacherPhone: String,
        onResult: (Boolean) -> Unit
    ) {
        teacherDatabase.getReference("teachers")
            .child(teacherPhone)
            .get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.exists())
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun getTeacher(
        teacherPhone: String,
        onResult: (TeacherModel?) -> Unit
    ) {
        teacherDatabase.getReference("teachers")
            .child(teacherPhone)
            .get()
            .addOnSuccessListener { snapshot ->

                onResult(
                    snapshot.getValue(TeacherModel::class.java)
                )
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun deleteTeacher(
        teacherPhone: String
    ) = teacherDatabase.getReference("teachers")
        .child(teacherPhone)
        .removeValue()

    fun updateTeacher(
        teacher: TeacherModel
    ) = teacherDatabase.getReference("teachers")
        .child(teacher.teacherPhone)
        .setValue(teacher)

    fun searchTeacher(
        teacherPhone: String,
        onResult: (TeacherItem?) -> Unit
    ) {
        teacherDatabase.getReference("teachers")
            .child(teacherPhone)
            .get()
            .addOnSuccessListener { snapshot ->

                val teacher =
                    snapshot.getValue(TeacherModel::class.java)

                onResult(
                    teacher?.let {
                        TeacherItem(
                            teacherName = it.teacherName,
                            teacherEmail = it.teacherEmail,
                            teacherPhone = it.teacherPhone,
                            teacherSubject = it.teacherSubject
                        )
                    }
                )
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun getAllTeachers(
        onResult: (List<TeacherItem>) -> Unit
    ) {
        teacherDatabase.getReference("teachers")
            .get()
            .addOnSuccessListener { snapshot ->

                val teacherList = mutableListOf<TeacherItem>()

                for (teacherSnapshot in snapshot.children) {

                    val teacher =
                        teacherSnapshot.getValue(TeacherModel::class.java)
                            ?: continue

                    teacherList.add(
                        TeacherItem(
                            teacherName = teacher.teacherName,
                            teacherSubject = teacher.teacherSubject,
                            teacherPhone = teacher.teacherPhone,
                            teacherEmail = teacher.teacherEmail
                        )
                    )
                }

                onResult(teacherList)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}