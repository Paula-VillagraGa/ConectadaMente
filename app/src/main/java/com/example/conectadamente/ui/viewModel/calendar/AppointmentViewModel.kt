package com.example.conectadamente.ui.viewModel.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.repository.calendarRepository.AppointmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository // Inyectamos el repositorio de citas
) : ViewModel() {

    private val _citasPendientes = MutableLiveData<List<Pair<String, String?>>>(emptyList())
    val citasPendientes: LiveData<List<Pair<String, String?>>> = _citasPendientes

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // Nuevo MutableLiveData para el estado de carga
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // Obtener las citas pendientes con los nombres de los pacientes para un psicólogo
    fun obtenerCitasPendientes(psychoId: String) {
        _isLoading.value = true // Se inicia la carga
        viewModelScope.launch {
            try {
                val citas =
                    appointmentRepository.obtenerCitasPendientesYPacientesPorPsychoId(psychoId)
                _citasPendientes.value = citas
            } catch (e: Exception) {
                // Manejo de error
                Log.e("AppointmentViewModel", "Error al obtener citas pendientes: ${e.message}")
                _errorMessage.value = "Error al obtener citas pendientes."
            } finally {
                _isLoading.value = false // Se termina la carga, ya sea con éxito o con error
            }
        }
    }

    // Cambiar el estado de una cita (por ejemplo, cancelada o realizada)
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
}
