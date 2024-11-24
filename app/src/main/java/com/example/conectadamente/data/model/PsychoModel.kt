package com.example.conectadamente.data.model



data class PsychoModel(
    val email: String = "",
    val name: String = "",
    val phone: String? = null,
    val age: String? = null,
    val isVerified: Boolean = false,
    val documentUrls: List<String> = emptyList(),
    val id: String = "",
    val rut: String = ""
)

