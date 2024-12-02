package com.example.conectadamente.data.repository.PatientsRepo


import android.util.Log
import com.example.conectadamente.data.model.PatientModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PatientRepository @Inject constructor(
    private val firebase: FirebaseFirestore
) {

    private suspend fun getPatientData(patientId: String): PatientModel {
        return suspendCoroutine { continuation ->
            firebase.collection("patients")
                .document(patientId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val data = document.toObject(PatientModel::class.java)
                        if (data != null) {
                            continuation.resume(data)
                        } else {
                            continuation.resumeWithException(Exception("Paciente no encontrado"))
                        }
                    } else {
                        continuation.resumeWithException(Exception("Documento inexistente"))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }


    suspend fun getCurrentPatientData(): PatientModel {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw Exception("Usuario no autenticado") // Lanza excepción si no hay usuario

        Log.d("PatientRepository", "Recuperando datos del usuario con UID: $currentUserId")

        return getPatientData(currentUserId) // Usa el UID para buscar en Firestore
    }
}
