package com.example.conectadamente.ui.homeUser.calendar

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.conectadamente.ui.theme.Purple10
import com.example.conectadamente.ui.viewModel.calendar.AgendarViewModel


@SuppressLint("RememberReturnType")
@Composable
fun AgendarScreen(
    viewModel: AgendarViewModel = hiltViewModel(),
    psychoId: String,
    patientId: String
) {
    // Estado de la disponibilidad y el estado de agendar
    val horariosDisponibles = viewModel.horariosDisponibles.observeAsState(emptyList())
    val estadoAgendar = viewModel.estadoAgendar.observeAsState("")

    // Cargar horarios disponibles para el psicólogo
    remember { viewModel.cargarHorariosDisponibles(psychoId) }

    // Estado de la fecha seleccionada
    var fechaSeleccionada by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Horarios disponibles",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Calendario o selector de fechas
        CalendarView(
            horariosDisponibles = horariosDisponibles.value,
            onDateSelected = { date ->
                fechaSeleccionada = date
            }
        )

        // Mostrar los horarios disponibles de la fecha seleccionada
        fechaSeleccionada?.let { selectedDate ->
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(
                    horariosDisponibles.value.filter {
                        it["fecha"] == selectedDate && it["estado"] == "disponible"
                    }
                ) { horario ->
                    HorarioItem(
                        horario = horario,
                        onAgendarClick = { availabilityId ->
                            viewModel.agendarHorario(availabilityId, patientId, psychoId)
                        }
                    )
                }
            }
        }

        // Mostrar el estado de la acción de agendar
        if (estadoAgendar.value.isNotEmpty()) {
            Text(
                text = estadoAgendar.value,
                style = MaterialTheme.typography.bodyMedium,
                color = if (estadoAgendar.value.contains("éxito", ignoreCase = true)) Color.Green else Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CalendarView(horariosDisponibles: List<Map<String, Any>>, onDateSelected: (String) -> Unit) {
    // Extraer las fechas disponibles
    val uniqueDates = horariosDisponibles
        .map { it["fecha"] as? String ?: "" }
        .distinct()
        .sorted()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        items(uniqueDates) { date ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onDateSelected(date) },
                shape = MaterialTheme.shapes.small,
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun HorarioItem(horario: Map<String, Any>, onAgendarClick: (String) -> Unit) {
    val fecha = horario["fecha"] as? String ?: ""
    val horaInicio = horario["horaInicio"] as? String ?: ""
    val horaFin = horario["horaFin"] as? String ?: ""
    val availabilityId = horario["id"] as? String ?: ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Fecha: $fecha", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Hora: $horaInicio - $horaFin", style = MaterialTheme.typography.bodyMedium)
            }
            Button(
                onClick = { onAgendarClick(availabilityId) },
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(containerColor = Purple10)
            ) {
                Text("Agendar", color = Color.White)
            }
        }
    }
}
