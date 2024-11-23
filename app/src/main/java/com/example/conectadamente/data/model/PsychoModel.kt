package com.example.conectadamente.data.model




data class PsychoModel(
    val email: String,  // Usamos el correo electrónico como identificador
    val name: String,
    val rut: String,
    val id: String = "",
    val isVerified: Boolean = false,
    val documentUrls: List<String> = emptyList()


) {
    // Constructor vacío necesario para Firebase
    constructor() : this("", "", "")
}
