package com.example.conectadamente.ui.homePsycho

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.conectadamente.ui.viewModel.calendar.DisponibilidadViewModel
import kotlinx.coroutines.delay
import java.util.Calendar

@SuppressLint("DefaultLocale")
@Composable
fun DisponibilidadScreen(viewModel: DisponibilidadViewModel) {
    val fechaSeleccionada = remember { mutableStateOf("") }
    val horasDisponibles = viewModel.horasDisponibles.collectAsState().value // Observando las horas disponibles
    val estado = viewModel.estado.collectAsState().value // Observando el estado global de la operación
    val psychoId = viewModel.obtenerIdUsuarioActual()
    val guardando = remember { mutableStateOf(false) } // Para mostrar el mensaje "Guardando..."
    val guardadoCorrectamente = remember { mutableStateOf(false) } // Para mostrar "Guardado correctamente"
    val mostrarMensajeFecha = remember { mutableStateOf(false) } // Para mostrar el mensaje de fecha obligatoria
    val mensajeEstado = remember { mutableStateOf("") } // Para mostrar el mensaje dinámico según el estado de la hora

    // Lista de horas posibles (de 8:00 a 19:00)
    val horasPosibles = (8..19).map { String.format("%02d:00", it) }

    // Fondo y espaciado general
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Título de la pantalla
        Text(
            text = "Configura tu disponibilidad",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Selección de fecha
        SeleccionFecha(fechaSeleccionada) { fecha ->
            fechaSeleccionada.value = fecha
            viewModel.cargarHorasDisponibles(fecha, psychoId)
            mostrarMensajeFecha.value = false
        }

        // Mostrar mensaje si no se ha seleccionado la fecha
        if (mostrarMensajeFecha.value) {
            Text(
                text = "Debe seleccionar una fecha primero",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mostrar botones para cada hora en filas de tres
        Column {
            horasPosibles.chunked(3).forEachIndexed { index, horaChunk ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    horaChunk.forEach { hora ->
                        // Buscar el estado de la hora en horasDisponibles
                        val horaEstado = horasDisponibles.find { it.hora == hora }?.estado ?: "no disponible"

                        Button(
                            onClick = {
                                if (fechaSeleccionada.value.isEmpty()) {
                                    mostrarMensajeFecha.value = true
                                    return@Button
                                }

                                // Verifica si la hora está documentada
                                val horaExistente = horasDisponibles.find { it.hora == hora }

                                if (horaExistente == null) {
                                    // Caso: La hora no está documentada (nueva)
                                    viewModel.guardarDisponibilidad(fechaSeleccionada.value, hora, psychoId)
                                } else {
                                    // Caso: La hora ya está documentada (cambiar estado)
                                    val nuevoEstado = if (horaExistente.estado == "disponible") "no disponible" else "disponible"
                                    viewModel.cambiarEstadoHora(fechaSeleccionada.value, hora, psychoId, nuevoEstado)
                                }

                                // Mostrar mensajes de guardado
                                guardando.value = true
                                guardadoCorrectamente.value = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = when (horaEstado) {
                                    "disponible" -> MaterialTheme.colorScheme.primary
                                    "no disponible" -> Color.Transparent
                                    else -> Color.Transparent // Para horas nuevas
                                },
                                contentColor = if (horaEstado == "disponible") Color.White else MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = hora,
                                color = if (horaEstado == "disponible") Color.White else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Espacio entre las filas
                if (index < horasPosibles.chunked(3).size - 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Mostrar el estado del proceso de guardado
        if (guardando.value) {
            Text(
                text = "",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (guardadoCorrectamente.value) {
            Text(
                text = "",
                color = Color.Green,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        // Mostrar el mensaje dinámico de acuerdo al estado
        if (mensajeEstado.value.isNotEmpty()) {
            Text(
                text = mensajeEstado.value,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }

    // Ejecutar el delay y ocultar el mensaje "Guardado correctamente" después de un tiempo
    if (guardadoCorrectamente.value) {
        LaunchedEffect(Unit) {
            delay(2000) // 2 segundos
            guardadoCorrectamente.value = false
        }
    }

    // Mostrar mensaje de estado de la operación
    LaunchedEffect(estado) {
        if (estado == "Estado de la hora actualizado con éxito") {
            guardando.value = false
            guardadoCorrectamente.value = true
        }
    }
}

@Composable
fun SeleccionFecha(fechaSeleccionada: MutableState<String>, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Abre el DatePickerDialog cuando se haga clic
    val openDatePicker = {
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Al seleccionar una fecha, la asignamos al campo
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                fechaSeleccionada.value = selectedDate
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    Column {
        Text(
            text = "Selecciona una fecha:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Button(
            onClick = openDatePicker,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = if (fechaSeleccionada.value.isEmpty()) "Seleccionar fecha" else fechaSeleccionada.value,
                color = Color.White
            )
        }
    }
}
