package com.example.conectadamente.data.model



data class ArticleModel(
    val title: String,
    val summary: String,
    val imageUrl: String, // URL de la imagen
    val date: String,     // Fecha en formato "yyyy-MM-dd"
    val idArticle: String,
    val type: String      // Tipo de noticia
)
