package com.example.schoolerpadmin.viewModel

import androidx.lifecycle.ViewModel
import com.example.schoolerpadmin.model.AdminModel
import com.example.schoolerpadmin.repository.AuthRepository

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    fun signup(
        email : String,
        password: String,
        admin : AdminModel
    )= repository.signup(email,password,admin)

    fun login(
        email: String,password: String
    ) = repository.login(email,password)

    fun forgotPassword(
        email: String
    ) = repository.forgotPassword(email)

    fun logout() = repository.logout()

    fun isAdminExists(
        onResult: (Boolean)-> Unit
    ){
        repository.isAdminExists (onResult)
    }
    fun getAdmin() = repository.getAdmin()
}