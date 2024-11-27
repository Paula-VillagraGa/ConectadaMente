package com.example.conectadamente.data.repository

import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.utils.constants.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
            (DataState.Error(e.message ?: "Error desconocido"))
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
            (DataState.Error(e.message ?: "Error desconocido"))
        } finally {
            emit(DataState.Finished) // Emitir estado finalizado
        }
    }


}

