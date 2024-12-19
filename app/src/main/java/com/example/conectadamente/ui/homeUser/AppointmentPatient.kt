package com.example.conectadamente.ui.homeUser

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.conectadamente.ui.viewModel.calendar.AppointmentViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppointmentPatientScreen(viewModel: AppointmentViewModel, psychoId: String) {
    val appointments by viewModel.citasPendientes.observeAsState()
    val psychologists by viewModel.psychologists.collectAsState(emptyMap())

    val patientId = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(patientId) {
        viewModel.obtenerCitasDelPaciente(patientId!!.uid)
    }


    Column(modifier = Modifier.padding(16.dp)) {
        if (appointments.isNullOrEmpty()) {
            // Mostrar mensaje si no hay citas agendadas
            Text(
                text = "No tienes citas agendadas.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                textAlign = TextAlign.Center
            )
        } else {
            // Mostrar las citas si hay alguna
            appointments?.forEach { appointment ->
                val psychologist = psychologists[appointment.psychoId]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Fecha: ${appointment.fecha}")
                        Text("Hora: ${appointment.hora}")
                        Text("Estado: ${appointment.estado}")

                        if (psychologist != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = rememberImagePainter(psychologist.photoUrl),
                                    contentDescription = "Foto del psicólogo",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Psicólogo: ${psychologist.name}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        if (appointment.estado == "Pendiente") {
                            Button(
                                onClick = {
                                    viewModel.cancelarCita(appointment.appointmentId, appointment.availabilityId,
                                        patientId = patientId!!.uid
                                    )
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Cancelar Cita")
                            }
                        }
                    }
                }
            }
        }
    }
}
