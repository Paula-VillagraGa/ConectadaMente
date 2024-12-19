package com.example.conectadamente.ui.viewModel.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.Appointment
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.data.repository.calendarRepository.AppointmentRepository
import com.example.conectadamente.data.repository.calendarRepository.CompletedAppointment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {


    private val _citasPendientes = MutableLiveData<List<Appointment>>(emptyList())
    val citasPendientes: LiveData<List<Appointment>> = _citasPendientes

    // Estados observables
    private val _appointments = MutableStateFlow<List<CompletedAppointment>>(emptyList())
    val appointments: StateFlow<List<CompletedAppointment>> = _appointments

    private val _citasDelPaciente = MutableStateFlow<List<Appointment>>(emptyList())
    val citasDelPaciente: StateFlow<List<Appointment>> = _citasDelPaciente

    private val _pendingAppointments = MutableStateFlow<List<Appointment>>(emptyList())
    val pendingAppointments: StateFlow<List<Appointment>> = _pendingAppointments

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _psychologists = MutableStateFlow<Map<String, PsychoModel>>(emptyMap())
    val psychologists: StateFlow<Map<String, PsychoModel>> = _psychologists


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

    private fun loadPsychologists(psychoIds: List<String>) {
        viewModelScope.launch {
            try {
                val psychologistsMap = appointmentRepository.getPsychologistsByIds(psychoIds)
                _psychologists.value = psychologistsMap
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener los datos de los psicólogos."
            }
        }
    }

    // Obtener citas del paciente autenticado
    fun obtenerCitasDelPaciente(patientId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val citas = appointmentRepository.obtenerCitasDelPaciente(patientId)
                _citasDelPaciente.value = citas
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener citas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Cancelar cita
    fun cancelarCita(appointmentId: String, availabilityId: String, patientId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val success = appointmentRepository.cancelarCitaYActualizar(patientId, appointmentId, availabilityId)
                if (success) {
                    obtenerCitasDelPaciente(patientId) // Actualiza la lista de citas
                } else {
                    _errorMessage.value = "Error al cancelar la cita."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cancelar la cita: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}


