package com.example.conectadamente.ui.homeUser

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.ui.viewModel.PsychoAuthViewModel
import com.example.conectadamente.ui.viewModel.reviews.ReviewViewModel
import com.example.conectadamente.utils.constants.DataState
import com.example.conectadamente.utils.extensions.ShowSnackBarMessage
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePsyFromPatScreen(
    psychologistId: String?,
    navController: NavController
) {
    val patientId = FirebaseAuth.getInstance().currentUser?.uid

    // Estados y ViewModels
    val snackbarHostState = remember { SnackbarHostState() }
    val reviewViewModel: ReviewViewModel = hiltViewModel()
    val viewModel: PsychoAuthViewModel = hiltViewModel()

    val isSubmitting by reviewViewModel.isSubmitting.observeAsState(false)
    var messageToShow by remember { mutableStateOf<String?>(null) }
    var ratingPromedio by remember { mutableStateOf(0.0) }
    var rating by remember { mutableIntStateOf(0) }
    var tags by remember { mutableStateOf(listOf<String>()) }
    var canSubmitReview by remember { mutableStateOf(false) } // Nuevo estado para verificar si se puede enviar reseña

    val profileState by viewModel.profileState.collectAsState()
    val ratingState by reviewViewModel.ratingState.collectAsState()



    // LaunchedEffect con el estado correcto
    LaunchedEffect(ratingState) {
        when (ratingState) {
            is DataState.Success -> {
                snackbarHostState.showSnackbar("¡Reseña enviada correctamente!")
            }

            is DataState.Error -> {
                snackbarHostState.showSnackbar("Error: ${(ratingState as DataState.Error).e}")
            }

            DataState.Loading -> {
                // Solo mostrar "Cargando..." mientras se envía
            }

            else -> {}
        }
    }


    LaunchedEffect(psychologistId) {
        psychologistId?.let { id ->
            viewModel.getPsychoById(id)
            ratingPromedio = reviewViewModel.getAverageRating(id)

            // Verificar si la cita ha sido realizada de manera asíncrona
            val patientId = FirebaseAuth.getInstance().currentUser?.uid
            if (patientId != null) {
                // Llamada a la función suspendida canSubmitReview
                val canReview = reviewViewModel.canSubmitReview(patientId, id)
                canSubmitReview = canReview // Actualizar el estado de la reseña

                // Obtener reseña existente, si la hay
                reviewViewModel.getExistingReview(id, patientId)?.let { review ->
                    rating = review.rating.toInt()
                    tags = review.tags
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Regresar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val state = profileState) {
                is DataState.Loading -> CircularProgressIndicator()
                is DataState.Success -> state.data?.let { psycho ->
                    // Información del perfil
                    ProfileSection(psycho, ratingPromedio, navController, psychologistId)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sección para realizar una nueva reseña
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Si la cita no ha sido realizada, deshabilitar la sección y mostrar mensaje
                    if (!canSubmitReview) {
                        Text(
                            "Puedes calificar una vez te hayas atendido con este especialista.",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        // Mostrar la sección de reseña solo si la cita ha sido realizada
                        NewReviewSection(
                            rating = rating,
                            tags = tags,
                            onRatingChange = { rating = it },
                            onTagToggle = { tag ->
                                tags =
                                    if (tags.contains(tag)) tags - tag else if (tags.size < 3) tags + tag else tags
                            },
                            onSubmit = {
                                val patientId = FirebaseAuth.getInstance().currentUser?.uid
                                if (psychologistId != null && patientId != null && canSubmitReview) {
                                    reviewViewModel.submitRating(
                                        psychologistId,
                                        patientId,
                                        tags,
                                        rating.toDouble()
                                    )
                                } else {
                                    messageToShow =
                                        "No se pudo autenticar al usuario o la cita no ha sido realizada."
                                }
                            },
                            isSubmitting = isSubmitting
                        )
                    }
                }

                is DataState.Error -> Text(
                    "Error: ${state.e}",
                    color = MaterialTheme.colorScheme.error
                )

                else -> Text("Cargando...")
            }
        }
    }
}


    @Composable
    fun ProfileSection(
        psycho: PsychoModel,
        ratingPromedio: Double,
        navController: NavController,
        psychologistId: String?,

    ) {
        val context = LocalContext.current // Obtiene el contexto de la aplicación
        // Mostrar foto, nombre, especialización y descripción
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = psycho.photoUrl
                        ?: "gs://proyectoconectadamente.firebasestorage.app/profile_pictures/hombre1.png",
                    contentDescription = "Foto de ${psycho.name}",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(psycho.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Especialización: ${psycho.specialization?.joinToString() ?: "No disponible"}")
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow)
                Spacer(modifier = Modifier.width(4.dp))
                Text("%.1f / 5.0".format(ratingPromedio))
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Descripción: ${psycho.descriptionPsycho ?: "No disponible"}")
            Spacer(modifier = Modifier.height(8.dp))
            // Ubicación
            psycho.location?.let { location ->
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .clickable {
                            // Redirigir a Google Maps usando la ubicación
                            val mapsUrl = "https://www.google.com/maps/search/?api=1&query=${Uri.encode(location)}"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl))
                            context.startActivity(intent)
                        }
                )
            } ?: Text(
                text = "Ubicación no disponible",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = {
                    val patientId = FirebaseAuth.getInstance().currentUser?.uid // Obtener el ID del paciente
                    if (psychologistId != null && patientId != null) {
                        navController.navigate("agendarCita/${psychologistId}/${patientId}") // Navegar a la pantalla de agendar con los dos IDs
                    } else {
                        //nada
                    }
                }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Agendar cita")
                }

                OutlinedButton(onClick = { psychologistId?.let { navController.navigate("chatWithPsychologist/$it") } }) {
                    Icon(Icons.Default.Chat, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chat")
                }
            }
        }
    }
@Composable
fun NewReviewSection(
    rating: Int,
    tags: List<String>,
    onRatingChange: (Int) -> Unit,
    onTagToggle: (String) -> Unit,
    onSubmit: () -> Unit,
    isSubmitting: Boolean
) {
    // Estado que verifica si el usuario ha intentado enviar la calificación
    var isSubmitAttempted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Text("Califica al psicólogo", style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.Center) {
            (1..5).forEach { i ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (i <= rating) Color.Yellow else Color.Gray,
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { onRatingChange(i) }
                )
            }
        }

        if (isSubmitAttempted && rating == 0) {
            Text(
                text = "Por favor, selecciona una calificación antes de enviar.",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
        Text("Define al psicólogo", style = MaterialTheme.typography.bodyLarge)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            val availableTags = listOf(
                "Empático", "Buen oyente", "Profesional", "Amable",
                "Comprensivo", "Paciente", "Confiable", "Respetuoso",
                "Comprometido", "Asertivo",
                "Honesto", "Motivador", "Amigable", "Flexible",
                "Sensible", "Discreto", "Dedicado", "Responsable",
                "Analítico", "Claro en la comunicación",
                "Ético", "Optimista"
            )
            items(availableTags) { tag ->
                AssistChip(
                    onClick = { onTagToggle(tag) },
                    label = { Text(tag) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (tags.contains(tag)) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary
                    )
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isSubmitAttempted = true
                if (rating > 0) {
                    onSubmit()
                }
            },
            enabled = rating > 0 && !isSubmitting
        ) {
            Text(if (isSubmitting) "Enviando..." else "Enviar calificación")
        }
    }
}
