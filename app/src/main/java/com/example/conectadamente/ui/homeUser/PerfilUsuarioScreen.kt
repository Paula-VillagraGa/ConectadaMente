package com.example.conectadamente.ui.homeUser


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.ui.theme.Purple80
import com.example.conectadamente.ui.viewModel.PatientProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioScreen(viewModel: PatientProfileViewModel = hiltViewModel(), navController: NavController) {
    // Cargar datos del paciente actual al cargar la pantalla
    LaunchedEffect(Unit) {
        viewModel.fetchCurrentPatientData()
    }

    // Obteniendo estados desde el ViewModel
    val patientData by viewModel.patientData.observeAsState(initial = null)
    val error by viewModel.error.observeAsState(initial = null)

    Scaffold(
        modifier = Modifier.padding(0.dp),
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Regresar",
                            tint = Purple80
                        )
                    }
                })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                error != null -> {
                    Text(
                        text = "Error: ${error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                patientData != null -> ProfileCard(patientData!!)
                else -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCard(patient: PatientModel) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Perfil de Usuario",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )

            ProfileDetailRow(
                icon = Icons.Default.Person,
                contentDescription = "Nombre",
                text = "Nombre: ${patient.name}",
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 20.sp
            )
            )

            ProfileDetailRow(
                icon = Icons.Default.Email,
                contentDescription = "Email",
                text = "Email: ${patient.email}",
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 20.sp
                )
            )
            ProfileDetailRow(
                icon = Icons.Default.LocationOn,
                contentDescription = "Región",
                text = "Región: ${if (patient.region.isNullOrEmpty()) "No disponible" else patient.region}",
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 20.sp
            )
            )
            ProfileDetailRow(
                icon = Icons.Default.Business,
                contentDescription = "Ciudad",
                text = "Ciudad: ${if (patient.city.isNullOrEmpty()) "No disponible" else patient.city}",
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 20.sp
            )
            )
            ProfileDetailRow(
                icon = Icons.Default.Cake,
                contentDescription = "Fecha de Nacimiento",
                text = "Fecha de Nacimiento: ${if (patient.birthDate.isNullOrEmpty()) "No disponible" else patient.birthDate}",
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 20.sp
                )
            )

            // Botón para navegar a la pantalla de edición
            Button(
                onClick = {},
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Editar Perfil")
            }
        }
    }
}

@Composable
fun ProfileDetailRow(
    icon: ImageVector,
    contentDescription: String,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
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
            style = textStyle.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}
