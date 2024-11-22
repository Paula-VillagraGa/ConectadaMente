package com.example.conectadamente.ui.homeUser


import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.ui.theme.PoppinsFontFamily
import com.example.conectadamente.ui.theme.Purple40
import com.example.conectadamente.ui.viewModel.PatientProfileViewModel

@Composable
fun PerfilUsuarioScreen(viewModel: PatientProfileViewModel = hiltViewModel()) {
    // Llamar a la función para obtener los datos cuando la pantalla se carga
    LaunchedEffect(Unit) {
        viewModel.fetchPatientData()  // Esto obtendrá los datos del paciente actual
    }

    // Obtener los datos del paciente desde el ViewModel
    val patientData by viewModel.patientData.observeAsState()  // Observamos los datos
    val error by viewModel.error.observeAsState(null)  // Observamos errores (usamos null como valor predeterminado)

    // Contenedor principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Mostrar una pantalla de carga, si no hay datos
        if (patientData == null) {
            // Mostrar un mensaje de error si no se puede obtener los datos
            if (error != null) {
                ErrorMessage(error!!)
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            // Si tenemos los datos, mostrar el nombre y el email
            patientData?.let { patient ->
                ProfileCard(patient)
            }
        }
    }
}

// Componente para mostrar los datos del paciente en una tarjeta
@Composable
fun ProfileCard(patient: PatientModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Usamos containerColor
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Perfil de Usuario",
                style = TextStyle(
                    color = Purple40,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily,
                    fontStyle = FontStyle.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Nombre
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Nombre",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Nombre: ${patient.name}",
                    style = TextStyle(
                        color = Purple40,
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily,
                        fontStyle = FontStyle.Normal
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Email
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Email: ${patient.email}",
                    style = TextStyle(
                        color = Purple40,
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily,
                        fontStyle = FontStyle.Normal
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

// Componente para mostrar un mensaje de error
@Composable
fun ErrorMessage(errorMessage: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: $errorMessage",
            color = Color.Red,
        )
    }
}

