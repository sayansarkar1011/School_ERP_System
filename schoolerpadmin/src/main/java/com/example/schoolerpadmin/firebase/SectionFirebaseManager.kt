package com.example.schoolerpadmin.firebase

import com.example.schoolerpadmin.model.SectionModel
import com.google.firebase.database.FirebaseDatabase

class SectionFirebaseManager {

    private val sectionDatabase =
        FirebaseDatabase.getInstance()
            .getReference("sections")

    fun addSection(
        sectionModel: SectionModel
    ) = sectionDatabase
        .child(sectionModel.className)
        .child(sectionModel.sectionName)
        .setValue(sectionModel)

    fun getAllSections(
        className: String,
        onResult: (List<SectionModel>) -> Unit
    ) {
        sectionDatabase
            .child(className)
            .get()
            .addOnSuccessListener { snapshot ->

                val list = snapshot.children
                    .mapNotNull { child ->
                        child.getValue(SectionModel::class.java)
                    }
                    .sortedBy { sectionModel ->
                        sectionModel.sectionName
                            .filter { char -> char.isDigit() }
                            .toIntOrNull() ?: 0
                    }

                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun deleteSection(
        className: String,
        sectionName: String
    ) = sectionDatabase
        .child(className)
        .child(sectionName)
        .removeValue()
}