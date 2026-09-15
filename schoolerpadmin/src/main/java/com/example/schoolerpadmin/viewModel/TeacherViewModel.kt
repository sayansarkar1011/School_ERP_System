package com.example.schoolerpadmin.viewModel

import androidx.lifecycle.ViewModel
import com.example.schoolerpadmin.model.TeacherItem
import com.example.schoolerpadmin.model.TeacherModel
import com.example.schoolerpadmin.repository.TeacherRepository

class TeacherViewModel : ViewModel() {
    private val teacherRepository = TeacherRepository()

    fun addTeacher(
        teacher: TeacherModel
    )= teacherRepository.addTeacher(teacher)

    fun isTeacherExists(
        teacherPhone : String,
        onResult: (Boolean) -> Unit
    ) = teacherRepository.isTeacherExists(teacherPhone,onResult)

    fun getTeacher(
        teacherPhone : String,
        onResult: (TeacherModel?) -> Unit
    ) = teacherRepository.getTeacher(teacherPhone,onResult)

    fun updateTeacher (
        teacher: TeacherModel
    ) = teacherRepository.updateTeacher(teacher)

    fun deleteTeacher(
        teacherPhone : String
    ) = teacherRepository.deleteTeacher(teacherPhone)

    fun searchTeacher(
        teacherPhone: String,
        onResult: (TeacherItem?) -> Unit
    ) = teacherRepository.searchTeacher(teacherPhone,onResult)

    fun getAllTeachers(
        onResult: (List<TeacherItem>) -> Unit
    ) = teacherRepository.getAllTeachers(onResult)
}