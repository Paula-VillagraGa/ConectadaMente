package com.example.conectadamente.data.repository

import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.data.model.ReviewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await // Usamos `await()` para las tareas asincrónicas
import javax.inject.Inject



class PsychoRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getPsychoInfo(psychoId: String): Result<PsychoModel> {
        if (psychoId.isBlank()) {
            return Result.failure(IllegalArgumentException("Psycho ID cannot be blank"))
        }

        return try {
            val psychoRef = firestore.collection("psychos").document(psychoId)
            val psychoSnapshot = psychoRef.get().await()
            val psycho = psychoSnapshot.toObject(PsychoModel::class.java)

            if (psycho != null) {
                Result.success(psycho)
            } else {
                Result.failure(NullPointerException("Psycho with ID '$psychoId' not found"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to fetch psycho info: ${e.localizedMessage}"))
        }
    }

    suspend fun getReviews(psychoId: String): List<ReviewModel> {
        if (psychoId.isBlank()) return emptyList()

        return try {
            val reviewsRef = firestore.collection("reviews").whereEqualTo("psychoId", psychoId)
            val reviewsSnapshot = reviewsRef.get().await()

            reviewsSnapshot.documents.mapNotNull { doc ->
                doc.toObject(ReviewModel::class.java)
            }
        } catch (e: Exception) {
            // Puedes registrar el error aquí
            emptyList()
        }
    }
}
