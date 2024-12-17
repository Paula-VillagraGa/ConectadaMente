package com.example.conectadamente.data.model

data class Disponibilidad(
    val availabilityId: String = "",  // Agregar este campo para almacenar el ID de disponibilidad
    val fecha: String = "",          // Fecha en formato "DD/MM/YYYY"
    val hora: String = "",           // Hora en formato "HH:MM"
    val estado: String = "",         // "disponible" o "no disponible"
    val psychoId: String = ""        // ID del psicólogo
)

data class Appointment(
    val appointmentId: String,
    val fechaHora: String,
    val paciente: String?
)
