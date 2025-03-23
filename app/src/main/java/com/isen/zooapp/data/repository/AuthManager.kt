package com.isen.zooapp.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.isen.zooapp.data.models.User

object AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUp(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    val user = User(name = "Utilisateur", email = email, role = "user")
                    Database.addUser(uid, user) { success ->
                        if (!success) {
                            Log.e(
                                "AuthManager",
                                "Erreur lors de l'ajout d'un utilisateur dans la base de données"
                            )
                        }
                    }
                }
                onResult(true, null)
            } else {
                onResult(false, task.exception?.message)
            }
        }
    }

    fun signIn(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, null)
            } else {
                onResult(false, task.exception?.message)
            }
        }
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    fun signOut() {
        auth.signOut()
    }
}