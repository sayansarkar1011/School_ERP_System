package com.example.schoolerpadmin.viewModel

import androidx.lifecycle.ViewModel
import com.example.schoolerpadmin.model.ResultItem
import com.example.schoolerpadmin.model.ResultModel
import com.example.schoolerpadmin.repository.ResultRepository

class ResultViewModel : ViewModel() {
    private val resultRepository = ResultRepository()

    fun addResult(
        resultModel: ResultModel
    ) = resultRepository.addResult(resultModel)

    fun updateResult(
        resultModel: ResultModel
    ) = resultRepository.updateResult(resultModel)

    fun deleteResult(
        studentClass : String,
        studentSection : String,
        studentRoll : String,
        year: String
    ) = resultRepository.deleteResult(
        studentClass,
        studentSection,
        studentRoll,
        year
    )

    fun getResult(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        year: String,
        onResult: (ResultModel?) -> Unit
    ) {
        resultRepository.getResult(
            studentClass,
            studentSection,
            studentRoll,
            year,
            onResult
        )
    }

    fun getAllResults(
        onResult: (List<ResultItem>) -> Unit
    ){
        resultRepository.getAllResults(onResult)
    }

    fun searchResults (
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        year: String,
        onResult: (ResultItem?) -> Unit
    ){
        resultRepository.searchResults(
            studentClass,
            studentSection,
            studentRoll,
            year,
            onResult
        )
    }
}