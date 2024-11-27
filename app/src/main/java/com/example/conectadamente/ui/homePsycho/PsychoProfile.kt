package com.example.conectadamente.ui.homePsycho

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.conectadamente.ui.viewModel.PsychoAuthViewModel
import com.example.conectadamente.utils.constants.DataState

@Composable
fun PsychoProfileScreen(viewModel: PsychoAuthViewModel = hiltViewModel()) {

    val profileState by viewModel.profileState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCurrentProfile() // Cargar el perfil al entrar a la pantalla
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = profileState) {
            is DataState.Loading -> {
                CircularProgressIndicator() // Mostrar indicador de carga
            }

            is DataState.Success -> {
                val profile = state.data // Accede al perfil si es exitoso
                if (profile != null) {
                    // Mostrar los datos del psicólogo
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Nombre: ${profile.name ?: "No disponible"}")
                        Text(text = "Especialidad: ${profile.specialization ?: "No disponible"}")
                        Text(text = "Teléfono: ${profile.phone ?: "No disponible"}")
                        Text(text = "Correo: ${profile.email ?: "No disponible"}")
                    }
                } else {
                    // Mostrar mensaje de carga
                    Text(text = "Cargando perfil...")
                }
            }

            is DataState.Error -> {
                val errorMessage = state.e
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }

            else -> {
                // Mostrar estado de finalización u otros estados
                Text(text = "Cargando perfil...")
            }
        }
    }
}
