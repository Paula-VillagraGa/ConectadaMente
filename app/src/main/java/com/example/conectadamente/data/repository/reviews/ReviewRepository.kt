package com.example.conectadamente.data.repository.reviews

import com.example.conectadamente.data.model.ReviewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun submitRating(
        psychoId: String,
        patientId: String,
        tags: List<String>,
        rating: Double
    ): Result<String> {
        return try {
            // Crear el modelo de reseña
            val review = ReviewModel(
                psychoId = psychoId,
                patientId = patientId,
                tags = tags,
                rating = rating,
                timestamp = System.currentTimeMillis()
            )

            // Guardar en la colección 'reviews'
            firestore.collection("reviews")
                .add(review)
                .await()

            Result.success("Reseña enviada con éxito")
        } catch (e: Exception) {
            Result.failure(e)
        }
    } // Calcular el promedio de las calificaciones para un psicólogo específico

    suspend fun getAverageRating(psychoId: String): Double {
        return try {
            val reviewQuery = firestore.collection("reviews")
                .whereEqualTo("psychoId", psychoId)
                .get()
                .await()

            // Si no hay reseñas, el promedio es 0
            if (reviewQuery.isEmpty) {
                0.0
            } else {
                val ratings = reviewQuery.documents.map { it.getDouble("rating") ?: 0.0 }
                ratings.average()
            }
        } catch (e: Exception) {
            0.0
        }
    }// Parte de actualizar review psycho+patient
    suspend fun upsertRating(
        psychoId: String,
        patientId: String,
        tags: List<String>,
        rating: Double
    ): Result<String> {
        return try {
            // Buscar una reseña existente
            val reviewQuery = firestore.collection("reviews")
                .whereEqualTo("psychoId", psychoId)
                .whereEqualTo("patientId", patientId)
                .get()
                .await()

            if (reviewQuery.documents.isNotEmpty()) {
                // Si existe, actualizar la reseña
                val existingDocId = reviewQuery.documents.first().id
                firestore.collection("reviews")
                    .document(existingDocId)
                    .update(
                        mapOf(
                            "tags" to tags,
                            "rating" to rating,
                            "timestamp" to System.currentTimeMillis()
                        )
                    )
                    .await()
                Result.success("Reseña actualizada con éxito")
            } else {
                // Si no existe, crear una nueva
                val review = ReviewModel(
                    psychoId = psychoId,
                    patientId = patientId,
                    tags = tags,
                    rating = rating,
                    timestamp = System.currentTimeMillis()
                )
                firestore.collection("reviews")
                    .add(review)
                    .await()
                Result.success("Reseña creada con éxito")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



}

