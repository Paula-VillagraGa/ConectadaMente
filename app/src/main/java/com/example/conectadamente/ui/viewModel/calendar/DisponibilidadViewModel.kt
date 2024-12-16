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
    fun cambiarEstadoHora(fecha: String, hora: String, psychoId: String, nuevoEstado: String) {
        viewModelScope.launch {
            _estado.value = "Guardando"
            try {
                // Verifica si la hora existe en la lista actual
                val horaExistente = _horasDisponibles.value.find { it.hora == hora }

                if (horaExistente != null) {
                    // Cambiar el estado actual de "disponible" a "no disponible" o viceversa
                    val nuevoEstado = if (horaExistente.estado == "disponible") "no disponible" else "disponible"

                    // Actualizar en la base de datos
                    repository.cambiarEstadoHora(fecha, hora, psychoId, nuevoEstado)

                    // Actualizar el estado local
                    _horasDisponibles.value = _horasDisponibles.value.map {
                        if (it.hora == hora) it.copy(estado = nuevoEstado) else it
                    }
                } else {
                    // Si no existe, se guarda como disponible por defecto
                    guardarDisponibilidad(fecha, hora, psychoId)
                }

                _estado.value = "Guardado correctamente"
            } catch (e: Exception) {
                _estado.value = "Error al cambiar el estado: ${e.message}"
            }
        }
    }

    fun guardarDisponibilidad(fecha: String, hora: String, psychoId: String) {
        if (fecha.isEmpty()) {
            _estado.value = "Por favor, selecciona una fecha"
            return
        }

        viewModelScope.launch {
            _estado.value = "Guardando"
            try {
                // Convertimos 'hora' en una lista de un solo elemento
                val availabilityIds = repository.guardarDisponibilidad(fecha, listOf(hora), psychoId, "disponible")

                // Crear objetos Disponibilidad para cada hora guardada
                val nuevasDisponibilidades = availabilityIds.map { availabilityId ->
                    Disponibilidad(
                        availabilityId = availabilityId,
                        fecha = fecha,
                        hora = hora,
                        estado = "disponible",
                        psychoId = psychoId
                    )
                }

                // Actualizamos la lista local
                _horasDisponibles.value = _horasDisponibles.value + nuevasDisponibilidades

                _estado.value = "Guardado correctamente"
            } catch (e: Exception) {
                _estado.value = "Error al guardar disponibilidad: ${e.message}"
            }
        }
    }

    // Función para limpiar el estado después de mostrar un mensaje
    fun limpiarEstado() {
        _estado.value = ""
    }
}
