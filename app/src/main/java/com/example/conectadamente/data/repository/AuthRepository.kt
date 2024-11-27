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
            // Obtener el rol del usuario, centralizando la lógica
            val role = getRoleForUser(uid)
            if (role == "none") {
                return "sin_rol"
            } else {
                return role
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al determinar el rol del usuario", e)
            return "error"
        }
    }

    fun logout(): Flow<DataState<String>> = flow {
        emit(DataState.Loading)
        try {
            auth.signOut()
            emit(DataState.Success("Cierre de sesión exitoso"))
        } catch (e: Exception) {
            emit(DataState.Error(e.message ?: "Error desconocido"))
        } finally {
            emit(DataState.Finished)
        }
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user
                ?: throw Exception("No se pudo obtener el usuario después del inicio de sesión")
        } catch (e: FirebaseAuthInvalidUserException) {
            throw Exception("Usuario no encontrado. Verifica el correo.")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw Exception("Contraseña incorrecta. Intenta nuevamente.")
        } catch (e: Exception) {
            throw Exception("Error al iniciar sesión: ${e.message}")
        }
    }

    suspend fun getRoleForUser(uid: String): String {
        return try {
            // Primero, intentar obtener el documento en la colección "patients"
            val patientDoc = db.collection("patients").document(uid).get().await()
            // Luego intentar en la colección "psychos"
            val psychoDoc = db.collection("psychos").document(uid).get().await()

            if (patientDoc.exists()) {
                // Si el documento existe en "patients", es un paciente
                Log.d("AuthRepository", "Documento encontrado en pacientes: ${patientDoc.data}")
                return "paciente"
            } else if (psychoDoc.exists()) {
                // Si el usuario es psicólogo, validar su estado de verificación
                Log.d("AuthRepository", "Usuario encontrado en 'psychos': ${psychoDoc.data}")
                val isVerified = psychoDoc.getBoolean("verified") ?: false
                Log.d("AuthRepository", "verified: $isVerified")
                return if (isVerified) "psicologo" else "unverified"

            } else {
                // Si no se encuentra en ninguna de las colecciones, no tiene rol
                Log.d("AuthRepository", "Documento no encontrado para UID: $uid")
                return "none" // No tiene acceso porque no tiene rol
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al obtener el rol del usuario", e)
            return "error" // Manejo de errores
        }
    }
}
