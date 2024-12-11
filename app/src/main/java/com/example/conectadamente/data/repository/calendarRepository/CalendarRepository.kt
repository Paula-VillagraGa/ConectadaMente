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

    // Agendar un horario, actualizando su estado y creando un documento en "appointments"
    suspend fun agendarHorario(availabilityId: String, patientId: String, psychoId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val availabilityRef = firestore.collection("availability").document(availabilityId)
                val appointmentsRef = firestore.collection("appointments").document()

                firestore.runTransaction { transaction ->
                    // Verificar que el horario existe y está disponible
                    val snapshot = transaction.get(availabilityRef)
                    if (!snapshot.exists()) {
                        throw Exception("El horario no existe.")
                    }
                    val estado = snapshot.getString("estado")
                    if (estado != "disponible") {
                        throw Exception("El horario ya no está disponible.")
                    }

                    // Actualizar el estado del horario a "reservado"
                    transaction.update(availabilityRef, "estado", "reservado")

                    // Crear un nuevo documento en la colección "appointments"
                    val appointment = mapOf(
                        "availabilityId" to availabilityId,
                        "patientId" to patientId,
                        "psychoId" to psychoId,
                        "fecha" to snapshot.getString("fecha"),
                        "horaInicio" to snapshot.getString("horaInicio"),
                        "horaFin" to snapshot.getString("horaFin"),
                        "estado" to "pendiente" // Estado inicial de la cita
                    )
                    transaction.set(appointmentsRef, appointment)
                }.await()
                true
            } catch (e: FirebaseFirestoreException) {
                Log.e("AgendarRepository", "Firestore error: ${e.message}")
                false
            } catch (e: Exception) {
                Log.e("AgendarRepository", "Unexpected error: ${e.message}")
                false
            }
        }
    }
}
