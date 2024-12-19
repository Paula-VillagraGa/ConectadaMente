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

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfesionalPerfilReseñasScreen(
    navController: NavController,  // Cambio a NavController
    psycho: PsychoModel,          // Recibe el modelo de psicólogo
    reviews: List<ReviewModel>    // Recibe la lista de reseñas
) {
    // Mostrar indicador de carga si los datos aún no están listos
    if (psycho == null || reviews == null) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Reseñas al Profesional") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Acción de retroceso
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Go Back"
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
            // Sección superior: Foto, nombre y experiencia
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) { // Imagen del perfil
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
                    text = "${psycho.therapy}, ${psycho.experience} years experience",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sección de calificaciones y desglose
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${psycho.rating}",
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

                Column(modifier = Modifier.weight(2f)) {
                    RatingBreakdown(
                        breakdown = List(5) { index -> reviews.count { review -> review.rating.toInt() == index + 1 } }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título de reseñas
            Text(
                text = "Reseñas",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )


            reviews.forEach { review ->
                ReviewItem(
                    photoRes = android.R.drawable.ic_menu_gallery,
                    userName = review.patientId,
                    date = SimpleDateFormat("MMM yyyy").format(Date(review.timestamp)),
                    rating = review.rating.toInt(),
                    comment = review.tags.toString()
                )
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
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
