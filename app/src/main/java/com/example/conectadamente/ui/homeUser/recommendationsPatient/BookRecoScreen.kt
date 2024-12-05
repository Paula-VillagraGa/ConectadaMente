package com.example.conectadamente.ui.homeUser.recommendationsPatient

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.conectadamente.data.model.RecommendationModel
import com.example.conectadamente.ui.viewModel.recommendations.BookViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookRecommendations(navController: NavController) {

    val bookViewModel: BookViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        bookViewModel.fetchRecommendations()
    }
    val recommendations = bookViewModel.recommendations.value
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text= "Lecturas Recomendadas",
                    style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.secondary, // Cambia el color
                    fontStyle = FontStyle.Italic, // Cambia el estilo de la fuente
                    fontWeight = FontWeight.Bold // Cambia el peso de la fuente
                ) ) },

                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Volver atrás")
                    }
                }
            )
        }
    ) {
        BookRecommendationList(recommendations = recommendations)
    }
}


@Composable
fun BookRecommendationList(recommendations: List<RecommendationModel>) {
    val categories = recommendations.groupBy { it.category }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .padding(top = 15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        categories.forEach { (category, books) ->
            Text(
                text = category,
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary),
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(books) { recommendation ->
                    BookRecommendationCard(recommendation)
                }
            }
        }
    }
}

@Composable
fun BookRecommendationCard(recommendation: RecommendationModel) {
    Card(
        modifier = Modifier
            .width(180.dp) // Hacemos la tarjeta más pequeña
            .height(270.dp), // Ajustamos la altura
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // Imagen del libro
            Image(
                painter = rememberImagePainter(recommendation.imageRes),
                contentDescription = recommendation.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Text(
                text = recommendation.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontStyle = FontStyle.Italic,
            )
            Text(
                    text = recommendation.autor,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
            )


            Text(
                text = recommendation.description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewBookRecommendations() {
    val recommendations = listOf(
        RecommendationModel(
            category = "Psicología",
            imageRes = "https://via.placeholder.com/150", // Usa una URL o un recurso local
            title = "El poder de la mente",
            autor = "Recomendado",
            description = "Una descripción detallada del libro de ejemplo"
        ),
        RecommendationModel(
            category = "Salud Mental",
            imageRes = "https://via.placeholder.com/150", // Usa una URL o un recurso local
            title = "La mente sana",
            autor = "Lectura sugerida",
            description = "Una descripción detallada del libro de ejemplo"
        ),
        RecommendationModel(
            category = "Psicología",
            imageRes = "https://via.placeholder.com/150", // Usa una URL o un recurso local
            title = "Crecimiento personal",
            autor = "Recomendado",
            description = "Una descripción detallada del libro de ejemplo"
        )
    )

    BookRecommendationList(recommendations = recommendations)
}
