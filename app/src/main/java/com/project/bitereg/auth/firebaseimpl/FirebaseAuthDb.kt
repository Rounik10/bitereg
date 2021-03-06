package com.project.bitereg.auth.firebaseimpl

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.project.bitereg.auth.Authenticator
import com.project.bitereg.models.User
import kotlinx.coroutines.tasks.await

class FirebaseAuthDb(context: Context) : Authenticator {

    init {
        FirebaseApp.initializeApp(context)
    }

    override suspend fun createUser(name: String, email: String, password: String): AuthResponse {
        return try {
            val response = Firebase.auth
                .createUserWithEmailAndPassword(email, password)
                .await()
            if (response.user == null) AuthResponse.Failure(Exception("Something went wrong"))
            AuthResponse.Success(User(response.user!!.uid, name, email))
        } catch (e: Exception) {
            e.printStackTrace()
            AuthResponse.Failure(e)
        }
    }

    override suspend fun loginUser(email: String, password: String): AuthResponse {
        return try {
            val response = Firebase.auth
                .signInWithEmailAndPassword(email, password)
                .await()
            if (response.user == null) {
                return AuthResponse.Failure(Exception("Something went wrong"))
            }
            AuthResponse.Success(User(id = response.user!!.uid))
        } catch (e: Exception) {
            e.printStackTrace()
            AuthResponse.Failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        val id = Firebase.auth.currentUser?.uid ?: return null
        return FirebaseFirestore.getInstance().document("users/$id").get().await()
            .toObject(User::class.java)
    }

    override fun isUserLoggedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override suspend fun logoutUser(): Boolean {
        if (Firebase.auth.currentUser == null) return true
        Firebase.auth.signOut()
        return true
    }
}