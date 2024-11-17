package com.example.conectadamente.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient")
data class PatientModel(
    @PrimaryKey val email: String,  // Usamos el correo electrónico como clave primaria
    val rut: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val password: String,
    val profileImageUri: String? = null // Foto de perfil opcional
)