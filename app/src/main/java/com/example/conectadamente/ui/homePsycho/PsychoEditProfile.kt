package com.example.conectadamente.ui.homePsycho

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.ui.viewModel.PsychoAuthViewModel
import com.example.conectadamente.utils.constants.DataState
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.libraries.places.api.model.Place


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPsychoProfileScreen(
    viewModel: PsychoAuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val profileState by viewModel.profileState.collectAsState()
    val context = LocalContext.current

    // Estados locales para los campos del perfil
    var phone by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var selectedSpecializations by remember { mutableStateOf(listOf<String>()) }
    var selectedTherapy by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedLocation by remember { mutableStateOf("") }

    val specializations = listOf(
        "Clinica", "Infantil", "Organizacional", "Educacional", "Salud",
        "Deportiva", "Social", "Comunicatorio", "Neuropsicológica", "Terapéutica",
        "Psicoterapia", "Sexualidad", "Duelo", "Humanista", "Integrativa"
    )
    //Terapias
    val therapies = listOf(
        "Terapia Cognitivo-Conductual", "Terapia Psicoanalítica", "Terapia Humanista",
        "Terapia Gestalt", "Terapia Familiar", "Terapia de Aceptación y Compromiso",
        "Terapia de Juego", "Terapia de Pareja", "Terapia Psicodinámica", "Terapia Existencial",
        "Terapia Conductual", "Terapia Sistémica"
    )


    var expanded by remember { mutableStateOf(false) }

    // Launcher de Places Autocomplete
    val placesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val place = Autocomplete.getPlaceFromIntent(intent)
                selectedLocation = place.address ?: "Ubicación no disponible"
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile() // Llama al ViewModel para cargar el perfil
    }

    // Inicializa Google Places API
    LaunchedEffect(Unit) {
        if (!Places.isInitialized()) {
            Places.initialize(context, "AIzaSyAH__Pc7g-tqS1oRvZz0kQPinRdh0HPN-g") // Reemplaza con tu API Key
        }
        viewModel.loadProfile() // Cargar el perfil
    }

    // Sincroniza los datos del perfil al estado del formulario
    LaunchedEffect(profileState) {
        if (profileState is DataState.Success) {
            val psycho = (profileState as DataState.Success).data
            psycho?.let {
                phone = it.phone.orEmpty()
                description = it.descriptionPsycho.orEmpty()
                experience = it.experience.orEmpty()
                selectedSpecializations = it.specialization.orEmpty()
                selectedTherapy = it.therapy.orEmpty()
                selectedLocation = it.location.orEmpty() // Cargar ubicación si existe
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Volver")
                    }
                },
                title = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()) // Habilitar scroll
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espaciado entre elementos
        ) {

            // Título Editar Perfil
            Text(
                text = "Edita tu Perfil",
                style = MaterialTheme.typography.headlineMedium, // Ajusta el estilo de texto
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 10.dp) // Ajusta el espacio superior
            )

            // Campo Teléfono
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )


            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )


            OutlinedTextField(
                value = experience,
                onValueChange = { experience = it },
                label = { Text("Experiencia") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            // Especializaciones
            Text("Especializaciones (Escoge máximo 2):")
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(specializations) { spec ->
                    val isSelected = spec in selectedSpecializations
                    val canSelectMore = selectedSpecializations.size < 2 || isSelected
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) {
                                selectedSpecializations = selectedSpecializations - spec
                            } else if (canSelectMore) {
                                selectedSpecializations = selectedSpecializations + spec
                            }
                        },
                        label = { Text(text = spec) },
                        modifier = Modifier.padding(4.dp),
                        enabled = canSelectMore // Deshabilitar cuando se alcanzó el límite
                    )
                }
            }


            Text("Tipo de Terapia:")
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedTherapy,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Terapia") },
                    placeholder = { Text("Selecciona") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    therapies.forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado) },
                            onClick = {
                                selectedTherapy = estado
                                expanded = false
                            }
                        )
                    }
                }
            }
            // Selección de Ubicación
            Text("Ubicación:")
            OutlinedTextField(
                value = selectedLocation,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        val intent = Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.OVERLAY, listOf(Place.Field.ADDRESS)
                        ).build(context)
                        placesLauncher.launch(intent)
                    }) {
                        Icon(Icons.Default.Place, contentDescription = "Seleccionar Ubicación")
                    }
                },
                label = { Text("Selecciona tu ubicación") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


            // Botón de Guardar Cambios
            Button(onClick = {
                viewModel.updateProfile(
                    phone = phone,
                    description = description,
                    experience = experience,
                    specializations = selectedSpecializations,
                    location = selectedLocation,
                    imageUri = imageUri,
                    therapy = selectedTherapy
                )
                navController.popBackStack()
            }) {
                Text("Guardar Cambios")
            }
        }
    }
}
