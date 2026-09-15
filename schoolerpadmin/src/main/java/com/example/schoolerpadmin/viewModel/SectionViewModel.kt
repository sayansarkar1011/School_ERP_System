package com.example.schoolerpadmin.viewModel

import androidx.lifecycle.ViewModel
import com.example.schoolerpadmin.model.SectionModel
import com.example.schoolerpadmin.repository.SectionRepository

class SectionViewModel : ViewModel() {

    private val sectionRepository =
        SectionRepository()

    fun addSection(
        sectionModel: SectionModel
    ) = sectionRepository.addSection(sectionModel)

    fun deleteSection(
        className: String,
        sectionName: String
    ) = sectionRepository.deleteSection(
        className,
        sectionName
    )

    fun getAllSections(
        className: String,
        onResult: (List<SectionModel>) -> Unit
    ) {
        sectionRepository.getAllSections(
            className,
            onResult
        )
    }
}