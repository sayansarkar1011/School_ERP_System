package com.example.schoolerpadmin.repository

import com.example.schoolerpadmin.firebase.TeacherFirebaseManager
import com.example.schoolerpadmin.model.StudentItem
import com.example.schoolerpadmin.model.TeacherItem
import com.example.schoolerpadmin.model.TeacherModel

class TeacherRepository {

    private val teacherFirebaseManager = TeacherFirebaseManager()

    fun addTeacher(
        teacher : TeacherModel
    ) = teacherFirebaseManager.addTeacher(teacher)

    fun isTeacherExists (
        teacherPhone : String,
        onResult: (Boolean) -> Unit
    )= teacherFirebaseManager.isTeacherExists(
        teacherPhone,
        onResult
    )

    fun getTeacher (
        teacherPhone : String,
        onResult: (TeacherModel?) -> Unit
    ) = teacherFirebaseManager.getTeacher(teacherPhone,onResult)

    fun updateTeacher(
        teacher: TeacherModel
    ) = teacherFirebaseManager.updateTeacher(teacher)

    fun deleteTeacher(
        teacherPhone : String
    ) = teacherFirebaseManager.deleteTeacher(teacherPhone)

    fun searchTeacher(
        teacherPhone : String,
        onResult: (TeacherItem?) -> Unit
    ) = teacherFirebaseManager.searchTeacher(teacherPhone,onResult)

    fun getAllTeachers(
        onResult: (List<TeacherItem>) -> Unit
    ) = teacherFirebaseManager.getAllTeachers(onResult)
}