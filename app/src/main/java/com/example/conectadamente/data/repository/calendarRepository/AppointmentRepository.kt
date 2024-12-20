package com.example.conectadamente.data.repository.calendarRepository

import android.util.Log
import com.example.conectadamente.data.model.Appointment
import com.example.conectadamente.data.model.PsychoModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class AppointmentRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun obtenerCitasPendientesYPacientesPorPsychoId(psychoId: String): List<Appointment> {
        return withContext(Dispatchers.IO) {
            try {
                val psychoId = FirebaseAuth.getInstance().currentUser?.uid
                    ?: throw Exception("Usuario no autenticado")

                // Obtener las citas pendientes para el psicólogo
                val citasSnapshot = firestore.collection("appointments")
                    .whereEqualTo("psychoId", psychoId)
                    .whereEqualTo("estado", "pendiente")
                    .get()
                    .await()

                Log.d("Debug", "Número de citas encontradas: ${citasSnapshot.size()}")

                val citas = mutableListOf<Appointment>()

                // Iterar sobre las citas encontradas
                for (citaDocument in citasSnapshot.documents) {
                    val patientId = citaDocument.getString("patientId") // Obtener el patientId de la cita

                    if (patientId != null) {
                        // Obtener el nombre del paciente
                        val pacienteDoc = firestore.collection("patients").document(patientId).get().await()
                        val nombrePaciente = pacienteDoc.getString("name")



                        val agendadoEnTimestamp = citaDocument.getTimestamp("agendadoEn")
                        val fechaHora = "${citaDocument.getString("fecha")} ${citaDocument.getString("hora")}"
                        val appointment = Appointment(
                            appointmentId = citaDocument.id,
                            fechaHora = fechaHora,
                            paciente = nombrePaciente,
                            agendadoEn = agendadoEnTimestamp,
                            availabilityId = citaDocument.getString("availabilityId") ?: "",
                            estado = citaDocument.getString("estado") ?: "",
                            fecha = citaDocument.getString("fecha") ?: "",
                            hora = citaDocument.getString("hora") ?: "",
                            modalidad = citaDocument.getString("modalidad") ?: "",
                            observaciones = citaDocument.getString("observaciones") ?: "",
                            patientId = citaDocument.getString("patientId") ?: "",
                            psychoId = citaDocument.getString("psychoId") ?: ""
                        )
                        citas.add(appointment)
                    }
                }

                citas
            } catch (e: Exception) {
                Log.e("AppointmentRepository", "Error al obtener citas pendientes y pacientes: ${e.message}")
                emptyList() // Si ocurre un error, retornamos una lista vacía
            }
        }
    }

    suspend fun actualizarEstadoCitaConObservaciones(
        appointmentId: String,
        nuevoEstado: String,
        observaciones: String,
        recomendaciones: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val appointmentRef = firestore.collection("appointments").document(appointmentId)

                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(appointmentRef)
                    if (!snapshot.exists()) throw Exception("La cita no existe.")

                    transaction.update(
                        appointmentRef, mapOf(
                            "estado" to nuevoEstado,
                            "observaciones" to observaciones,
                            "recomendaciones" to recomendaciones
                        )
                    )
                }.await()

                true
            } catch (e: Exception) {
                Log.e("AppointmentRepository", "Error al actualizar cita: ${e.message}")
                false
            }
        }
    }
    suspend fun getCompletedAppointments(psychoId: String): List<CompletedAppointment> {
        val appointments = mutableListOf<CompletedAppointment>()

        try {
            // Obtener citas donde estado = "Realizada" y psychoId coincide
            val appointmentQuery = firestore.collection("appointments")
                .whereEqualTo("estado", "Realizada")
                .whereEqualTo("psychoId", psychoId)
                .get()
                .await()

            for (document in appointmentQuery.documents) {
                val patientId = document.getString("patientId") ?: ""
                val fecha = document.getString("fecha") ?: ""
                val hora = document.getString("hora") ?: ""
                val observaciones = document.getString("observaciones") ?: ""
                val recomendaciones = document.getString("recomendaciones") ?: ""

                // Obtener el nombre y rut del paciente desde la colección "patients"
                val patientDoc = firestore.collection("patients")
                    .document(patientId)
                    .get()
                    .await()

                val patientName = patientDoc.getString("name") ?: "Nombre no disponible"
                val patientRut = patientDoc.getString("rut") ?: "Rut no disponible"

                // Crear el objeto de cita completada
                appointments.add(
                    CompletedAppointment(
                        fecha = fecha,
                        hora = hora,
                        patientName = patientName,
                        rut = patientRut,
                        observaciones = observaciones,
                        recomendaciones = recomendaciones
                    )
                )
            }

        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener citas completadas: $e")
        }

        return appointments
    }
    suspend fun obtenerCitasRealizadasPorPaciente(patientId: String): List<CompletedAppointmentPatient> {
        val appointments = mutableListOf<CompletedAppointmentPatient>()

        try {
            // Obtener citas donde estado = "Realizada" y psychoId coincide
            val appointmentQuery = firestore.collection("appointments")
                .whereEqualTo("estado", "Realizada")
                .whereEqualTo("patientId", patientId)
                .get()
                .await()

            for (document in appointmentQuery.documents) {
                val psychoId = document.getString("psychoId") ?: ""
                val fecha = document.getString("fecha") ?: ""
                val hora = document.getString("hora") ?: ""
                val recomendaciones= document.getString("recomendaciones") ?: ""


                val psychoDoc = firestore.collection("psychos")
                    .document(psychoId)
                    .get()
                    .await()

                val psychoName = psychoDoc.getString("name") ?: "Nombre no disponible"
                val psychoPhoto= psychoDoc.getString("photoUrl")?: "Sin foto"


                // Crear el objeto de cita completada
                appointments.add(
                    CompletedAppointmentPatient(
                        fecha = fecha,
                        hora = hora,
                        psychoName = psychoName,
                        rut = "",
                        recomendaciones = recomendaciones,
                        psychoPhoto = psychoPhoto
                    )
                )
            }

        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener citas completadas: $e")
        }

        return appointments
    }
    suspend fun obtenerCitasDelPaciente(patientId: String): List<Appointment> {
        return withContext(Dispatchers.IO) {
            val citas = mutableListOf<Appointment>()
            try {
                // Consulta las citas donde patientId coincida con el usuario autenticado
                val appointmentsSnapshot = firestore.collection("appointments")
                    .whereEqualTo("patientId", patientId)
                    .get()
                    .await()

                for (doc in appointmentsSnapshot.documents) {
                    val cita = doc.toObject(Appointment::class.java)
                    if (cita != null) {
                        citas.add(cita)
                    }
                }
            } catch (e: Exception) {
                Log.e("AppointmentRepository", "Error al obtener citas: ${e.message}")
            }
            citas
        }
    }
    suspend fun cancelarCitaYActualizar(patientId: String, appointmentId: String, availabilityId: String): Boolean {
        return cancelarCita(appointmentId, availabilityId)
    }


    suspend fun getPsychologistsByIds(psychoIds: List<String>): Map<String, PsychoModel> {
        return withContext(Dispatchers.IO) {
            val psychologistsMap = mutableMapOf<String, PsychoModel>()
            try {
                val querySnapshot = firestore.collection("psychos")
                    .whereIn("id", psychoIds)
                    .get()
                    .await()

                for (doc in querySnapshot.documents) {
                    val psycho = doc.toObject(PsychoModel::class.java)
                    if (psycho != null) {
                        psychologistsMap[psycho.id] = psycho
                    }
                }
            } catch (e: Exception) {
                Log.e("AppointmentRepository", "Error al obtener psicólogos: ${e.message}")
            }
            psychologistsMap
        }
    }

    // Cancelar cita y actualizar disponibilidad
    suspend fun cancelarCita(appointmentId: String, availabilityId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val appointmentRef = firestore.collection("appointments").document(appointmentId)
                val availabilityRef = firestore.collection("availability").document(availabilityId)

                firestore.runTransaction { transaction ->
                    val appointmentSnapshot = transaction.get(appointmentRef)
                    val availabilitySnapshot = transaction.get(availabilityRef)

                    if (!appointmentSnapshot.exists()) throw Exception("Cita no encontrada.")
                    if (!availabilitySnapshot.exists()) throw Exception("Disponibilidad no encontrada.")

                    // Actualizar estado de la cita
                    transaction.update(appointmentRef, "estado", "Cancelada")
                    // Actualizar disponibilidad a "Disponible"
                    transaction.update(availabilityRef, "estado", "disponible")
                }.await()

                true
            } catch (e: Exception) {
                Log.e("AppointmentRepository", "Error al cancelar la cita: ${e.message}")
                false
            }
        }
    }
}


// Modelo de datos actualizado
data class CompletedAppointment(
    val fecha: String,
    val hora: String,
    val patientName: String,
    val rut: String,
    val observaciones: String,
    val recomendaciones: String
)

// Modelo de datos actualizado
data class CompletedAppointmentPatient(
    val fecha: String,
    val hora: String,
    val psychoName: String,
    val rut: String,
    val recomendaciones: String,
    val psychoPhoto: String
)