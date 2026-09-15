package com.example.schoolerpadmin.repository

import com.example.schoolerpadmin.firebase.AdminFirebaseManager
import com.example.schoolerpadmin.model.AdminModel

class AuthRepository {
    private val firebaseManager = AdminFirebaseManager()

    fun signup(
        email : String,
        password : String,
        admin : AdminModel
    ) = firebaseManager.signup(email,password,admin)

    fun login(
        email: String,password: String
    ) = firebaseManager.login(email,password)

    fun forgotPassword(
        email: String
    ) = firebaseManager.forgotPassword(
        email,
    )

    fun logout() = firebaseManager.logout()

    fun isAdminExists(onResult: (Boolean)-> Unit) =
        firebaseManager.isAdminExists(onResult)

    fun getAdmin() = firebaseManager.getAdmin()


}