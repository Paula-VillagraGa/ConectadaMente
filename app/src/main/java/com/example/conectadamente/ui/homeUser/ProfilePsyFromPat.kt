package com.example.conectadamente.ui.homeUser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import coil.compose.AsyncImage
import com.example.conectadamente.R
import com.example.conectadamente.ui.theme.Gray50
import com.example.conectadamente.ui.viewModel.PsychoAuthViewModel
import com.example.conectadamente.ui.viewModel.reviews.ReviewViewModel
import com.example.conectadamente.utils.constants.DataState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePsyFromPatScreen(
    psychologistId: String?,
    navController: NavController
) {

    // Usar ambos ViewModels
    val reviewViewModel: ReviewViewModel = hiltViewModel()
    val viewModel: PsychoAuthViewModel = hiltViewModel()


    // Observar el estado de los datos del psicólogo
    val profileState by viewModel.profileState.collectAsState()
    val ratingState by reviewViewModel.ratingState.observeAsState()

    var rating by remember { mutableStateOf(0) }
    var feedback by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf(listOf<String>()) }

    // Iniciar la carga del psicólogo al montar la pantalla
    LaunchedEffect(psychologistId) {
        if (psychologistId != null) {
            viewModel.getPsychoById(psychologistId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Contenido principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                                model = psycho.photoUrl
                                    ?: "gs://proyectoconectadamente.firebasestorage.app/profile_pictures/hombre1.png",
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
                                fontWeight = FontWeight.Bold,
                                color = Gray50
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

                            Spacer(modifier = Modifier.height(8.dp))

                            // Rating
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_estrella),
                                    contentDescription = "Rating",
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(24.dp)
                                )
                                val rating = psycho.rating ?: "No disponible"
                                Text(
                                    text = "$rating / 5.0",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

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
                            Text(
                                text = "Califica al psicólogo",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(top = 16.dp)
                            )

                            //Review acá
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                for (i in 1..5) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (i <= rating) Color.Yellow else Color.Gray,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clickable { rating = i }
                                    )
                                }

                            }
                            // Tags (opcional)
                            Text(
                                text = "Define al Psicólogo",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            LazyRow(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val availableTags = listOf("Empático", "Buen oyente", "Profesional", "Amable")
                                items(availableTags) { tag ->
                                    AssistChip(
                                        onClick = {
                                            if (tags.contains(tag)) {
                                                tags = tags - tag
                                            } else if (tags.size < 3) {
                                                tags = tags + tag
                                            }
                                        },
                                        label = { Text(tag) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = if (tags.contains(tag)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                }
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
}
@Composable
fun Chip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}
