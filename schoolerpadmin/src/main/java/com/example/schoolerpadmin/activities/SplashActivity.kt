package com.example.schoolerpadmin.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolerpadmin.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val auth =
        FirebaseAuth.getInstance()

    private val adminDatabase =
        FirebaseDatabase.getInstance()
            .getReference("admin")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        Handler(Looper.getMainLooper())
            .postDelayed({

                checkLogin()

            }, 1000)
    }

    private fun checkLogin() {

        val user =
            auth.currentUser

        /*
         * Firebase Authentication-e kono user nei
         */
        if (user == null) {

            openLogin()

            return
        }

        /*
         * Authentication user ache.
         * Ebar check korbo Realtime Database-e
         * admin record ache kina.
         */
        adminDatabase
            .get()
            .addOnSuccessListener { snapshot ->

                var adminExists = false

                for (adminSnapshot in snapshot.children) {

                    val email =
                        adminSnapshot
                            .child("adminEmail")
                            .getValue(String::class.java)

                    /*
                     * Current logged-in user-er email
                     * database-er admin email-er sathe match korle
                     * admin exists.
                     */
                    if (
                        !email.isNullOrEmpty() &&
                        email == user.email
                    ) {

                        adminExists = true

                        break
                    }
                }

                if (adminExists) {

                    openDashboard()

                } else {

                    /*
                     * Authentication account ache,
                     * kintu admin database record nei.
                     *
                     * Tai logout kore Login screen-e pathabo.
                     */
                    auth.signOut()

                    openLogin()
                }
            }
            .addOnFailureListener {

                /*
                 * Database check fail hole safety-r jonno
                 * dashboard-e na giye login-e pathabo.
                 */
                auth.signOut()

                openLogin()
            }
    }

    private fun openDashboard() {

        startActivity(
            Intent(
                this,
                DashboardActivity::class.java
            )
        )

        finish()
    }

    private fun openLogin() {

        startActivity(
            Intent(
                this,
                LoginActivity::class.java
            )
        )

        finish()
    }
}