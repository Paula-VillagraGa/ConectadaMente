package com.example.conectadamente.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey


data class PatientModel(
    val email: String,  // Usamos el correo electrónico como identificador
    val rut: String,
    val firstName: String,
    val lastName: String = "", // Campo opcional
    val phoneNumber: String = "" // Campo opcional

) {
    // Constructor vacío necesario para Firebase
    constructor() : this("", "", "")
}
