package com.example.conectadamente.data.repository.recommendation

import android.util.Log
import com.example.conectadamente.data.model.ArticleModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ArticleRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // Función para obtener artículos desde Firestore
    suspend fun getArticles(): List<ArticleModel> {
        val articlesList = mutableListOf<ArticleModel>()
        try {
            val querySnapshot = firestore.collection("articles").orderBy("date").get().await()
            for (document in querySnapshot.documents) {
                val article = ArticleModel(
                    title = document.getString("title") ?: "",
                    summary = document.getString("summary") ?: "",
                    imageUrl = document.getString("imageUrl") ?: "",
                    date = document.getDate("date") ?: Date(),
                    idArticle = document.getString("idArticle") ?: "",
                    type = document.getString("type") ?: ""
                )
                articlesList.add(article)
                Log.d("ArticleRepository", "Article: ${article.title}")  // Log para ver los artículos obtenidos
            }
        } catch (e: Exception) {
            Log.e("ArticleRepository", "Error getting articles: ${e.message}")
        }
        return articlesList
    }
}