package com.example.conectadamente.data.repository.calendarRepository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppointmentRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // Obtener todas las citas pendientes para un psicólogo y obtener los nombres de los pacientes
    suspend fun obtenerCitasPendientesYPacientesPorPsychoId(psychoId: String): List<Pair<String, String?>> {
        return withContext(Dispatchers.IO) {
            try {
                // Obtener el ID del usuario logueado (psychoId)
                val psychoId = FirebaseAuth.getInstance().currentUser?.uid
                    ?: throw Exception("Usuario no autenticado")

                // Log del psychoId para ver si se obtiene correctamente
                Log.d("AppointmentRepository", "psychoId (current user): $psychoId")
                // Obtener las citas pendientes para el psicólogo
                val citasSnapshot = firestore.collection("appointments")
                    .whereEqualTo("psychoId", psychoId)
                    .whereEqualTo("estado", "pendiente")
                    .get()
                    .await()

                Log.d("Debug", "Número de citas encontradas: ${citasSnapshot.size()}")

                val citasYPacientes = mutableListOf<Pair<String, String?>>()

                // Iterar sobre las citas encontradas
                for (citaDocument in citasSnapshot.documents) {
                    val patientId =
                        citaDocument.getString("patientId") // Obtener el patientId de la cita

                    // Verificar que el patientId exista
                    if (patientId != null) {
                        // Obtener el nombre del paciente
                        val paciente =
                            firestore.collection("patients").document(patientId).get().await()
                        val nombrePaciente =
                            paciente.getString("name") // Obtener el nombre del paciente

                        // Añadir la cita con el nombre del paciente a la lista
                        val fechaHora =
                            "${citaDocument.getString("fecha")} ${citaDocument.getString("hora")}"
                        citasYPacientes.add(Pair(fechaHora, nombrePaciente))
                    }
                }

                citasYPacientes
            } catch (e: Exception) {
                Log.e(
                    "AppointmentRepository",
                    "Error al obtener citas pendientes y pacientes: ${e.message}"
                )
                emptyList() // Si ocurre un error, retornamos una lista vacía
            }
        }
    }


    // Actualizar el estado de una cita (cancelada, realizada, etc.)
    suspend fun actualizarEstadoCita(appointmentId: String, nuevoEstado: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val appointmentRef = firestore.collection("appointments").document(appointmentId)

                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(appointmentRef)
                    if (!snapshot.exists()) {
                        throw Exception("La cita no existe.")
                    }

                    // Actualizar el estado de la cita
                    transaction.update(appointmentRef, "estado", nuevoEstado)
                }.await()

                true
            } catch (e: FirebaseFirestoreException) {
                Log.e("AppointmentRepository", "Firestore error: ${e.message}")
                false
            } catch (e: Exception) {
                Log.e("AppointmentRepository", "Unexpected error: ${e.message}")
                false
            }
        }
    }
}
