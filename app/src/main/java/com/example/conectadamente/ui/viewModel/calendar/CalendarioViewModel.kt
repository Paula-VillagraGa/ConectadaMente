package com.example.conectadamente.ui.viewModel.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.repository.calendarRepository.AgendarRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgendarViewModel @Inject constructor(
    private val repository: AgendarRepository
) : ViewModel() {

    private val _horariosPendientes = MutableLiveData<List<String>>()
    val horariosPendientes: LiveData<List<String>> get() = _horariosPendientes


    private val _horariosDisponibles = MutableLiveData<List<Map<String, Any>>>()
    val horariosDisponibles: LiveData<List<Map<String, Any>>> get() = _horariosDisponibles

    private val _estadoAgendar = MutableLiveData<String>()
    val estadoAgendar: LiveData<String> get() = _estadoAgendar

    // Cargar los horarios disponibles para un psicólogo por su ID
    fun cargarHorariosDisponibles(psychoId: String) {
        viewModelScope.launch {
            try {
                val horarios = repository.obtenerDisponibilidadPorPsychoId(psychoId)
                _horariosDisponibles.postValue(horarios)
            } catch (e: Exception) {
                _estadoAgendar.postValue("Error al cargar horarios: ${e.message}")
            }
        }
    }

    fun agendarHorario(
        availabilityId: String,
        patientId: String,
        psychoId: String,
        modalidad: String
    ) {
        viewModelScope.launch {
            try {
                // Llamada a la función de agendar del repository
                val exito = repository.agendarHorario(availabilityId, patientId, psychoId, modalidad)

                if (exito) {
                    // Estado de éxito
                    _estadoAgendar.postValue("Horario reservado con éxito.")
                } else {
                    // Estado de error si no se puede reservar
                    _estadoAgendar.postValue("Error al reservar el horario. Puede estar ocupado.")
                }
            } catch (e: Exception) {
                // Manejo de errores generales
                Log.e("AgendarViewModel", "Error al intentar agendar el horario: ${e.message}")
                if (e.message?.contains("El horario ya no está disponible") == true) {
                    _estadoAgendar.postValue("El horario ya no está disponible.")
                } else {
                    _estadoAgendar.postValue("Error al intentar reservar el horario: ${e.message}")
                }
            }
        }
    }


    fun actualizarDisponibilidad(availabilityId: String) {
        Log.d("ActualizarDisponibilidad", "availabilityId: $availabilityId")

        // Buscar el documento en la colección "availability" usando availabilityId
        FirebaseFirestore.getInstance()
            .collection("availability")
            .whereEqualTo("availabilityId", availabilityId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()

                    // Actualizar el estado de disponibilidad a "reservado"
                    document.reference.update("estado", "reservado")
                        .addOnSuccessListener {
                            Log.d("ActualizarDisponibilidad", "Disponibilidad actualizada a 'reservado'")
                            // Después de actualizar, recargar los horarios
                            cargarHorariosDisponibles(document["psychoId"] as String)
                        }
                        .addOnFailureListener { e ->
                            Log.e("ActualizarDisponibilidad", "Error al actualizar la disponibilidad", e)
                        }
                } else {
                    Log.e("ActualizarDisponibilidad", "No se encontró el horario con availabilityId: $availabilityId")
                }
            }
            .addOnFailureListener { e ->
                Log.e("ActualizarDisponibilidad", "Error al buscar el horario", e)
            }
    }

    fun obtenerHorariosPendientes(psychoId: String) {
        viewModelScope.launch {
            val horarios = repository.obtenerHorariosPendientes(psychoId)
            _horariosPendientes.postValue(horarios) // Actualiza el estado con los horarios obtenidos
        }
    }
}

