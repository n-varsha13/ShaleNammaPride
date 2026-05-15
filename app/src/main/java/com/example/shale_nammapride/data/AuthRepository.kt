package com.example.shale_nammapride.data

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    fun loginUser(email: String, password: String, callback: (String) -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            callback("Please fill all fields")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { callback("success") }
            .addOnFailureListener { callback(it.message ?: "Login Failed") }
    }

    fun logoutUser() {
        auth.signOut()
    }
}
