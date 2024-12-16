package com.example.conectadamente.data.repository.calendarRepository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot


class AgendarRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // Obtener horarios disponibles de un psicólogo por ID, solo los que están en estado "disponible"
    suspend fun obtenerDisponibilidadPorPsychoId(psychoId: String): List<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            try {
                val documents = firestore.collection("availability")
                    .whereEqualTo("psychoId", psychoId)
                    .whereEqualTo("estado", "disponible") // Filtrar solo los disponibles
                    .get()
                    .await()
                documents.map { it.data }
            } catch (e: FirebaseFirestoreException) {
                Log.e("AgendarRepository", "Firestore error: ${e.message}")
                emptyList()
            } catch (e: Exception) {
                Log.e("AgendarRepository", "Unexpected error: ${e.message}")
                emptyList()
            }
        }
    }

    suspend fun agendarHorario(
        availabilityId: String,
        patientId: String,
        psychoId: String,
        modalidad: String
    ): Boolean {
        if (availabilityId.isNullOrEmpty()) {
            Log.e("AgendarRepository", "availabilityId no puede ser nulo o vacío.")
            return false
        }

        return withContext(Dispatchers.IO) {
            try {
                val availabilityRef = firestore.collection("availability").document(availabilityId)
                val appointmentsRef = firestore.collection("appointments").document()

                firestore.runTransaction { transaction ->
                    // Obtener la disponibilidad
                    val snapshot = transaction.get(availabilityRef)
                    if (!snapshot.exists()) {
                        throw Exception("El horario no existe.")
                    }

                    // Verificar que el estado del horario sea "disponible"
                    val estado = snapshot.getString("estado")
                    if (estado != "disponible") {
                        throw Exception("El horario ya no está disponible.")
                    }

                    // Obtener la fecha y hora de availability
                    val fechaAvailability =
                        snapshot.getString("fecha") ?: throw Exception("Fecha no válida.")
                    val hora = snapshot.getString("hora") ?: throw Exception("Hora no válida.")

                    // Actualizar el estado del horario a "reservado"
                    transaction.update(availabilityRef, "estado", "reservado")

                    // Crear un nuevo documento en la colección "appointments"
                    val appointment = mapOf(
                        "availabilityId" to availabilityId,
                        "patientId" to patientId,
                        "psychoId" to psychoId,
                        "fecha" to fechaAvailability,  // Fecha del horario (availability)
                        "hora" to hora,  // Hora del horario
                        "modalidad" to modalidad,
                        "estado" to "pendiente",  // Estado inicial de la cita
                        "agendadoEn" to Timestamp.now() // Fecha y hora de la reserva (timestamp)
                    )

                    transaction.set(appointmentsRef, appointment)
                }.await()

                Log.d("AgendarRepository", "Cita agendada exitosamente")
                true
            } catch (e: FirebaseFirestoreException) {
                Log.e("AgendarRepository", "Error en Firestore: ${e.message}")
                false
            } catch (e: Exception) {
                Log.e("AgendarRepository", "Error inesperado: ${e.message}")
                false
            }
        }
    }
    suspend fun obtenerHorariosPendientes(psychoId: String): List<String> {
        val firestore = FirebaseFirestore.getInstance()

        return try {
            val querySnapshot: QuerySnapshot = firestore.collection("appointments")
                .whereEqualTo("psychoId", psychoId) // Filtra por psychoId
                .whereEqualTo("estado", "pendiente") // Filtra por estado pendiente
                .get()
                .await() // Espera la consulta

            // Extraer las horas de las citas pendientes
            val horariosPendientes = querySnapshot.documents.mapNotNull { document ->
                document.getString("hora") // Extrae el campo "hora"
            }

            horariosPendientes
        } catch (e: Exception) {
            // Manejo de errores
            println("Error al obtener horarios pendientes: ${e.message}")
            emptyList<String>() // Si ocurre un error, retorna una lista vacía
        }
    }
}