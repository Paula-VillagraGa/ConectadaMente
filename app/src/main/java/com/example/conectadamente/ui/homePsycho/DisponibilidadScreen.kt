package com.example.conectadamente.ui.homePsycho

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.conectadamente.ui.viewModel.calendar.DisponibilidadViewModel
import java.util.Calendar

@Composable
fun DisponibilidadScreen(viewModel: DisponibilidadViewModel) {
    val fechaSeleccionada = remember { mutableStateOf("") }
    val horaInicio = remember { mutableStateOf("") }
    val horaFin = remember { mutableStateOf("") }
    val estado = viewModel.estado.collectAsState()

    val userId = viewModel.obtenerIdUsuarioActual()

    // Fondo y espaciado general
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Título de la pantalla
        Text(
            text = "Configura tu disponibilidad",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Selección de fecha
        SeleccionFecha(fechaSeleccionada) {
            viewModel.mostrarDatePicker { fecha -> fechaSeleccionada.value = fecha }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Entrada para hora de inicio
        EntradaHora(
            label = "Hora de inicio:",
            horaValue = horaInicio,
            onValueChange = { horaInicio.value = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Entrada para hora de fin
        EntradaHora(
            label = "Hora de fin:",
            horaValue = horaFin,
            onValueChange = { horaFin.value = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para guardar la disponibilidad
        Button(
            onClick = {
                viewModel.guardarDisponibilidad(
                    fechaSeleccionada.value,
                    horaInicio.value,
                    horaFin.value,
                    userId

                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Guardar disponibilidad", color = Color.White)
        }

        // Estado del proceso de guardado
        if (estado.value.isNotEmpty()) {
            Text(
                text = estado.value,
                color = Color.Green,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionFecha(fechaSeleccionada: MutableState<String>, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Abre el DatePickerDialog cuando se haga clic
    val openDatePicker = {
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Al seleccionar una fecha, la asignamos al campo
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                fechaSeleccionada.value = selectedDate
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    Column {
        Text(
            text = "Selecciona una fecha:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Button(
            onClick = openDatePicker,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = if (fechaSeleccionada.value.isEmpty()) "Seleccionar fecha" else fechaSeleccionada.value,
                color = Color.White
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntradaHora(label: String, horaValue: MutableState<String>, onValueChange: (String) -> Unit) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        OutlinedTextField(
            value = horaValue.value,
            onValueChange = onValueChange,
            placeholder = { Text("Ej: 09:00") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                focusedLabelColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
}
