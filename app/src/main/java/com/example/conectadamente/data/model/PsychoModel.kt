package com.example.conectadamente.data.model



data class PsychoModel(
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val age: String = "",
    val isVerified: Boolean = false,
    val documentUrls: List<String> = emptyList(),
    val id: String = "",
    val rut: String = ""
)

