package com.example.schoolerpadmin.firebase

import com.example.schoolerpadmin.model.ClassModel
import com.google.firebase.database.FirebaseDatabase

class ClassFirebaseManager {

    private val classDatabase =
        FirebaseDatabase.getInstance().getReference("classes")

    fun addClass(
        classModel: ClassModel
    ) = classDatabase
        .child(classModel.className)
        .setValue(classModel)

    fun getAllClasses(
        onResult: (List<ClassModel>) -> Unit
    ) {
        classDatabase.get()
            .addOnSuccessListener { snapshot ->

                val list = snapshot.children
                    .mapNotNull { child ->
                        child.getValue(ClassModel::class.java)
                    }
                    .sortedBy { classModel ->
                        classModel.className
                            .filter { char -> char.isDigit() }
                            .toIntOrNull() ?: 0
                    }

                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun deleteClass(
        className: String
    ) = classDatabase
        .child(className)
        .removeValue()
}