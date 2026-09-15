package com.example.schoolerpadmin.viewModel

import androidx.lifecycle.ViewModel
import com.example.schoolerpadmin.model.StudentItem
import com.example.schoolerpadmin.model.StudentModel
import com.example.schoolerpadmin.repository.StudentRepository

class StudentViewModel : ViewModel() {
    private val studentRepository = StudentRepository()

    fun addStudent(
        student: StudentModel
    ) = studentRepository.addStudent(
        student
    )

    fun isStudentExists(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        studentYear: String,
        onResult: (Boolean) -> Unit
    ) {
        studentRepository.isStudentExists(
            studentClass,
            studentSection,
            studentRoll,
            studentYear,
            onResult
        )
    }

    fun getAllStudents(
        onResult: (List<StudentItem>) -> Unit
    ) {
        studentRepository.getAllStudents(onResult)
    }

    fun deleteStudent(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        studentYear: String
    ) = studentRepository.deleteStudent(
        studentClass,
        studentSection,
        studentRoll,
        studentYear
    )

    fun getStudent(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        studentYear: String,
        onResult: (StudentModel?) -> Unit
    ) {
        studentRepository.getStudent(
            studentClass,
            studentSection,
            studentRoll,
            studentYear,
            onResult
        )
    }

    fun updateStudent(
        student: StudentModel
    ) = studentRepository.updateStudent(student)

    fun searchStudent(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        studentYear: String,
        onResult: (StudentItem?) -> Unit
    ) {
        studentRepository.searchStudent(
            studentClass,
            studentSection,
            studentRoll,
            studentYear,
            onResult
        )
    }

    fun getStudentByGuardianPhone(
        guardianPhone: String,
        onResult: (StudentModel?) -> Unit
    ) {
        studentRepository.getStudentByGuardianPhone(
            guardianPhone,
            onResult
        )
    }


}