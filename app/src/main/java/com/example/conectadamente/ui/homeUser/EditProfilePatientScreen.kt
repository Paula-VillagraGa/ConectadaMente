package com.example.conectadamente.ui.homeUser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Locale


/*
// Región (Editable)
OutlinedTextField(
value = region,
onValueChange = { region = it },
label = { Text("Región") },
modifier = Modifier
.fillMaxWidth()
.shadow(4.dp, RoundedCornerShape(8.dp)),
colors = TextFieldDefaults.outlinedTextFieldColors(
containerColor = MaterialTheme.colorScheme.background,
focusedBorderColor = MaterialTheme.colorScheme.primary,
unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
),
leadingIcon = {
    Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = "Región",
        tint = MaterialTheme.colorScheme.primary
    )
}
)

// Ciudad (Editable)
OutlinedTextField(
value = city,
onValueChange = { city = it },
label = { Text("Ciudad") },
modifier = Modifier
.fillMaxWidth()
.shadow(4.dp, RoundedCornerShape(8.dp)),
colors = TextFieldDefaults.outlinedTextFieldColors(
containerColor = MaterialTheme.colorScheme.background,
focusedBorderColor = MaterialTheme.colorScheme.primary,
unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
),
leadingIcon = {
    Icon(
        imageVector = Icons.Default.Business,
        contentDescription = "Ciudad",
        tint = MaterialTheme.colorScheme.primary
    )
}
)


// Fecha de Nacimiento (Editable con DatePicker)
OutlinedTextField(
value = birthDate,
onValueChange = {},
label = { Text("Fecha de Nacimiento") },
modifier = Modifier
.fillMaxWidth()
.clickable { isDatePickerOpen = true } // Abre el DatePicker al hacer clic
.shadow(4.dp, RoundedCornerShape(8.dp)),
colors = TextFieldDefaults.outlinedTextFieldColors(
containerColor = MaterialTheme.colorScheme.background,
focusedBorderColor = MaterialTheme.colorScheme.primary,
unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
),
leadingIcon = {
    Icon(
        imageVector = Icons.Default.CalendarToday,
        contentDescription = "Fecha de Nacimiento",
        tint = MaterialTheme.colorScheme.primary
    )
},
enabled = false // Deshabilitado para evitar la edición directa
)

// Mostrar el DatePickerDialog si isDatePickerOpen es verdadero
if (isDatePickerOpen) {
    DatePickerDialog(
        onDismissRequest = { isDatePickerOpen = false },
        onDateSelected = { year, month, dayOfMonth ->
            // Actualizar la fecha de nacimiento
            birthDate = "$dayOfMonth/${month + 1}/$year"
            isDatePickerOpen = false
        }
    )
}



// Botón para guardar los cambios
Button(
onClick = {
    val updatedPatient = patient.copy(
        region = region,
        city = city,
        birthDate = birthDate
    )
    viewModel.updatePatientData(updatedPatient)  // Actualiza en Firestore
},
modifier = Modifier.align(Alignment.CenterHorizontally)
) {
    Text("Guardar cambios")
}
}
}
}


@Composable
fun ProfileDetailRow(icon: ImageVector, contentDescription: String, text: String, textStyle: TextStyle = MaterialTheme.typography.bodyMedium) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
fun ErrorMessage(errorMessage: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: $errorMessage",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
@Composable
fun DatePickerDialog(onDismissRequest: () -> Unit, onDateSelected: (Int, Int, Int) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Configura el Locale en español para el DatePickerDialog
    val spanishLocale = Locale("es", "ES")
    val configuration = context.resources.configuration
    configuration.setLocale(spanishLocale)
    context.createConfigurationContext(configuration)

    android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(year, month, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        setOnDismissListener { onDismissRequest() }
        show()
    }
}*/