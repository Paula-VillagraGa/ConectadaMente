package com.example.conectadamente.ui.homeUser

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.conectadamente.data.model.Appointment
import com.example.conectadamente.ui.theme.Purple80
import com.example.conectadamente.ui.viewModel.calendar.AppointmentViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentPatientScreen(
    viewModel: AppointmentViewModel,
    psychoId: String,
    navController: NavController
) {
    val appointments by viewModel.citasDelPaciente.collectAsState()
    val psychologists by viewModel.psychologists.collectAsState(emptyMap())

    val patientId = FirebaseAuth.getInstance().currentUser
    var showDialog by remember { mutableStateOf(false) } // Estado para controlar el diálogo de confirmación
    var appointmentToCancel by remember { mutableStateOf<Appointment?>(null) } // La cita seleccionada para cancelar

    LaunchedEffect(patientId) {
        viewModel.obtenerCitasDelPaciente(patientId!!.uid)
    }

    Scaffold(
        modifier = Modifier.padding(0.dp),
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("Mis Consultas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Regresar",
                            tint = Purple80
                        )
                    }
                })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                if (appointments.isNullOrEmpty()) {
                    Text(
                        text = "No tienes citas agendadas.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(appointments) { appointment ->
                            val psychologist = psychologists[appointment.psychoId]

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    if (psychologist != null) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                painter = rememberImagePainter(psychologist.photoUrl),
                                                contentDescription = "Foto del psicólogo",
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Psicólogo: ${psychologist.name}",
                                                style = MaterialTheme.typography.bodyLarge,
                                                modifier = Modifier.weight(1f),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }

                                    Text(
                                        "Fecha: ${appointment.fecha}",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        "Hora: ${appointment.hora}",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        "Estado: ${appointment.estado.replaceFirstChar { it.uppercase() }}",
                                        color = MaterialTheme.colorScheme.secondary
                                    )

                                    if (appointment.estado.equals("pendiente", ignoreCase = true)) {
                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Botón de cancelar cita
                                        Button(
                                            onClick = {
                                                // Al seleccionar cancelar, guardamos la cita a cancelar
                                                appointmentToCancel = appointment
                                                showDialog = true
                                            },
                                            modifier = Modifier
                                                .padding(top = 8.dp)
                                                .fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = Color.White
                                            ),
                                            shape = MaterialTheme.shapes.small
                                        ) {
                                            Text(
                                                text = "Cancelar Cita",
                                                style = MaterialTheme.typography.bodySmall,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Mostrar el diálogo de confirmación
            if (showDialog && appointmentToCancel != null) {
                AlertDialog(
                    onDismissRequest = { showDialog = false }, // Cierra el diálogo si se toca fuera
                    title = { Text("Confirmar Cancelación") },
                    text = { Text("¿Estás seguro de que deseas cancelar esta cita?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                // Realizamos la cancelación con la cita seleccionada
                                val appointment = appointmentToCancel
                                if (appointment != null) {
                                    viewModel.cancelarCita(
                                        appointment.appointmentId, appointment.availabilityId,
                                        patientId = patientId!!.uid
                                    )
                                }
                                showDialog = false // Cerrar el diálogo
                            }
                        ) {
                            Text("Confirmar")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false } // Cierra el diálogo sin hacer nada
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    )
}
