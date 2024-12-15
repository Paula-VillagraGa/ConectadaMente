package com.example.conectadamente.ui.homeUser.calendar

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.conectadamente.ui.theme.Purple10
import com.example.conectadamente.ui.viewModel.calendar.AgendarViewModel
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter


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

        CustomCalendar(
            availableDates = horariosDisponibles.value.map { it["fecha"] as String },
            onDateSelected = { date ->
                fechaSeleccionada = date
            }
        )
        fechaSeleccionada?.let { selectedDate ->
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(
                    horariosDisponibles.value
                        .filter { it["fecha"] == selectedDate && it["estado"] == "disponible" }
                        .sortedBy {
                            // Intentamos convertir la hora a LocalTime
                            val horaString = it["hora"] as? String ?: ""
                            try {
                                // Convertir la hora al formato LocalTime (formato "HH:mm")
                                LocalTime.parse(horaString, DateTimeFormatter.ofPattern("HH:mm"))
                            } catch (e: Exception) {
                                // En caso de error (formato incorrecto o nulo), asignamos la hora más tarde (23:59)
                                LocalTime.MAX
                            }
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
        val horaInicio = horario["hora"] as? String ?: ""
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
                    Text(text = "Hora: $horaInicio", style = MaterialTheme.typography.bodyMedium)
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
@Composable
fun CustomCalendar(
    availableDates: List<String>, // Lista de fechas en formato "dd/MM/yyyy"
    onDateSelected: (String) -> Unit
) {
    // Estado para el mes actual
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentMonth.lengthOfMonth()

    // Obtener el primer día del mes
    val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7 // 0: Domingo, 6: Sábado

    Column(modifier = Modifier.padding(16.dp)) {
        // Cabecera del calendario
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Mes anterior")
            }
            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }, // Primera letra en mayúscula
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary // Color primario
                )
            )
            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.Default.ArrowForwardIos, contentDescription = "Mes siguiente")
            }
        }
    }


    // Días de la semana
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom").forEach { day ->
            Text(
                text = day,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }

    // Días del mes
    val totalCells = firstDayOfMonth + daysInMonth
    val rows = (totalCells / 7) + if (totalCells % 7 > 0) 1 else 0

    Column {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (col in 0..6) {
                    val dayIndex = row * 7 + col
                    val dayNumber = dayIndex - firstDayOfMonth + 1

                    if (dayIndex < firstDayOfMonth || dayNumber > daysInMonth) {
                        Spacer(modifier = Modifier.weight(1f)) // Espacio vacío
                    } else {
                        val date = currentMonth.atDay(dayNumber)
                        val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        val isAvailable = formattedDate in availableDates

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isAvailable -> Color(0xFFB39DDB) // Morado claro para fechas disponibles
                                        else -> Color(0xFFEDE7F6) // Morado muy claro para días normales
                                    }
                                )
                                .clickable(enabled = isAvailable) { onDateSelected(formattedDate) }
                                .border(
                                    width = if (isAvailable) 2.dp else 1.dp,
                                    color = if (isAvailable) Color(0xFF7E57C2) else Color(0xFFD1C4E9),
                                    shape = CircleShape
                                )
                                .size(48.dp), // Tamaño de los días
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayNumber.toString(),
                                color = when {
                                    isAvailable -> Color.White // Texto blanco para fechas disponibles
                                    else -> Color(0xFF7E57C2) // Texto morado oscuro para días normales
                                },
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}
