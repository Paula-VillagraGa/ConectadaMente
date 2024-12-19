package com.example.conectadamente.ui.viewModel.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.Appointment
import com.example.conectadamente.data.repository.calendarRepository.AppointmentRepository
import com.example.conectadamente.data.repository.calendarRepository.CompletedAppointment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {

    private val _appointments = MutableStateFlow<List<CompletedAppointment>>(emptyList())
    val appointments: StateFlow<List<CompletedAppointment>> = _appointments

    private val _citasPendientes = MutableLiveData<List<Appointment>>(emptyList())
    val citasPendientes: LiveData<List<Appointment>> = _citasPendientes

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun obtenerCitasPendientes(psychoId: String) {
        _isLoading.value = true // Se inicia la carga
        viewModelScope.launch {
            try {
                val citas = appointmentRepository.obtenerCitasPendientesYPacientesPorPsychoId(psychoId)
                _citasPendientes.value = citas
            } catch (e: Exception) {
                Log.e("AppointmentViewModel", "Error al obtener citas pendientes: ${e.message}")
                _errorMessage.value = "Error al obtener citas pendientes."
            } finally {
                _isLoading.value = false // Se termina la carga, ya sea con éxito o con error
            }
        }
    }

    fun actualizarEstadoCita(appointmentId: String, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                val success = appointmentRepository.actualizarEstadoCita(appointmentId, nuevoEstado)
                if (success) {
                    // Actualiza las citas pendientes después de cambiar el estado
                    obtenerCitasPendientes(appointmentId) // Opcional, depende de la lógica de tu app
                } else {
                    _errorMessage.value = "Error al actualizar el estado de la cita."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar el estado de la cita."
            }
        }
    }

    fun actualizarEstadoCitaConObservaciones(
        appointmentId: String,
        nuevoEstado: String,
        observaciones: String,
        recomendaciones: String
    ) {
        viewModelScope.launch {
            try {
                val success = appointmentRepository.actualizarEstadoCitaConObservaciones(
                    appointmentId, nuevoEstado, observaciones, recomendaciones
                )
                if (!success) {
                    _errorMessage.value = "Error al actualizar la cita."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
        }
    }
    // Obtener citas completadas para un psicólogo (mostrando paciente, rut, fecha, hora, observaciones)
    fun fetchCompletedAppointments(currentPsychoId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = appointmentRepository.getCompletedAppointments(currentPsychoId)
                _appointments.value = result
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar las citas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
