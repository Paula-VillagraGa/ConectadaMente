package com.example.conectadamente.ui.homePsycho

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.conectadamente.R
import com.example.conectadamente.ui.viewModel.calendar.AppointmentViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservedAppointmentsScreen(
    viewModel: AppointmentViewModel,
    navController: NavController // Agregamos el controlador de navegación
) {
    val citasPendientes by viewModel.citasPendientes.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(false)

    val fechaActual = LocalDate.now()


    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        viewModel.obtenerCitasPendientes("psychoIdExample") // Aquí puedes usar el psychoId real
    }

    if (errorMessage?.isNotEmpty() == true) {
        Text(
            text = "Error: $errorMessage",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )
    }


    val citasFiltradasPorFecha = citasPendientes.filter { cita ->
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val fechaCita = LocalDate.parse(cita.fecha.split(" ")[0], formatter)
        !fechaCita.isBefore(fechaActual)
    }


    val citasFiltradas = citasFiltradasPorFecha.sortedBy { cita ->
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        LocalDate.parse(cita.fecha.split(" ")[0], formatter)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Citas Pendientes", color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Acción de volver
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                // Campo de búsqueda
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar paciente") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)  // Padding horizontal para los bordes
                        .padding(bottom = 8.dp),  // Ajusta el espacio debajo del campo de búsqueda
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,  // Fondo transparente
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,  // No mostrar indicador al tener foco
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary  // No mostrar indicador cuando no tiene foco
                    ),
                    shape = MaterialTheme.shapes.medium  // Configura la forma, si lo deseas
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                start = 10.dp,
                                top = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(citasFiltradas) { cita ->
                                ReservedAppointmentCard(
                                    appointmentId = cita.appointmentId,
                                    fechaHora = cita.fechaHora,
                                    paciente = cita.paciente
                                ) { id ->
                                    navController.navigate(
                                        "editarCita/$id?fechaHora=${cita.fechaHora}&paciente=${cita.paciente ?: ""}"
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ReservedAppointmentCard(
    appointmentId: String, // Agregar el ID de la cita
    fechaHora: String,
    paciente: String?,
    onCardClick: (String) -> Unit // Callback para manejar la navegación
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { onCardClick(appointmentId) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.mosca),
                contentDescription = "Foto del usuario",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.padding(16.dp)) {
                val (fecha, hora) = fechaHora.split(" ")
                Text(
                    "Fecha: $fecha",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Hora: $hora",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Paciente: ${paciente ?: "Paciente no disponible"}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
