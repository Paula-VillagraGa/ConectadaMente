package com.example.conectadamente.data.model

data class ReviewModel (
    val psychoId: String = "",
    val patientId: String = "",
    val tags: List<String> = emptyList(),
    val rating: Double = 0.0,
    val timestamp: Long = 0L,
    val id: String = "",
    val patientName: String? = null
)
