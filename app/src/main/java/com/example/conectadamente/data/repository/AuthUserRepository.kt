package com.example.conectadamente.data.repository

import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.utils.constants.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.conectadamente.utils.validations.isValidEmail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthUserRepository @Inject constructor() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Registrar un paciente con Flow
    fun registerPatient(
        patient: PatientModel,
        password: String
    ): Flow<DataState<String>> = flow {
        emit(DataState.Loading) // Emitir estado de carga
        try {
            // Crear usuario en Firebase Authentication
            val authResult = auth.createUserWithEmailAndPassword(patient.email, password).await()
            val uid = authResult.user?.uid ?: throw IllegalStateException("UID no encontrado")

            // Guardar datos del paciente en Firestore
            db.collection("patients").document(uid).set(patient).await()

            emit(DataState.Success("Paciente registrado correctamente")) // Emitir éxito
        } catch (e: Exception) {
            emit(DataState.Error(e)) // Emitir error en caso de fallo
        } finally {
            emit(DataState.Finished) // Emitir estado finalizado
        }
    }

    // Obtener el UID
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    // Obtener datos del usuario actual
    fun getUserData(): Flow<DataState<PatientModel>> = flow {
        emit(DataState.Loading) // Estado
        try {
            val userId = getCurrentUserId() ?: throw IllegalStateException("Usuario no autenticado")
            val documentSnapshot = db.collection("patients").document(userId).get().await()
            val patient = documentSnapshot.toObject(PatientModel::class.java)
                ?: throw IllegalStateException("Datos del paciente no encontrados")

            emit(DataState.Success(patient)) // Emitir datos del paciente
        } catch (e: Exception) {
            emit(DataState.Error(e)) // Emitir error en caso de fallo
        } finally {
            emit(DataState.Finished) // Emitir estado finalizado
        }
    }



    // Iniciar sesión con Flow
    fun login(email: String, password: String): Flow<DataState<String>> = flow {
        emit(DataState.Loading) // Emitir estado de carga
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            emit(DataState.Success("Inicio de sesión exitoso")) // Emitir éxito
        } catch (e: Exception) {
            emit(DataState.Error(e)) // Emitir error en caso de fallo
        } finally {
            emit(DataState.Finished) // Emitir estado finalizado
        }
    }

    // Cerrar sesión del usuario actual
    fun logout(): Flow<DataState<String>> = flow {
        emit(DataState.Loading) // Emitir estado de carga
        try {
            auth.signOut()
            emit(DataState.Success("Cierre de sesión exitoso")) // Emitir éxito
        } catch (e: Exception) {
            emit(DataState.Error(e)) // Emitir error en caso de fallo
        } finally {
            emit(DataState.Finished) // Emitir estado finalizado
        }
    }
}
