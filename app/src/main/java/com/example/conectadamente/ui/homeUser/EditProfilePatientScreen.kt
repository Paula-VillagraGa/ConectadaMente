package com.example.conectadamente.ui.homeUser

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.conectadamente.ui.viewModel.PatientProfileViewModel
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfilePatientScreen(
    viewModel: PatientProfileViewModel = hiltViewModel(),
    onProfileSaved: () -> Unit,
    navController: NavController
) {
    // Cargar datos del paciente al abrir la pantalla
    LaunchedEffect(Unit) {
        viewModel.fetchCurrentPatientData()
    }

    val patientData by viewModel.patientData.observeAsState()
    val error by viewModel.error.observeAsState()

    // Mostrar un indicador de carga si los datos aún no están disponibles
    if (patientData == null) {
        Box(
            modifier = Modifier.fillMaxSize() // Asegura que el Box ocupe toda la pantalla
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center) // Centra el indicador
            )
        }
        return
    }


    // Manejar errores si existen
    error?.let {
        Snackbar {
            Text("Error: $it")
        }
    }

    // Variables para campos editables
    var region by remember { mutableStateOf(patientData?.region.orEmpty()) }
    var city by remember { mutableStateOf(patientData?.city.orEmpty()) }
    var birthDate by remember { mutableStateOf(patientData?.birthDate.orEmpty()) }
    var isDatePickerOpen by remember { mutableStateOf(false) }

    val regions = listOf(
        "XV - Región de Arica y Parinacota",
        "I - Región de Tarapacá",
        "II - Región de Antofagasta",
        "III - Región de Atacama",
        "IV - Región de Coquimbo",
        "V - Región de Valparaíso",
        "RM - Región Metropolitana de Santiago",
        "VI - Región del Libertador General Bernardo O'Higgins",
        "VII - Región del Maule",
        "XVI - Región de Ñuble",
        "VIII - Región del Biobío",
        "IX - Región de La Araucanía",
        "XIV - Región de Los Ríos",
        "X - Región de Los Lagos",
        "XI - Región de Aysén del General Carlos Ibáñez del Campo",
        "XII - Región de Magallanes y de la Antártica Chilena"
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Fecha de Nacimiento (Editable con DatePicker)
            OutlinedTextField(
                value = birthDate ?: "",
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
                enabled = false
            )

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
            // Componente de selección de región
            RegionDropdown(
                region = region,
                onRegionSelected = { selectedRegion ->
                    region = selectedRegion
                },
                regions = regions
            )

            // Ciudad (Editable)
            OutlinedTextField(
                value = city ?: "",
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



            Button(
                onClick = {
                    val updatedPatient = patientData?.copy(
                        region = region,
                        city = city,
                        birthDate = birthDate
                    )
                    if (updatedPatient != null) {
                        viewModel.updatePatientData(updatedPatient)
                        onProfileSaved()
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Guardar cambios")
            }
        }
    }
}
@Composable
fun DatePickerDialog(onDismissRequest: () -> Unit, onDateSelected: (Int, Int, Int) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionDropdown(
    region: String?,
    onRegionSelected: (String) -> Unit,
    regions: List<String> // Lista de regiones disponibles
) {
    var expanded by remember { mutableStateOf(false) } // Controla si el menú está abierto

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = region ?: "", // Muestra la región seleccionada o una cadena vacía
            onValueChange = {}, // La edición manual no está permitida, se selecciona del menú
            label = { Text("Región") },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(), // Necesario para conectar con el menú
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            ),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Cerrar menú" else "Abrir menú",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            readOnly = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            regions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onRegionSelected(option) // Actualiza la región seleccionada
                        expanded = false // Cierra el menú
                    }
                )
            }
        }
    }
}