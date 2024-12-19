package com.example.conectadamente.ui.authPsicologo

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.conectadamente.R
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.data.model.ReviewModel
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat", "DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfesionalPerfilReseñasScreen(
    navController: NavController,
    psycho: PsychoModel?,
    reviews: List<ReviewModel>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Reseñas al Profesional") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (psycho == null) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else {
                // Información del psicólogo
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = psycho.photoUrl ?: R.drawable.ico_perfil,
                        contentDescription = "Foto del Psicólogo",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = psycho.name,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${psycho.therapy}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Especialización: ${
                            psycho.specialization?.joinToString(", ") ?: "N/A"
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Resumen de las reseñas
                val averageRating = if (reviews.isNotEmpty()) {
                    reviews.sumOf { it.rating } / reviews.size
                } else {
                    0.0
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = String.format("%.1f", averageRating),
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Promedio",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "${reviews.size} reseñas",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Desglose de calificaciones
                    Column(modifier = Modifier.weight(2f)) {
                        RatingBreakdown(
                            breakdown = List(5) { index ->
                                val count = reviews.count { review -> review.rating.toInt() == index + 1 }
                                if (reviews.isNotEmpty()) count * 100 / reviews.size else 0
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Listado de reseñas
                Text(
                    text = "Reseñas",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                reviews.forEach { review ->
                    val formattedDate = SimpleDateFormat("dd MMM yyyy").format(Date(review.timestamp))
                    ReviewItem(
                        photoRes = R.drawable.lavanda, // Placeholder de imagen
                        userName = review.patientName ?: "Desconocido",
                        date = formattedDate,
                        rating = review.rating.toInt(),
                        comment = review.tags.joinToString(", ")
                    )
                }
            }
        }
    }
}


@Composable
fun RatingBreakdown(breakdown: List<Int>) {
    val totalPercentage = 100
    Column {
        breakdown.reversed().forEachIndexed { index, percentage ->
            val stars = 5 - index
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$stars",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(20.dp)
                )
                LinearProgressIndicator(
                    progress = percentage / totalPercentage.toFloat(),
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ReviewItem(
    photoRes: Int,
    userName: String,
    date: String,
    rating: Int,
    comment: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(id = photoRes),
                contentDescription = "Foto del usuario",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                RatingBar(rating = rating.toFloat())
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = comment,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Composable
fun RatingBar(rating: Float) {
    Row {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < rating.toInt()) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Estrella",
                tint = if (index < rating.toInt()) Color.Yellow else Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}