package com.example.conectadamente.ui.homePsycho

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var recomendaciones by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Cita", color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface), // Fondo suave
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Mostrar los detalles de la cita
            val (fecha, hora) = fechaHora.split(" ")
            Text("Fecha: $fecha", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text("Hora: $hora", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text("Paciente: ${paciente.ifBlank { "Paciente no disponible" }}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // DropdownMenuBox
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .background(Color.Transparent)
            ) {
                // Aquí solo aplicamos el borde al OutlinedTextField
                OutlinedTextField(
                    value = nuevoEstado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nuevo Estado") },
                    placeholder = { Text("Selecciona un estado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .clip(RoundedCornerShape(10.dp))
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(Color.Transparent)
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
            OutlinedTextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text("Observaciones", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)) },
                placeholder = {
                    Text(
                        "Escribe observaciones aquí...",
                        style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.secondary)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Altura personalizada para hacerlo más grande
                    .clip(RoundedCornerShape(10.dp)), // Bordes más redondeados
                shape = RoundedCornerShape(10.dp), // Bordes redondeados
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                textStyle = TextStyle(fontSize = 18.sp), // Tamaño del texto dentro del campo
                maxLines = 5 // Permite varias líneas
            )

            // Agregar el nuevo campo "Recomendaciones" (opcional)
            OutlinedTextField(
                value = recomendaciones,
                onValueChange = { recomendaciones = it },
                label = { Text("Recomendaciones para el paciente", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)) },
                placeholder = {
                    Text(
                        "Escribe las recomendaciones aquí...",
                        style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.secondary)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Altura personalizada para hacerlo más grande
                    .clip(RoundedCornerShape(10.dp)), // Bordes más redondeados
                shape = RoundedCornerShape(10.dp), // Bordes redondeados
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                textStyle = TextStyle(fontSize = 18.sp),
                maxLines = 5
            )


            Button(
                onClick = {
                    viewModel.actualizarEstadoCitaConObservaciones(
                        appointmentId, nuevoEstado, observaciones, recomendaciones
                    )
                    navController.popBackStack()
                },
                enabled = nuevoEstado.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)), // Bordes redondeados para el botón
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar Cambios", color = MaterialTheme.colorScheme.onPrimary)
            }

            if (errorMessage?.isNotEmpty() == true) {
                Text("Error: $errorMessage", color = Color.Red)
            }
        }
    }
}

