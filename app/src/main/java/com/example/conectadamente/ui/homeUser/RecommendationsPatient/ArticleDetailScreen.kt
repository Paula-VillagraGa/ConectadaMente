package com.example.conectadamente.ui.homeUser.RecommendationsPatient

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.conectadamente.data.model.ArticleModel


@Composable
fun ArticleDetailScreen(article: ArticleModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = article.title,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = article.summary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Aquí puedes agregar más contenido, como el cuerpo completo de la noticia, fecha, etc.
        Text(
            text = "Fecha: ${article.date}",
            style = MaterialTheme.typography.bodyMedium
        )

        Image(
            painter = rememberImagePainter(article.imageUrl),
            contentDescription = article.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 16.dp)
        )
    }
}
