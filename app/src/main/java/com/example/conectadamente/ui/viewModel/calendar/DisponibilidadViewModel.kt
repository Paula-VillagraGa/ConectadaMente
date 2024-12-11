package com.example.conectadamente.ui.viewModel.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // Función para obtener el ID del usuario actual
    fun obtenerIdUsuarioActual(): String {
        return repository.obtenerIdUsuarioActual()
    }

    fun mostrarDatePicker(onFechaSeleccionada: (String) -> Unit) {
        // Aquí puedes usar un DatePicker en la Activity para mostrar un selector de fecha
        // Pasa la fecha seleccionada al callback `onFechaSeleccionada`
    }

    fun guardarDisponibilidad(fecha: String, inicioHora: String, finHora: String, userId: String) {
        viewModelScope.launch {
            try {
                repository.guardarDisponibilidad(fecha, inicioHora, finHora, userId)
                _estado.value = "Disponibilidad guardada exitosamente"
            } catch (e: Exception) {
                _estado.value = "Error al guardar disponibilidad: ${e.message}"
            }
        }
    }
}
