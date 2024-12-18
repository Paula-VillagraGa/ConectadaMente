package com.example.conectadamente.data.model

import com.google.firebase.Timestamp

data class Disponibilidad(
    val availabilityId: String = "",  // Agregar este campo para almacenar el ID de disponibilidad
    val fecha: String = "",          // Fecha en formato "DD/MM/YYYY"
    val hora: String = "",           // Hora en formato "HH:MM"
    val estado: String = "",         // "disponible" o "no disponible"
    val psychoId: String = ""        // ID del psicólogo
)

data class Appointment(
    val appointmentId: String ="",
    val fechaHora: String = "",  // Fecha y hora de la cita
    val paciente: String? = null,  // Nombre del paciente, puede ser nulo
    val agendadoEn: Timestamp? = null,  // Marca de tiempo
    val availabilityId: String = "",  // ID de disponibilidad
    val estado: String = "",  // Estado de la cita (Realizada, Cancelada, etc.)
    val fecha: String = "",  // Fecha de la cita
    val hora: String = "",  // Hora de la cita
    val modalidad: String = "",  // Modalidad de la cita (Presencial, Virtual)
    val observaciones: String = "",  // Observaciones de la cita
    val patientId: String = "",  // ID del paciente
    val psychoId: String = ""  // ID del psicólogo
)
