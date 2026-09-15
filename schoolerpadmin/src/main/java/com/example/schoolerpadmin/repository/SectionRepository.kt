package com.example.schoolerpadmin.repository

import com.example.schoolerpadmin.firebase.SectionFirebaseManager
import com.example.schoolerpadmin.model.SectionModel

class SectionRepository {

    private val sectionFirebaseManager =
        SectionFirebaseManager()

    fun addSection(
        sectionModel: SectionModel
    ) = sectionFirebaseManager.addSection(sectionModel)

    fun deleteSection(
        className: String,
        sectionName: String
    ) = sectionFirebaseManager.deleteSection(
        className,
        sectionName
    )

    fun getAllSections(
        className: String,
        onResult: (List<SectionModel>) -> Unit
    ) {
        sectionFirebaseManager.getAllSections(
            className,
            onResult
        )
    }
}