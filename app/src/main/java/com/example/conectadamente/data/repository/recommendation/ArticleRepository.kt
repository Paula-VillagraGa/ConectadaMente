package com.example.conectadamente.data.repository.recommendation

import com.example.conectadamente.data.model.ArticleModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ArticleRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // Función para obtener artículos desde Firestore
    suspend fun getArticles(): List<ArticleModel> {
        return firestore.collection("articles")
            .orderBy("date")
            .get()
            .await()
            .documents
            .map { document ->
                document.toObject(ArticleModel::class.java)!!
            }
    }
}
