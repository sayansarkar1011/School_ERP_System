package com.example.schoolerpadmin.model


data class StudentModel(

    val studentName: String = "",
    val studentDOB: String = "",

    val studentRoll: String = "",
    val studentClass: String = "",
    val studentSection: String = "",

    val studentAddress: String = "",

    val studentYear : String = "",

    val studentFatherName: String = "",
    val studentMotherName: String = "",
    val studentFatherIncome: Int = 0,
    val studentMotherIncome: Int = 0,
    val studentGuardianName: String = "",
    val studentGuardianPhone: String = "",
    val studentFatherOccupation: String = "",
    val studentMotherOccupation: String = "",
    val studentFatherPhone: String = "",
    val studentMotherPhone: String = "",
    val studentGuardianEmail: String = ""


)
