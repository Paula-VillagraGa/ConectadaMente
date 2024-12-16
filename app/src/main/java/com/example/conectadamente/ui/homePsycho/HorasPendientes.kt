package com.example.conectadamente.ui.homePsycho

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    val isLoading by viewModel.isLoading.observeAsState(false) // Estado para la carga

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
                title = { Text("Citas Pendientes", color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Acción de volver
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Si está cargando, mostrar el círculo de carga centrado
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.Center) // Centrado en el centro de la pantalla
                            .padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 10.dp, // Margen izquierdo
                            top = 16.dp,   // Margen superior
                            end = 16.dp,   // Margen derecho
                            bottom = 16.dp // Margen inferior
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues) // Asegura que el contenido de LazyColumn no se corte
                    ) {
                        items(citasPendientes) { cita ->
                            val (fechaHora, paciente) = cita
                            ReservedAppointmentCard(fechaHora, paciente)
                        }
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
            // Fecha y hora separados
            val (fecha, hora) = fechaHora.split(" ") // Suponiendo que la fecha y hora están separadas por un espacio
            Row {
                Text(
                    text = "Fecha: $fecha",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp)) // Un pequeño espacio entre la fecha y la hora
            Row {
                Text(
                    text = "Hora: $hora",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Paciente: ${paciente ?: "Paciente no disponible"}",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}