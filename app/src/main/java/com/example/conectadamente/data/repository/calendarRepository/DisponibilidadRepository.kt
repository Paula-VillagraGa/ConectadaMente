package com.example.conectadamente.data.repository.calendarRepository

import android.util.Log
import com.example.conectadamente.data.model.Disponibilidad
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DisponibilidadRepositorio @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()

    // Función para obtener el ID del usuario actual
    fun obtenerIdUsuarioActual(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid
            ?: ""  // Si el usuario está autenticado, devuelve el ID, de lo contrario, cadena vacía
    }

    suspend fun cargarHorasDisponibles(fecha: String, psychoId: String): List<Disponibilidad> {
        return try {
            val querySnapshot = db.collection("availability")
                .whereEqualTo("fecha", fecha)
                .whereEqualTo("psychoId", psychoId)
                .get()
                .await()

            // Mapeamos los resultados de Firestore a objetos Disponibilidad
            querySnapshot.documents.mapNotNull { document ->
                val hora = document.getString("hora")
                val estado = document.getString("estado") ?: "no disponible"
                val fecha = document.getString("fecha")
                val psychoId = document.getString("psychoId")

                if (hora != null && fecha != null && psychoId != null) {
                    Disponibilidad(hora = hora, estado = estado, fecha = fecha, psychoId = psychoId)
                } else {
                    null
                }
            }
        } catch (exception: Exception) {
            throw Exception("Error al cargar horas: ${exception.message}")
        }
    }

    suspend fun guardarDisponibilidad(
        fecha: String,
        horas: List<String>, // Lista de horas seleccionadas
        psychoId: String,
        estado: String
    ): List<String> { // Cambiamos el retorno a una lista de IDs
        return try {
            val availabilityIds = mutableListOf<String>()

            horas.forEach { hora ->
                // Crea un documento vacío para generar el ID primero
                val documentReference = db.collection("availability").document()

                // Recupera el ID generado
                val availabilityId = documentReference.id

                // Agrega el availabilityId a los datos
                val availability = hashMapOf(
                    "availabilityId" to availabilityId, // Incluye el ID generado
                    "fecha" to fecha,
                    "hora" to hora,  // Guardamos solo una hora
                    "psychoId" to psychoId,
                    "estado" to estado // Predeterminado "disponible"
                )

                // Guarda los datos en Firestore usando el ID previamente generado
                documentReference.set(availability).await()

                // Añade el ID generado a la lista
                availabilityIds.add(availabilityId)
            }

            availabilityIds // Retorna la lista de IDs
        } catch (exception: Exception) {
            throw exception // Propaga la excepción al ViewModel
        }
    }

    // Función para eliminar una hora habilitada
    suspend fun eliminarDisponibilidad(fecha: String, hora: String, psychoId: String) {
        try {
            val querySnapshot = db.collection("availability")
                .whereEqualTo("fecha", fecha)
                .whereEqualTo("horaInicio", hora)
                .whereEqualTo("psychoId", psychoId)
                .get()
                .await()

            // Eliminar el primer documento encontrado con esa hora y fecha
            querySnapshot.documents.firstOrNull()?.reference?.delete()?.await()
        } catch (exception: Exception) {
            throw Exception("Error al eliminar la disponibilidad: ${exception.message}")
        }
    }

    suspend fun cambiarEstadoHora(
        fecha: String,
        hora: String,
        psychoId: String,
        estadoActual: String
    ) {
        try {
            val querySnapshot = db.collection("availability")
                .whereEqualTo("fecha", fecha)
                .whereEqualTo("hora", hora)
                .whereEqualTo("psychoId", psychoId)
                .get()
                .await()

            // Si ya existe un documento con esta hora, lo actualizamos
            if (querySnapshot.documents.isNotEmpty()) {
                val documentReference = querySnapshot.documents.first().reference
                val nuevoEstado =
                    if (estadoActual == "disponible") "no disponible" else "disponible"
                documentReference.update("estado", nuevoEstado).await()
            } else {
                // Si no existe, lo creamos con el estado inicial
                val availability = hashMapOf(
                    "fecha" to fecha,
                    "hora" to hora,
                    "psychoId" to psychoId,
                    "estado" to "disponible"
                )
                db.collection("availability")
                    .add(availability)
                    .await() // Utilizamos await() para hacer esta operación de forma asincrónica
            }
        } catch (exception: Exception) {
            throw Exception("Error al cambiar el estado de la hora: ${exception.message}")
        }
    }
}

