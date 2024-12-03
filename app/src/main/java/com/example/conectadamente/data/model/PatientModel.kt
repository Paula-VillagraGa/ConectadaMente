package com.example.conectadamente.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey


data class PatientModel(
    val email: String = "",   // Correo electrónico
    val name: String = "",    // Nombre
    val rut: String = "",     // RUT
    val userId: String = "",  // ID del usuario (UID de Firebase)
    val rol: String = "paciente" // Rol predeterminado
) {
    // Constructor sin argumentos requerido por Firebase
    constructor() : this("", "", "", "", "paciente")
}
