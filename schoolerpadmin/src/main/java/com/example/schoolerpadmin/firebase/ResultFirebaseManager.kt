package com.example.schoolerpadmin.firebase

import android.util.Log
import com.example.schoolerpadmin.model.ResultItem
import com.example.schoolerpadmin.model.ResultModel
import com.example.schoolerpadmin.model.StudentItem
import com.google.firebase.database.FirebaseDatabase

class ResultFirebaseManager {

    private val resultDatabase = FirebaseDatabase.getInstance()

    fun addResult(
        resultModel: ResultModel
    ) : com.google.android.gms.tasks.Task<Void> {

        Log.d(
            "RESULT_PATH",
            "results/${resultModel.studentClass}/${resultModel.studentSection}/${resultModel.studentRoll}/${resultModel.year}"
        )

        return resultDatabase.getReference("results")
            .child(resultModel.studentClass)
            .child(resultModel.studentSection)
            .child(resultModel.studentRoll)
            .child(resultModel.year)
            .setValue(resultModel)
    }

    fun updateResult(
        resultModel: ResultModel
    ) = resultDatabase.getReference("results")
        .child(resultModel.studentClass)
        .child(resultModel.studentSection)
        .child(resultModel.studentRoll)
        .child(resultModel.year)
        .setValue(resultModel)

    fun deleteResult(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        year: String
    ) = resultDatabase.getReference("results")
        .child(studentClass)
        .child(studentSection)
        .child(studentRoll)
        .child(year)
        .removeValue()

    fun getResult(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        year: String,
        onResult: (ResultModel?) -> Unit
    ) {
        resultDatabase.getReference("results")
            .child(studentClass)
            .child(studentSection)
            .child(studentRoll)
            .child(year)
            .get()
            .addOnSuccessListener { snapshot ->
                onResult(
                    snapshot.getValue(ResultModel::class.java)
                )
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun searchResult(
        studentClass: String,
        studentSection: String,
        studentRoll: String,
        year: String,
        onResult: (ResultItem?) -> Unit
    ){
        resultDatabase.getReference("results")
            .child(studentClass)
            .child(studentSection)
            .child(studentRoll)
            .child(year)
            .get()
            .addOnSuccessListener { snapshot ->
                val result =
                    snapshot.getValue(ResultModel::class.java)

                onResult(
                    result?.let { data ->
                        ResultItem(
                            studentName = data.studentName,
                            studentClass = data.studentClass,
                            studentRoll = data.studentRoll,
                            studentSection = data.studentSection,
                            year = data.year
                        )
                    }
                )
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun getAllResults(
        onResult: (List<ResultItem>) -> Unit
    ) {
        resultDatabase.getReference("results")
            .get()
            .addOnSuccessListener { snapshot ->

                val resultList = mutableListOf<ResultItem>()

                for (classSnapshot in snapshot.children) {

                    for (sectionSnapshot in classSnapshot.children) {

                        for (studentSnapshot in sectionSnapshot.children) {

                            for (yearSnapshot in studentSnapshot.children) {

                                val result =
                                    yearSnapshot.getValue(ResultModel::class.java)
                                        ?: continue

                                resultList.add(
                                    ResultItem(
                                        studentName = result.studentName,
                                        studentRoll = result.studentRoll,
                                        studentClass = result.studentClass,
                                        studentSection = result.studentSection,
                                        year = result.year
                                    )
                                )
                            }
                        }
                    }
                }

                onResult(resultList)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}