package com.example.conectadamente.data.repository.recommendation

import android.util.Log
import com.example.conectadamente.data.model.RecommendationModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookRepository  @Inject constructor(private val firestore: FirebaseFirestore) {

    suspend fun getRecommendations(): List<RecommendationModel> {
        val recommendationsList = mutableListOf<RecommendationModel>()
        try {
            val querySnapshot = firestore.collection("recommendations").get().await()
            for (document in querySnapshot.documents) {
                recommendationsList.add(
                    RecommendationModel(
                        category = document.getString("category") ?: "",
                        imageRes = document.getString("imageRes") ?: "",
                        title = document.getString("title") ?: "",
                        autor = document.getString("autor") ?: "",
                        description = document.getString("description") ?: ""
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("BookRepository", "Error getting recommendations: ${e.message}")
        }
        return recommendationsList
    }
}
