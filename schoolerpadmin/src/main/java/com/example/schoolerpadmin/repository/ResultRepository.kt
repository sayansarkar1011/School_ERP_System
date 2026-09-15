package com.example.schoolerpadmin.repository

import com.example.schoolerpadmin.firebase.ResultFirebaseManager
import com.example.schoolerpadmin.model.ResultItem
import com.example.schoolerpadmin.model.ResultModel
import com.example.schoolerpadmin.model.StudentItem

class ResultRepository {

    private val resultFirebaseManager = ResultFirebaseManager()

    fun addResult(
        resultModel: ResultModel
    ) = resultFirebaseManager.addResult(resultModel)

    fun updateResult(
        resultModel: ResultModel
    ) = resultFirebaseManager.updateResult(resultModel)

    fun deleteResult(
        studentClass : String,
        studentSection : String,
        studentRoll : String,
        year : String
    ) = resultFirebaseManager.deleteResult(
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
        resultFirebaseManager.getResult(
            studentClass,
            studentSection,
            studentRoll,
            year,
            onResult
        )
    }

    fun searchResults (
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        year: String,
        onResult: (ResultItem?) -> Unit
    ){
        resultFirebaseManager.searchResult(
            studentClass,
            studentSection,
            studentRoll,
            year,
            onResult
        )
    }

    fun getAllResults(
        onResult: (List<ResultItem>) -> Unit
    ) {
        resultFirebaseManager.getAllResults(onResult)
    }
}