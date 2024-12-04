package com.example.conectadamente.ui.homeUser.RecommendationsPatient

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.conectadamente.data.model.ArticleModel
import com.example.conectadamente.ui.viewModel.recommendations.ArticleViewModel


@Composable
fun ArticleScreen(viewModel: ArticleViewModel = hiltViewModel()) {
    val articles = viewModel.articles.value

    ArticleList(articles = articles)
}
@Composable
fun ArticleList(articles: List<ArticleModel>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(articles) { article ->
            ArticleCard(
                title = article.title,
                summary = article.summary,
                imageRes = article.imageUrl,  // Usamos imageUrl como String (URL de imagen)
                onReadMoreClick = { /* Acción al presionar "Leer más" */ }
            )
        }
    }
}

@Composable
fun ArticleCard(
    title: String,
    summary: String,
    imageRes: String,
    onReadMoreClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Imagen del artículo
            Image(
                painter = rememberImagePainter(imageRes),  // Usamos rememberImagePainter para cargar la imagen desde URL
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(120.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )
            // Información del artículo
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Button(
                    onClick = { onReadMoreClick() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Leer más")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewArticleCard() {
    ArticleCard(
        title = "Los secretos del cerebro humano",
        summary = "Descubre cómo funciona el cerebro y su impacto en la salud mental.",
        imageRes = "https://via.placeholder.com/150",  // URL de ejemplo
        onReadMoreClick = { /* Acción al presionar "Leer más" */ }
    )
}