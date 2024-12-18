package com.example.conectadamente.ui.homePsycho

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.conectadamente.data.repository.calendarRepository.CompletedAppointment
import com.example.conectadamente.ui.viewModel.calendar.AppointmentViewModel
import com.example.conectadamente.utils.extensions.formatRut
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedAppointmentsScreen(viewModel: AppointmentViewModel) {
    val appointments by viewModel.appointments.collectAsState() // Aquí usamos collectAsState
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()


    val currentPsychoId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Fetch appointments cuando la pantalla se carga
    LaunchedEffect(Unit) {
        viewModel.fetchCompletedAppointments(currentPsychoId)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("Mis Pacientes") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            if (appointments.isEmpty() && !isLoading) {
                Text(
                    text = "No hay citas realizadas.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(appointments) { appointment ->
                        CompletedAppointmentItem(appointment)
                    }
                }
            }
        }
    }
}
@Composable
fun CompletedAppointmentItem(appointment: CompletedAppointment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Paciente: ${appointment.patientName}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Rut: ${formatRut(appointment.rut)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "Fecha: ${appointment.fecha}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Hora: ${appointment.hora}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (appointment.observaciones.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Observaciones: ${appointment.observaciones}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
