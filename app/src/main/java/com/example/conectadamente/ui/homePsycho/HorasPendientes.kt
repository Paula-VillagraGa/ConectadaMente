package com.example.conectadamente.ui.homePsycho

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.conectadamente.ui.viewModel.calendar.AppointmentViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservedAppointmentsScreen(
    viewModel: AppointmentViewModel,
    navController: NavController // Agregamos el controlador de navegación
) {
    // Observar las citas pendientes desde el ViewModel
    val citasPendientes by viewModel.citasPendientes.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState("")

    // Obtenemos las citas pendientes
    LaunchedEffect(true) {
        viewModel.obtenerCitasPendientes("psychoIdExample") // Aquí puedes usar el psychoId real
    }

    // Si hay un mensaje de error, lo mostramos
    if (errorMessage.isNotEmpty()) {
        Text(text = "Error: $errorMessage", color = Color.Red, modifier = Modifier.padding(16.dp))
    }

    // Contenido de la pantalla dentro de un Scaffold
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Citas Pendientes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Acción de volver
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(16.dp)) {
                // LazyColumn para la lista de citas
                LazyColumn(contentPadding = paddingValues) {
                    items(citasPendientes) { cita ->
                        val (fechaHora, paciente) = cita
                        ReservedAppointmentCard(fechaHora, paciente)
                    }
                }
            }
        }
    )
}

@Composable
fun ReservedAppointmentCard(fechaHora: String, paciente: String?) {
    // Card para mostrar la cita
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Fecha y Hora: $fechaHora",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Paciente: ${paciente ?: "Paciente no disponible"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
