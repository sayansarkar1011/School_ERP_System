package com.example.schoolerpadmin.model

data class TeacherModel(

    var teacherName: String = "",
    var teacherPhone: String = "",
    var teacherEmail: String = "",

    var teacherGender: String = "",
    var teacherDob: String = "",

    var teacherSubject: String = "",

    var teacherQualification: String = "",

    var teacherAddress: String = "",

    var joiningDate: String = "",

    var salary: Int = 0
)
