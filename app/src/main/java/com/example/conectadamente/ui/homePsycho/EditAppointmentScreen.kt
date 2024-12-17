package com.example.conectadamente.ui.homePsycho

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.conectadamente.ui.viewModel.calendar.AppointmentViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAppointmentScreen(
    viewModel: AppointmentViewModel,
    navController: NavController,
    appointmentId: String,
    fechaHora: String,
    paciente: String
) {

    // Opciones del estado
    val estadoOpciones = listOf("Realizada", "Cancelada")
    var expanded by remember { mutableStateOf(false) }
    var nuevoEstado by remember { mutableStateOf("") }

    var observaciones by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.observeAsState("")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Cita", color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Mostrar los detalles de la cita
            val (fecha, hora) = fechaHora.split(" ")
            Text("Fecha: $fecha", style = MaterialTheme.typography.bodyLarge)
            Text("Hora: $hora", style = MaterialTheme.typography.bodyLarge)
            Text("Paciente: ${paciente.ifBlank { "Paciente no disponible" }}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            // DropdownMenuBox
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                // Campo de texto solo lectura que actúa como un disparador para el menú desplegable
                OutlinedTextField(
                    value = nuevoEstado,
                    onValueChange = {}, // No permitimos edición manual
                    readOnly = true,
                    label = { Text("Nuevo Estado") },
                    placeholder = { Text("Selecciona un estado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor() // Necesario para anclar el menú correctamente
                )

                // Menú desplegable con opciones
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    estadoOpciones.forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado) },
                            onClick = {
                                nuevoEstado = estado // Asignar la opción seleccionada
                                expanded = false    // Cerrar el menú después de la selección
                            }
                        )
                    }
                }
            }

            // Campo para observaciones
            OutlinedTextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text("Observaciones") },
                placeholder = { Text("Escribe observaciones aquí...") }
            )

            // Botón para guardar cambios
            Button(
                onClick = {
                    viewModel.actualizarEstadoCitaConObservaciones(
                        appointmentId, nuevoEstado, observaciones
                    )
                    navController.popBackStack() // Volver atrás después de guardar
                },
                enabled = nuevoEstado.isNotBlank() // Solo habilitar el botón si el estado no está vacío
            ) {
                Text("Guardar Cambios")
            }

            // Mostrar mensaje de error si existe
            if (errorMessage.isNotEmpty()) {
                Text("Error: $errorMessage", color = Color.Red)
            }
        }
    }
}
