package com.example.conectadamente.ui.viewModel.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.Disponibilidad
import com.example.conectadamente.data.repository.calendarRepository.DisponibilidadRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisponibilidadViewModel @Inject constructor(
    private val repository: DisponibilidadRepositorio
) : ViewModel() {

    private val _estado = MutableStateFlow("")
    val estado: StateFlow<String> = _estado

    private val _horasDisponibles = MutableStateFlow<List<Disponibilidad>>(emptyList())
    val horasDisponibles: StateFlow<List<Disponibilidad>> = _horasDisponibles

    // Función para obtener el ID del usuario actual
    fun obtenerIdUsuarioActual(): String {
        return repository.obtenerIdUsuarioActual()
    }

    // Función para cargar las horas habilitadas para una fecha específica
    fun cargarHorasDisponibles(fecha: String, psychoId: String) {
        viewModelScope.launch {
            try {
                val horas = repository.cargarHorasDisponibles(fecha, psychoId)
                _horasDisponibles.value = horas // Actualizamos la lista de horas disponibles
            } catch (e: Exception) {
                _estado.value = "Error al cargar horas: ${e.message}"
            }
        }
    }

    // Función para cambiar el estado de la hora
    fun cambiarEstadoHora(fecha: String, hora: String, psychoId: String, estadoActual: String) {
        viewModelScope.launch {
            try {
                // Cambiar el estado de la hora en la base de datos
                repository.cambiarEstadoHora(fecha, hora, psychoId, estadoActual)

                // Actualizar el estado de la hora en la lista local
                _horasDisponibles.value = _horasDisponibles.value.map {
                    if (it.hora == hora) {
                        it.copy(estado = estadoActual) // Cambiar el estado de la hora específica
                    } else {
                        it // Mantener el estado de las otras horas igual
                    }
                }

                _estado.value = "Estado de la hora actualizado con éxito"
            } catch (e: Exception) {
                _estado.value = "Error al cambiar el estado: ${e.message}"
            }
        }
    }

    fun guardarDisponibilidad(fecha: String, hora: String, psychoId: String) {
        // Validación para asegurarse de que la fecha no esté vacía
        if (fecha.isEmpty()) {
            _estado.value = "Por favor, selecciona una fecha"
            return
        }

        viewModelScope.launch {
            try {
                // Convertimos 'hora' en una lista de un solo elemento
                val result = repository.guardarDisponibilidad(fecha, listOf(hora), psychoId, "disponible")
                _estado.value = result
            } catch (e: Exception) {
                _estado.value = "Error al guardar disponibilidad: ${e.message}"
            }
        }
    }


    // Función para eliminar la disponibilidad de una hora
    fun eliminarDisponibilidad(fecha: String, hora: String, psychoId: String) {
        viewModelScope.launch {
            try {
                repository.eliminarDisponibilidad(fecha, hora, psychoId)
                _estado.value = "Disponibilidad eliminada con éxito"
            } catch (e: Exception) {
                _estado.value = "Error al eliminar disponibilidad: ${e.message}"
            }
        }
    }



    // Función para mostrar mensajes de error
    fun mostrarError(mensaje: String) {
        _estado.value = mensaje
    }
}
