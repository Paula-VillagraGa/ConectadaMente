package com.example.conectadamente.data.repository.PatientsRepo

import android.util.Log
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.utils.constants.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class AuthUserRepository @Inject constructor() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Registrar un paciente con Flow

    suspend fun registerPatientInFirebaseAuth(email: String, password: String): String {
        return suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val uid = authResult.user?.uid
                    if (uid != null) {
                        continuation.resume(uid) // Devuelve el UID del usuario registrado
                    } else {
                        continuation.resumeWithException(Exception("UID no encontrado"))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    }
    suspend fun savePatientData(uid: String, patient: PatientModel) {
        db.collection("patients")
            .document(uid)
            .set(patient)
            .addOnSuccessListener {
                Log.d("PatientRepository", "Datos del paciente guardados con éxito.")
            }
            .addOnFailureListener { e ->
                Log.e("PatientRepository", "Error al guardar los datos: ${e.message}")
            }
    }


    // Obtener el UID
    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
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

