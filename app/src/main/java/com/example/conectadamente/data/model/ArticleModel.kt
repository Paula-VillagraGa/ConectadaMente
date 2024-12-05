package com.example.conectadamente.data.model

import java.util.Date


data class ArticleModel(
    val title: String,
    val summary: String,
    val imageUrl: String, // URL de la imagen
    val date: Date = Date(),     // Fecha en formato "yyyy-MM-dd"
    val idArticle: String,
    val type: String      // Tipo de noticia
)
