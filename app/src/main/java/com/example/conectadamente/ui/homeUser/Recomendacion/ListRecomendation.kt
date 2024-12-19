package com.example.conectadamente.ui.homeUser.Recomendacion

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.conectadamente.navegation.NavScreen
import com.example.conectadamente.ui.viewModel.BuscarPorTagViewModel

@Composable
fun BuscarPorTagScreen(tag: String, navController: NavHostController) {
    // Usamos hiltViewModel para obtener el ViewModel
    val viewModel: BuscarPorTagViewModel = hiltViewModel()

    // Estado para la especialización seleccionada
    val selectedSpecialization = remember { mutableStateOf<String?>(null) }

    // Estado para mostrar/ocultar el popup
    val showPopup = remember { mutableStateOf(false) }

    // Mostrar el popup si el tag es "culpa" o "miedo"
    LaunchedEffect(tag) {
        if (tag == "culpa" || tag == "miedo") {
            showPopup.value = true
        }
    }


    // Llamamos a la función del ViewModel para filtrar los psicólogos por la etiqueta (tag) y especialización
    LaunchedEffect(tag, selectedSpecialization.value) {
        // Establecemos la etiqueta para la búsqueda
        viewModel.tagsQuery.value = listOf(tag)
        // Establecemos la especialización si está seleccionada
        viewModel.specializationQuery.value =
            selectedSpecialization.value?.let { listOf(it) } ?: emptyList()
        // Realizamos la búsqueda
        viewModel.searchPsychologists()
    }

    // Obtenemos la lista de psicólogos filtrados
    val filteredPsychologists = viewModel.filteredPsychologists

    Scaffold(
        topBar = { com.example.conectadamente.ui.homeUser.Recomendacion.TopAppBar() },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEFEFEF))
            ) {
                // Popup
                if (showPopup.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .wrapContentHeight(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Recuerda que hay números telefónicos que pueden ayudarte en casos de emergencia:",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(
                                        onClick = {
                                            // Acción para ir a números de emergencia
                                            navController.navigate(NavScreen.CallSosRecommendations.route)
                                            showPopup.value = false
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "Números de\nEmergencia",
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            // Acción para ver psicólogos
                                            showPopup.value = false
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "Ver\nPsicólogos",
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Fila horizontal con las especializaciones disponibles para selección
                val specializations = listOf(
                    "Clinica",
                    "Infantil",
                    "Organizacional",
                    "Educacional",
                    "Salud",
                    "Deportiva",
                    "Social",
                    "Comunicatorio",
                    "Neuropsicológica",
                    "Terapéutica",
                    "Psicoterapia",
                    "Sexualidad",
                    "Duelo",
                    "Humanista",
                    "Integrativa"
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(specializations) { specialization ->
                        val isSelected = specialization == selectedSpecialization.value
                        FilterChip(
                            text = specialization,
                            isSelected = isSelected,
                            onClick = {
                                selectedSpecialization.value =
                                    if (isSelected) null else specialization
                            }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    if (filteredPsychologists.isEmpty()) {
                        item {
                            Text(
                                text = "No hay psicólogos disponibles.",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    } else {
                        items(filteredPsychologists) { psicologo ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clickable {
                                        // Navegar al detalle del psicólogo
                                        navController.navigate("profile/${psicologo.id}")
                                    },
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Imagen del psicólogo
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(Color.Gray)
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(psicologo.photoUrl)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Imagen de Psicólogo",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            text = psicologo.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = psicologo.specialization?.joinToString(", ")
                                                ?: "",
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
// Composable para los filtros de especialización
@Composable
fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0xFF1980E6) else Color(0xFFF0F2F4))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black,
            style = MaterialTheme.typography.bodySmall
        )
    }
}




