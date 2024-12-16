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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.conectadamente.ui.viewModel.calendar.DisponibilidadViewModel
import kotlinx.coroutines.delay
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale")
@Composable
fun AvailabilityScreen(viewModel: DisponibilidadViewModel, navController: NavController) {
    val fechaSeleccionada = remember { mutableStateOf("") }
    val horasDisponibles = viewModel.horasDisponibles.collectAsState().value // Observando las horas disponibles
    val estado = viewModel.estado.collectAsState().value // Observando el estado global de la operación
    val psychoId = viewModel.obtenerIdUsuarioActual()

    // Mensajes de estado
    val guardando = estado == "Guardando"
    val guardadoCorrectamente = estado == "Guardado correctamente"

    val mostrarMensajeFecha = remember { mutableStateOf(false) } // Para mostrar el mensaje de fecha obligatoria
    val mensajeEstado = remember { mutableStateOf("") } // Para mostrar el mensaje dinámico según el estado de la hora

    // Lista de horas posibles (de 8:00 a 19:00)
    val horasPosibles = (8..19).map { String.format("%02d:00", it) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Disponibilidad") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        // Fondo y espaciado general
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Título de la pantalla
            Text(
                text = "Gestiona tu Agenda Disponible",
                style = MaterialTheme.typography.titleMedium,
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
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título para selección de horarios
            Text(
                text = "Selecciona los horarios disponibles:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

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
            if (guardando) {
                Text(
                    text = "Guardando...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            if (guardadoCorrectamente) {
                Text(
                    text = "Guardado correctamente",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Ocultar el mensaje después de 2 segundos
                LaunchedEffect(guardadoCorrectamente) {
                    delay(2000)
                    viewModel.limpiarEstado()
                }
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
