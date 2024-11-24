package com.example.conectadamente.data.repository

import com.example.conectadamente.utils.constants.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
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
    // Funciones de login y logout
    fun login(email: String, password: String): Flow<DataState<String>> = flow {
        emit(DataState.Loading)
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            emit(DataState.Success("Inicio de sesión exitoso"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            emit(DataState.Error(Exception("Credenciales inválidas. Verifica tu correo y contraseña.")))
        } catch (e: FirebaseAuthInvalidUserException) {
            emit(DataState.Error(Exception("Usuario no encontrado. Verifica si estás registrado.")))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        } finally {
            emit(DataState.Finished)
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

    // Obtener rol del usuario actual
    fun getUserRole(): Flow<DataState<String>> = flow {
        emit(DataState.Loading)
        try {
            val uid = auth.currentUser?.uid ?: throw IllegalStateException("Usuario no autenticado")
            val docSnapshot = db.collection("psychos").document(uid).get().await()
            if (docSnapshot.exists()) {
                emit(DataState.Success("Psicólogo"))
            } else {
                val patientSnapshot = db.collection("patients").document(uid).get().await()
                if (patientSnapshot.exists()) {
                    emit(DataState.Success("Paciente"))
                } else {
                    throw IllegalStateException("Rol no encontrado.")
                }
            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
        } finally {
            emit(DataState.Finished)
        }
    }
}
