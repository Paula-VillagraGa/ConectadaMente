package com.example.conectadamente.ui.homeUser

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePsyFromPatScreen(
    psychologistId: String?,
    navController: NavController
) {
    // Estados para el Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    var isSubmitting by remember { mutableStateOf(false) }
    var messageToShow by remember { mutableStateOf<String?>(null) }

    // ViewModels
    val reviewViewModel: ReviewViewModel = hiltViewModel()
    val viewModel: PsychoAuthViewModel = hiltViewModel()

    var ratingPromedio by remember { mutableStateOf(0.0) }

    // Estados observados
    val profileState by viewModel.profileState.collectAsState()
    val ratingState by reviewViewModel.ratingState.observeAsState()

    // Estados locales
    var rating by remember { mutableIntStateOf(0) }
    var tags by remember { mutableStateOf(listOf<String>()) }

    // Manejo de mensajes del Snackbar
    LaunchedEffect(ratingState) {
        when (ratingState) {
            is DataState.Success -> {
                snackbarHostState.showSnackbar("¡Reseña enviada correctamente!")
                isSubmitting = false
            }
            is DataState.Error -> {
                snackbarHostState.showSnackbar("Error: ${(ratingState as DataState.Error).e}")
                isSubmitting = false
            }
            DataState.Loading -> {
                isSubmitting = true
            }
            else -> {}
        }
    }

    // Reseña previa
    LaunchedEffect(psychologistId) {
        psychologistId?.let { id ->
            viewModel.getPsychoById(id)
            ratingPromedio = reviewViewModel.getAverageRating(id)

            val patientId = FirebaseAuth.getInstance().currentUser?.uid
            if (patientId != null) {
                val existingReview = reviewViewModel.getExistingReview(id, patientId)
                existingReview?.let { review ->
                    rating = review.rating.toInt()
                    tags = review.tags
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.padding(2.dp),
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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 2.dp)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = profileState) {
                is DataState.Loading -> {
                    CircularProgressIndicator()
                }
                is DataState.Success -> {
                    state.data?.let { psycho ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.Start
                        ) {
                            // Foto y nombre
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
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
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = psycho.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
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

                            // Rating promedio
                            val formattedRating = "%.1f".format(ratingPromedio)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$formattedRating / 5.0",
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
                            Text(
                                text = psycho.descriptionPsycho ?: "Descripción no disponible",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            // Botones para agendar cita y chat
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        // Acción para agendar cita
                                        psychologistId?.let { id ->
                                            navController.navigate("scheduleAppointment/$id")
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .weight(1f)
                                        .height(48.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = "Agendar cita",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Agendar cita", style = MaterialTheme.typography.bodyMedium)
                                }

                                OutlinedButton(
                                    onClick = {
                                        // Acción para iniciar el chat
                                        psychologistId?.let { id ->
                                            navController.navigate("chatWithPsychologist/$id")
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .weight(1f)
                                        .height(48.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Chat,
                                        contentDescription = "Hablar por chat",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Chat", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            // Calificación del paciente
                            Text(
                                text = "Califica al psicólogo",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(top = 16.dp)
                            )
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

                            // Etiquetas
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
                                        label = { Text(text = tag) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = if (tags.contains(tag)) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.secondary,
                                            labelColor = Color.White
                                        )
                                    )
                                }
                            }

                            // Botón de enviar
                            Button(
                                onClick = {
                                    psychologistId?.let { id ->
                                        val patientId = FirebaseAuth.getInstance().currentUser?.uid
                                        if (patientId != null) {
                                            isSubmitting = true
                                            reviewViewModel.submitRating(
                                                psychoId = id,
                                                patientId = patientId,
                                                tags = tags,
                                                rating = rating.toDouble()
                                            )
                                        } else {
                                            messageToShow = "No se pudo autenticar al usuario."
                                        }
                                    }
                                },
                                enabled = rating > 0 && !isSubmitting,
                                modifier = Modifier.padding(vertical = 16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSubmitting) MaterialTheme.colorScheme.secondary
                                    else MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(if (isSubmitting) "Enviando..." else "Enviar calificación")
                            }
                        }
                    }
                }
                is DataState.Error -> {
                    Text(
                        text = state.e,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    Text(
                        text = "Cargando...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

