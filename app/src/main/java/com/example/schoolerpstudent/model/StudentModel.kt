package com.example.schoolerpstudent.model

data class StudentModel(

    var studentName: String = "",
    var studentDOB: String = "",
    var studentRoll: String = "",
    var studentClass: String = "",
    var studentSection: String = "",
    var studentAddress: String = "",

    var studentYear: String = "",

    var studentFatherName: String = "",
    var studentFatherPhone: String = "",
    var studentFatherOccupation: String = "",
    var studentFatherIncome: Int = 0,

    var studentMotherName: String = "",
    var studentMotherPhone: String = "",
    var studentMotherOccupation: String = "",
    var studentMotherIncome: Int = 0,

    var studentGuardianName: String = "",
    var studentGuardianPhone: String = "",
    var studentGuardianEmail: String = ""
)