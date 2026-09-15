package com.example.schoolerpadmin.firebase

import com.example.schoolerpadmin.model.StudentItem
import com.example.schoolerpadmin.model.StudentModel
import com.google.firebase.database.FirebaseDatabase

class StudentFirebaseManager {

    private val studentDatabase = FirebaseDatabase.getInstance()

    fun addStudent(
        student: StudentModel
    ) =
        studentDatabase
            .getReference("students")
            .child(student.studentClass)
            .child(student.studentSection)
            .child(student.studentRoll)
            .child(student.studentYear)
            .setValue(student)

    fun isStudentExists(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        studentYear: String,
        onResult: (Boolean) -> Unit
    ) {
        studentDatabase
            .getReference("students")
            .child(studentClass)
            .child(studentSection)
            .child(studentRoll)
            .child(studentYear)
            .get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.exists())
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun getAllStudents(
        onResult: (List<StudentItem>) -> Unit
    ) {
        studentDatabase
            .getReference("students")
            .get()
            .addOnSuccessListener { snapshot ->

                val studentList = mutableListOf<StudentItem>()

                for (classSnapshot in snapshot.children) {

                    for (sectionSnapshot in classSnapshot.children) {

                        for (studentSnapshot in sectionSnapshot.children) {

                            for (yearSnapshot in studentSnapshot.children) {

                                val student =
                                    yearSnapshot.getValue(
                                        StudentModel::class.java
                                    ) ?: continue

                                studentList.add(
                                    StudentItem(
                                        studentName = student.studentName,
                                        studentClass = student.studentClass,
                                        studentSection = student.studentSection,
                                        studentRoll = student.studentRoll,
                                        studentYear = student.studentYear
                                    )
                                )
                            }
                        }
                    }
                }

                onResult(studentList)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun deleteStudent(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        studentYear: String
    ) =
        studentDatabase
            .getReference("students")
            .child(studentClass)
            .child(studentSection)
            .child(studentRoll)
            .child(studentYear)
            .removeValue()

    fun getStudent(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        studentYear: String,
        onResult: (StudentModel?) -> Unit
    ) {
        studentDatabase
            .getReference("students")
            .child(studentClass)
            .child(studentSection)
            .child(studentRoll)
            .child(studentYear)
            .get()
            .addOnSuccessListener { snapshot ->

                onResult(
                    snapshot.getValue(StudentModel::class.java)
                )
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun updateStudent(
        student: StudentModel
    ) =
        studentDatabase
            .getReference("students")
            .child(student.studentClass)
            .child(student.studentSection)
            .child(student.studentRoll)
            .child(student.studentYear)
            .setValue(student)

    fun searchStudent(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        studentYear: String,
        onResult: (StudentItem?) -> Unit
    ) {
        studentDatabase
            .getReference("students")
            .child(studentClass)
            .child(studentSection)
            .child(studentRoll)
            .child(studentYear)
            .get()
            .addOnSuccessListener { snapshot ->

                val student =
                    snapshot.getValue(StudentModel::class.java)

                onResult(
                    student?.let { data ->
                        StudentItem(
                            studentName = data.studentName,
                            studentClass = data.studentClass,
                            studentSection = data.studentSection,
                            studentRoll = data.studentRoll,
                            studentYear = data.studentYear
                        )
                    }
                )
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun getStudentByGuardianPhone(
        guardianPhone: String,
        onResult: (StudentModel?) -> Unit
    ) {
        studentDatabase
            .getReference("students")
            .get()
            .addOnSuccessListener { snapshot ->

                var foundStudent: StudentModel? = null

                for (classSnapshot in snapshot.children) {

                    for (sectionSnapshot in classSnapshot.children) {

                        for (studentSnapshot in sectionSnapshot.children) {

                            for (yearSnapshot in studentSnapshot.children) {

                                val student =
                                    yearSnapshot.getValue(
                                        StudentModel::class.java
                                    ) ?: continue

                                if (
                                    student.studentGuardianPhone.trim() ==
                                    guardianPhone.trim()
                                ) {
                                    foundStudent = student
                                    break
                                }
                            }

                            if (foundStudent != null) {
                                break
                            }
                        }

                        if (foundStudent != null) {
                            break
                        }
                    }

                    if (foundStudent != null) {
                        break
                    }
                }

                onResult(foundStudent)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}