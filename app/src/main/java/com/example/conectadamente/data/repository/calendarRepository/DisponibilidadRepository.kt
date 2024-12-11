package com.example.conectadamente.data.repository.calendarRepository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DisponibilidadRepositorio @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()

    // Función para obtener el ID del usuario actual
    fun obtenerIdUsuarioActual(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid ?: ""  // Si el usuario está autenticado, devuelve el ID, de lo contrario, cadena vacía
    }

    // Función para guardar la disponibilidad del usuario
    suspend fun guardarDisponibilidad(fecha: String, horaInicio: String, horaFin: String, userId: String): String {
        return try {
            val availability = hashMapOf(
                "fecha" to fecha,
                "horaInicio" to horaInicio,
                "horaFin" to horaFin,
                "userId" to userId
            )

            // Guardar la disponibilidad en Firestore
            val result = db.collection("availability")
                .add(availability)
                .await() // Utilizamos await() para hacer esta operación de forma asincrónica

            "Disponibilidad guardada con éxito"
        } catch (exception: Exception) {
            "Error al guardar la disponibilidad: ${exception.message}"
        }
    }
}
