package com.example.conectadamente.ui.homePsycho

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.conectadamente.ui.viewModel.PsychoAuthViewModel

@Composable
fun PsychoProfileScreen(viewModel: PsychoAuthViewModel = hiltViewModel()) {
    val profile by viewModel.profileState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCurrentProfile() // Cargar el perfil al entrar a la pantalla
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (profile != null) {
            // Mostrar los datos del psicólogo
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Nombre: ${profile?.name ?: "No disponible"}")
                Text(text = "Especialidad: ${profile?.specialization ?: "No disponible"}")
                Text(text = "Teléfono: ${profile?.phone ?: "No disponible"}")
                Text(text = "Correo: ${profile?.email ?: "No disponible"}")
            }
        } else {
            // Mostrar mensaje de carga
            Text(text = "Cargando perfil...")
        }
    }
}
