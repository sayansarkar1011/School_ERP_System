package com.example.schoolerpadmin.repository

import com.example.schoolerpadmin.firebase.StudentFirebaseManager
import com.example.schoolerpadmin.model.StudentItem
import com.example.schoolerpadmin.model.StudentModel

class StudentRepository {
    private val studentFirebaseManager = StudentFirebaseManager()

    fun addStudent(
        student: StudentModel
    ) = studentFirebaseManager.addStudent(
        student
    )

    fun isStudentExists(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        studentYear : String,
        onResult: (Boolean) -> Unit
    ) =
        studentFirebaseManager.isStudentExists(
            studentClass,
            studentSection,
            studentRoll,
            studentYear,
            onResult
        )

    fun getAllStudents(
        onResult: (List<StudentItem>) -> Unit
    ) {
        studentFirebaseManager.getAllStudents(onResult)
    }

    fun deleteStudent(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        studentYear: String
    )= studentFirebaseManager.deleteStudent(
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
    ){
        studentFirebaseManager.getStudent(
            studentClass,
            studentSection,
            studentRoll,
            studentYear,
            onResult
        )
    }

    fun updateStudent(
        student : StudentModel
    ) = studentFirebaseManager.updateStudent(student)

    fun searchStudent (
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        studentYear: String,
        onResult: (StudentItem?) -> Unit
    ){
        studentFirebaseManager.searchStudent(
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
        studentFirebaseManager.getStudentByGuardianPhone(
            guardianPhone,
            onResult
        )
    }


}