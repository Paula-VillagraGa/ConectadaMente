package com.example.conectadamente.ui.homePsycho

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.conectadamente.R
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.ui.viewModel.PsychoAuthViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.size.Scale
import com.example.conectadamente.utils.constants.DataState

@Composable
fun ProfilePsyFromPatScreen(
    psychologistId: String?,
    viewModel: PsychoAuthViewModel = hiltViewModel()
) {
    // Observar el estado de los datos del psicólogo
    val profileState by viewModel.profileState.collectAsState()

    // Iniciar la carga del psicólogo al montar la pantalla
    LaunchedEffect(psychologistId) {
        if (psychologistId != null) {
            viewModel.getPsychoById(psychologistId)
        }
    }

    // Contenedor principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = profileState) {
            is DataState.Loading -> {
                CircularProgressIndicator() // Mostrar indicador de carga
            }

            is DataState.Success -> {
                state.data?.let { psycho ->
                    // Contenido principal cuando se cargan los datos
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Mostrar la foto
                        AsyncImage(
                            model = psycho.photoUrl ?: "gs://proyectoconectadamente.firebasestorage.app/profile_pictures/hombre1.png",
                            contentDescription = "Foto de ${psycho.name}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nombre
                        Text(
                            text = psycho.name,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Especialización
                        Text(
                            text = "Especialización:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (psycho.specialization.isEmpty()) {
                            Text(
                                text = "Sin especialización disponible",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            psycho.specialization.forEach { specialization ->
                                Text(
                                    text = specialization,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Rating
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ico_perfil),
                                contentDescription = "Rating",
                                tint = Color.Yellow,
                                modifier = Modifier.size(24.dp)
                            )
                            val rating = psycho.rating ?: "No disponible" // Si rating es nulo, mostrar "No disponible"
                            Text(
                                text = "$rating / 5.0",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Descripción
                        Text(
                            text = "Descripción:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        psycho.descriptionPsycho?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        } ?: run {
                            Text(
                                text = "Descripción no disponible",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                } ?: run {
                    // Mostrar un mensaje si no hay datos
                    Text(
                        text = "No se encontraron datos del psicólogo",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is DataState.Error -> {
                // Mostrar mensaje de error
                Text(
                    text = state.e,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                // Estado por defecto si no hay datos
                Text(
                    text = "Cargando...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
