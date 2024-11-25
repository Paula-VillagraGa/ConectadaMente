package com.example.conectadamente.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey


data class PatientModel(
    val email: String,  // Usamos el correo electrónico como identificador
    val name: String,
    val rut: String,
    val id: String = "",
    val rol: String= "paciente"

) {
    // Constructor vacío necesario para Firebase
    constructor() : this("", "", "","")
}
