package com.example.conectadamente.data.model

data class Disponibilidad(
    val fecha: String = "",       // Fecha en formato "YYYY-MM-DD"
    val horaInicio: String = "",  // Hora de inicio de disponibilidad: "10:00"
    val horaFin: String = "",     // Hora de fin de disponibilidad: "12:00"
    val psychoId: String = ""     // ID del psicólogo
)

data class Cita(
    val fecha: String = "",
    val hora: String = "",
    val psychoId: String = "",
    val pacienteId: String = ""
)
