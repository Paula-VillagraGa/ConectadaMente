package com.example.conectadamente.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthUserRepository {

    private val auth = FirebaseAuth.getInstance()

    // Registra un nuevo usuario
    fun registerUser(email: String, password: String, onComplete: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    onComplete(true, "Registro exitoso")
                } else {
                    onComplete(false, task.exception?.message ?: "Error desconocido")
                }
            }
    }

    // Iniciar sesión con correo y contraseña
    fun loginUser(email: String, password: String, onComplete: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    onComplete(true, "Inicio de sesión exitoso")
                } else {
                    onComplete(false, task.exception?.message ?: "Error desconocido")
                }
            }
    }

    // Obtener usuario actual
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Cerrar sesión
    fun logout() {
        auth.signOut()
    }
}
