package com.example.schoolerpadmin.model

data class ResultModel(
    var studentName: String = "",
    var studentRoll: String = "",
    var studentClass: String = "",
    var studentSection: String = "",
    var examName: String = "",
    var subjects: List<SubjectResultModel> = emptyList(),
    var totalMarks: Int = 0,
    var obtainedMarks: Int = 0,
    var percentage: Double = 0.0,
    var grade: String = "",
    var year: String = ""
)
