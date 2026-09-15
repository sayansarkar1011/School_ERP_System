package com.example.schoolerpadmin.firebase

import com.example.schoolerpadmin.model.AdminModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminFirebaseManager {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    fun signup(
        email: String,
        password: String,
        admin: AdminModel
    ) = auth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener {
            val uid = auth.currentUser?.uid ?: return@addOnSuccessListener

            val updateAdmin = admin.copy(
                adminUid = uid
            )
            database.getReference("admin")
                .child(uid)
                .setValue(updateAdmin)
        }

    fun login (email: String,
               password: String
    ) = auth.signInWithEmailAndPassword(email,password)

    fun forgotPassword (email: String,) = auth.sendPasswordResetEmail(email)

    fun logout(){
        auth.signOut()
    }

    fun isAdminExists(
        onResult: (Boolean) -> Unit
    ){
        database.getReference("admin")
            .get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.exists())
            }
            .addOnFailureListener {
                onResult(false)
            }
    }
    fun getAdmin() =
        database.getReference("admin")
            .child(auth.currentUser?.uid ?: "")
            .get()

}