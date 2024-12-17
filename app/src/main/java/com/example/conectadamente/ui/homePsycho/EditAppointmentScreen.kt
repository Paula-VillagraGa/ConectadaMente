package com.example.conectadamente.data.repository.calendarRepository

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    appointmentId: String,
    navController: NavController
) {
    var nuevoEstado by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.observeAsState("")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Cita") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nuevoEstado,
                onValueChange = { nuevoEstado = it },
                label = { Text("Nuevo Estado") },
                placeholder = { Text("Ej: cancelada, realizada") }
            )

            OutlinedTextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text("Observaciones") },
                placeholder = { Text("Escribe observaciones aquí...") }
            )

            Button(
                onClick = {
                    viewModel.actualizarEstadoCitaConObservaciones(
                        appointmentId, nuevoEstado, observaciones
                    )
                    navController.popBackStack()
                },
                enabled = nuevoEstado.isNotBlank()
            ) {
                Text("Guardar Cambios")
            }

            if (errorMessage.isNotEmpty()) {
                Text("Error: $errorMessage", color = Color.Red)
            }
        }
    }
}
