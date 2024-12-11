package com.example.conectadamente.ui.viewModel.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.repository.calendarRepository.AgendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgendarViewModel @Inject constructor(
    private val repository: AgendarRepository
) : ViewModel() {

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

    // Agendar un horario, incluyendo la actualización del estado y la creación de la cita en la colección de appointments
    fun agendarHorario(availabilityId: String, patientId: String, psychoId: String) {
        viewModelScope.launch {
            try {
                val exito = repository.agendarHorario(availabilityId, patientId, psychoId)
                if (exito) {
                    _estadoAgendar.postValue("Horario reservado con éxito.")
                } else {
                    _estadoAgendar.postValue("Error al reservar el horario. Puede estar ocupado.")
                }
            } catch (e: Exception) {
                _estadoAgendar.postValue("Error al intentar reservar el horario: ${e.message}")
            }
        }
    }
}
