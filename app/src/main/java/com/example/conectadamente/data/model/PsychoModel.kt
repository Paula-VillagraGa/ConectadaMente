package com.example.conectadamente.data.model



data class PsychoModel(
    val email: String = "",
    val name: String = "",
    val phone: String? = null,
    val experience: String? = null,
    val verified: Boolean = true,
    val documentUrls: List<String> = emptyList(),
    val id: String = "",
    val rut: String = "",
    val rol: String = "psicologo",
    val specialization: List<String>? = emptyList(),
    val rating: Double = 0.0,
    val tagsSpecific: List<String> = emptyList(),
    val descriptionPsycho: String? = null,
    val photoUrl: String? = null
)

