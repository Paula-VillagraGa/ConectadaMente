package com.example.conectadamente.ui.homeUser.calendar

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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


@OptIn(ExperimentalMaterial3Api::class)
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

    // Estado de la fecha seleccionada
    var fechaSeleccionada by remember { mutableStateOf<String?>(null) }

    // Estado para mostrar el cuadro emergente
    var mostrarDialogo by remember { mutableStateOf(false) }

    // Estado para la modalidad seleccionada
    var modalidadSeleccionada by remember { mutableStateOf("En línea") }

    // Estado para la hora y fecha seleccionada
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    var availabilityIdSeleccionado by remember { mutableStateOf<String?>(null) }

    // Cargar horarios disponibles para el psicólogo
    remember {
        Log.d("AgendarScreen", "Cargando horarios disponibles para el psicólogo: $psychoId")
        viewModel.cargarHorariosDisponibles(psychoId)
    }

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
                Log.d("AgendarScreen", "Fecha seleccionada: $date")
                fechaSeleccionada = date
            }
        )
        fechaSeleccionada?.let { selectedDate ->
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(
                    horariosDisponibles.value
                        .filter { it["fecha"] == selectedDate && it["estado"] == "disponible" }
                        .sortedBy {
                            val horaString = it["hora"] as? String ?: ""
                            try {
                                LocalTime.parse(horaString, DateTimeFormatter.ofPattern("HH:mm"))
                            } catch (e: Exception) {
                                LocalTime.MAX
                            }
                        }
                ) { horario ->
                    HorarioItem(
                        horario = horario,
                        onAgendarClick = { availabilityId ->
                            Log.d("AgendarScreen", "Horario seleccionado: ${horario["hora"]}, AvailabilityId: $availabilityId")
                            horaSeleccionada = horario["hora"] as? String
                            availabilityIdSeleccionado = availabilityId
                            mostrarDialogo = true
                        }
                    )
                }
            }
        }

        if (estadoAgendar.value.isNotEmpty()) {
            Log.d("AgendarScreen", "Estado de la acción de agendar: ${estadoAgendar.value}")
            Text(
                text = estadoAgendar.value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    if (mostrarDialogo) {
        Log.d("AgendarScreen", "Mostrando el cuadro de diálogo para confirmar cita.")
        ModalBottomSheet(
            onDismissRequest = { mostrarDialogo = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .padding(top = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Confirmar cita",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Fecha: $fechaSeleccionada",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Hora: $horaSeleccionada",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Selector de modalidad con botones
                    Text(text = "Selecciona modalidad:")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center // Centra los botones
                    ) {
                        Button(
                            onClick = {
                                Log.d("AgendarScreen", "Modalidad seleccionada: En línea")
                                modalidadSeleccionada = "En línea"
                            },
                            modifier = Modifier
                                .padding(8.dp) // Ajusta el espacio alrededor del botón
                                .weight(1f), // Asegura que ambos botones tengan el mismo tamaño
                            shape = MaterialTheme.shapes.medium, // Forma más estilizada
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (modalidadSeleccionada == "En línea") Purple10 else Color.Gray
                            ),
                            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 32.dp) // Ajusta el padding para hacerlo más grande
                        ) {
                            Text(
                                text = "En línea",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), // Texto más grande y visible
                                modifier = Modifier.align(Alignment.CenterVertically) // Alinea el texto verticalmente
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp)) // Añade un espacio entre los botones

                        Button(
                            onClick = {
                                Log.d("AgendarScreen", "Modalidad seleccionada: Presencial")
                                modalidadSeleccionada = "Presencial"
                            },
                            modifier = Modifier
                                .padding(8.dp) // Ajusta el espacio alrededor del botón
                                .weight(1f), // Asegura que ambos botones tengan el mismo tamaño
                            shape = MaterialTheme.shapes.medium, // Forma más estilizada
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (modalidadSeleccionada == "Presencial") Purple10 else Color.Gray
                            ),
                            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 32.dp) // Ajusta el padding para hacerlo más grande
                        ) {
                            Text(
                                text = "Presencial",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), // Texto más grande y visible
                                modifier = Modifier.align(Alignment.CenterVertically) // Alinea el texto verticalmente
                            )
                        }
                    }


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 16.dp), // Márgenes arriba y abajo
                        contentAlignment = Alignment.Center // Centra el contenido dentro del Box
                    ) {
                        Button(
                            onClick = {
                                Log.d("AgendarScreen", "Confirmando cita con los siguientes datos: availabilityId=$availabilityIdSeleccionado, patientId=$patientId, psychoId=$psychoId, modalidad=$modalidadSeleccionada")

                                // Crear un documento en "appointments" con la información de la cita
                                viewModel.agendarHorario(
                                    availabilityId = availabilityIdSeleccionado ?: "",  // El ID del horario disponible
                                    patientId = patientId,  // El ID del paciente
                                    psychoId = psychoId,  // El ID del psicólogo
                                    modalidad = modalidadSeleccionada  // La modalidad seleccionada
                                )

                                // Actualizar el estado de la disponibilidad a "reservado"
                                viewModel.actualizarDisponibilidad(
                                    availabilityId = availabilityIdSeleccionado ?: ""  // ID del horario seleccionado
                                )
                                mostrarDialogo = false
                            },
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(horizontal = 16.dp),
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(containerColor = Purple10),
                            contentPadding = PaddingValues(vertical = 14.dp, horizontal = 50.dp)
                        ) {
                            Text(
                                text = "Confirmar",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun HorarioItem(horario: Map<String, Any>, onAgendarClick: (String) -> Unit) {
    val fecha = horario["fecha"] as? String ?: ""
    val hora = horario["hora"] as? String ?: ""
    val availabilityId = horario["availabilityId"] as? String ?: ""  // Asegúrate de que es availabilityId y no "id" o algo diferente

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
                Text(text = "Hora: $hora", style = MaterialTheme.typography.bodyMedium)
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

    // Obtener el primer día del mes (0 = Lunes, 6 = Domingo)
    val firstDayOfMonth = (currentMonth.atDay(1).dayOfWeek.value + 6) % 7

    // Días de la semana (Lun, Mar, Mié, Jue, Vie, Sáb, Dom)
    val daysOfWeek = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

    // Calculamos el total de celdas necesarias (esto es como un offset para la primera fila)
    val totalCells = firstDayOfMonth + daysInMonth
    val rows = (totalCells / 7) + if (totalCells % 7 > 0) 1 else 0

    Column(modifier = Modifier.padding(16.dp)) {
        // Cabecera del calendario
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { currentMonth = currentMonth.minusMonths(1) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary // Establece el color del icono
                )
            ) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Mes anterior")
            }

            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }, // Primera letra en mayúscula
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary // Color primario
                )
            )
            IconButton(
                onClick = { currentMonth = currentMonth.plusMonths(1) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary // Establece el color del icono
                )
            ) {
                Icon(Icons.Default.ArrowForwardIos, contentDescription = "Mes siguiente")
            }
        }

        // Mostrar los días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Días del mes
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
}
