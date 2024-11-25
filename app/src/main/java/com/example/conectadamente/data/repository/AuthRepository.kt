package com.example.conectadamente.data.repository

import android.util.Log
import com.example.conectadamente.utils.constants.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {

    suspend fun handleLogin(): String {
        val user = auth.currentUser ?: return "unauthenticated"

        val uid = user.uid

        return try {
            // Buscar en la colección de psicólogos
            val psychoDoc = db.collection("psychos").document(uid).get().await()
            if (psychoDoc.exists()) {
                val isVerified = psychoDoc.getBoolean("isVerified") ?: false
                if (isVerified) {
                    "psicologo"
                } else {
                    "unverified"
                }
            } else {
                // Si no está en psicólogos, buscar en pacientes
                val patientDoc = db.collection("patients").document(uid).get().await()
                if (patientDoc.exists()) {
                    "paciente"
                } else {
                    "none"
                }
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al determinar el rol del usuario", e)
            "error"
        }
    }

    fun logout(): Flow<DataState<String>> = flow {
        emit(DataState.Loading)
        try {
            auth.signOut()
            emit(DataState.Success("Cierre de sesión exitoso"))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        } finally {
            emit(DataState.Finished)
        }
    }
    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user ?: throw Exception("No se pudo obtener el usuario después del inicio de sesión")
        } catch (e: FirebaseAuthInvalidUserException) {
            throw Exception("Usuario no encontrado. Verifica el correo.")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw Exception("Contraseña incorrecta. Intenta nuevamente.")
        } catch (e: Exception) {
            throw Exception("Error al iniciar sesión: ${e.message}")
        }
    }

    // Obtener rol del usuario actual
    suspend fun getRoleForUser(uid: String): String{
        return try {
            // Verificar si el usuario es psicólogo
            val psychoDoc = db.collection("psychos").document(uid).get().await()
            if (psychoDoc.exists()) {
                val isVerified = psychoDoc.getBoolean("isVerified") ?: false
                return if (isVerified) "psicologo" else "unverified"
            }

            // Verificar si el usuario es paciente
            val patientDoc = db.collection("patients").document(uid).get().await()
            if (patientDoc.exists()) {
                return "paciente"
            }

            // Si no es ninguno de los dos, devolver "none"
            "none"
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al obtener el rol del usuario", e)
            "error"
        }
    }
}