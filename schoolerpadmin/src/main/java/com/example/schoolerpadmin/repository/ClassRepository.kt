package com.example.schoolerpadmin.repository

import com.example.schoolerpadmin.firebase.ClassFirebaseManager
import com.example.schoolerpadmin.model.ClassModel

class ClassRepository {

    private val classFirebaseManager =
        ClassFirebaseManager()

    fun addClass(
        classModel: ClassModel
    ) = classFirebaseManager.addClass(classModel)

    fun deleteClass(
        className: String
    ) = classFirebaseManager.deleteClass(className)

    fun getAllClasses(
        onResult: (List<ClassModel>) -> Unit
    ) {
        classFirebaseManager.getAllClasses(
            onResult
        )
    }
}