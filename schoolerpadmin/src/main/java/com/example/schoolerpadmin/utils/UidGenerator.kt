package com.example.schoolerpadmin.utils

object UidGenerator {

    fun generateSchoolUid(): String {
        return "SCH${System.currentTimeMillis()}"
    }
}