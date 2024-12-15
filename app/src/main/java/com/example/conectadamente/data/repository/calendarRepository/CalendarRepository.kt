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

    suspend fun agendarHorario(
        availabilityId: String,
        patientId: String,
        psychoId: String,
        modalidad: String // Modalidad es el nuevo parámetro que hemos agregado
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val availabilityRef = firestore.collection("availability").document(availabilityId)
                val appointmentsRef = firestore.collection("appointments").document()

                firestore.runTransaction { transaction ->
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

                    // Ahora solo tenemos una "hora" en lugar de "horaInicio" y "horaFin"
                    val hora = snapshot.getString("hora") ?: throw Exception("Hora no válida.")

                    // Crear un nuevo documento en la colección "appointments"
                    val appointment = mapOf(
                        "availabilityId" to availabilityId,
                        "patientId" to patientId,
                        "psychoId" to psychoId,
                        "hora" to hora, // Usamos la hora directamente
                        "estado" to "pendiente",
                        "modalidad" to modalidad // Aquí agregamos la modalidad
                    )
                    transaction.set(appointmentsRef, appointment)
                }.await() // Espera a que la transacción termine
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