package com.example.conectadamente.data.repository.calendarRepository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AgendarRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

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
                val availabilityQuery = firestore.collection("availability")
                    .whereEqualTo("availabilityId", availabilityId)
                    .get()
                    .await()

                if (availabilityQuery.isEmpty) {
                    Log.e("AgendarRepository", "El horario no fue encontrado.")
                    return@withContext false
                }

                val availabilityDoc = availabilityQuery.documents.first()

                // Verificar el estado
                val estado = availabilityDoc.getString("estado")
                if (estado != "disponible") {
                    Log.e("AgendarRepository", "El horario ya no está disponible.")
                    return@withContext false
                }

                // Comenzar la transacción para agendar
                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(availabilityDoc.reference)
                    if (!snapshot.exists()) {
                        throw Exception("El horario no existe.")
                    }

                    val estado = snapshot.getString("estado")
                    if (estado != "disponible") {
                        throw Exception("El horario ya no está disponible.")
                    }

                    transaction.update(snapshot.reference, "estado", "reservado")

                    val hora = snapshot.getString("hora") ?: throw Exception("Hora no válida.")

                    // Crear el documento de cita en appointments
                    val appointment = mapOf(
                        "availabilityId" to availabilityId,
                        "patientId" to patientId,
                        "psychoId" to psychoId,
                        "hora" to hora,
                        "estado" to "pendiente",
                        "modalidad" to modalidad
                    )

                    transaction.set(firestore.collection("appointments").document(), appointment)
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
}