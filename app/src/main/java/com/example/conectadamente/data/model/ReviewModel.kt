package com.example.conectadamente.data.model

data class ReviewModel (
    val psychoId: String,
    val patientId: String,
    val tags: List<String>,
    val rating: Double,
    val timestamp: Long,

)