package com.example.conectadamente.data.repository.reviews

import android.util.Log
import com.example.conectadamente.data.model.Appointment
import com.example.conectadamente.data.model.ReviewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createReview(
        psychoId: String,
        patientId: String,
        tags: List<String>,
        rating: Double
    ) {
        try {
            val newReviewRef = firestore.collection("reviews").document()
            val reviewData = mapOf(
                "id" to newReviewRef.id,
                "psychoId" to psychoId,
                "patientId" to patientId,
                "tags" to tags,
                "rating" to rating,
                "timestamp" to System.currentTimeMillis()
            )

            newReviewRef.set(reviewData).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateReview(
        reviewId: String,
        psychoId: String,
        patientId: String,
        tags: List<String>,
        rating: Double
    ) {
        try {
            firestore.collection("reviews")
                .document(reviewId)
                .update(
                    mapOf(
                        "psychoId" to psychoId,
                        "patientId" to patientId,
                        "tags" to tags,
                        "rating" to rating,
                        "timestamp" to System.currentTimeMillis()
                    )
                )
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Obtener una reseña existente
    suspend fun getExistingReview(psychoId: String, patientId: String): ReviewModel? {
        return try {
            val reviewQuery = firestore.collection("reviews")
                .whereEqualTo("psychoId", psychoId)
                .whereEqualTo("patientId", patientId)
                .get()
                .await()

            if (reviewQuery.documents.isNotEmpty()) {
                val doc = reviewQuery.documents.first()
                ReviewModel(
                    psychoId = doc.getString("psychoId") ?: "",
                    patientId = doc.getString("patientId") ?: "",
                    tags = doc.get("tags") as? List<String> ?: emptyList(),
                    rating = doc.getDouble("rating") ?: 0.0,
                    timestamp = doc.getLong("timestamp") ?: 0L,
                    id = doc.id
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    // Calcular el promedio de las calificaciones para un psicólogo
    suspend fun getAverageRating(psychoId: String): Double {
        return try {
            val reviewQuery = firestore.collection("reviews")
                .whereEqualTo("psychoId", psychoId)
                .get()
                .await()

            if (reviewQuery.isEmpty) {
                0.0
            } else {
                val ratings = reviewQuery.documents.map { it.getDouble("rating") ?: 0.0 }
                ratings.average()
            }
        } catch (e: Exception) {
            0.0
        }
    }

    val patientId = FirebaseAuth.getInstance().currentUser?.uid
    suspend fun getAppointment(patientId: String,psychoId: String): Appointment? {
        Log.d("AppointmentDebug", "Current User ID: $patientId")
        Log.d("AppointmentDebug", "Psycho ID: $psychoId")

        return try {
            // Realiza la consulta en Firestore
            val appointment = firestore.collection("appointments")
                .whereEqualTo("patientId", patientId)
                .whereEqualTo("psychoId", psychoId)
                .whereEqualTo("estado", "Realizada")
                .get()
                .await()
                .documents
                .firstOrNull()
                ?.toObject(Appointment::class.java)


            if (appointment != null) {
                Log.d("AppointmentDebug", "Cita encontrada: ${appointment.fecha}")
            } else {
                Log.d(
                    "AppointmentDebug",
                    "No se encontró cita para el paciente y psicólogo especificados."
                )
            }

            appointment
        } catch (e: Exception) {
            Log.e("AppointmentDebug", "Error al obtener la cita: ${e.message}")
            null // Si ocurre un error, se devuelve null
        }
    }
}
