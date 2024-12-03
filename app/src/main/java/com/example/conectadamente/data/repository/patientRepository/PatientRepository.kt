package com.example.conectadamente.data.repository.patientRepository


import android.util.Log
import com.example.conectadamente.data.model.PatientModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PatientRepository @Inject constructor(
    private val firebase: FirebaseFirestore
) {

    // Recupera los datos del paciente desde Firestore usando el UID
    private suspend fun getPatientData(userId: String): PatientModel {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("patients")
            .document(userId)
            .get()
            .await() // Espera la respuesta usando corutinas

        // Lanza excepción si no existe el documento
        if (!snapshot.exists()) throw Exception("Datos del paciente no encontrados")

        // Convierte el documento a PatientModel y retorna
        return snapshot.toObject(PatientModel::class.java)
            ?: throw Exception("Error al convertir los datos del paciente")
    }


    suspend fun getCurrentPatientData(): PatientModel {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw Exception("Usuario no autenticado") // Lanza excepción si no hay usuario

        Log.d("PatientRepository", "Recuperando datos del usuario con UID: $currentUserId")

        return getPatientData(currentUserId) // Usa el UID para buscar en Firestore
    }
}
