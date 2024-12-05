package com.example.conectadamente.ui.homeUser.recommendationsPatient

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.conectadamente.ui.theme.Purple20
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SosDialCardScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text= "Números de Ayuda",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.secondary, // Cambia el color
                        fontStyle = FontStyle.Italic, // Cambia el estilo de la fuente
                        fontWeight = FontWeight.Bold // Cambia el peso de la fuente
                    ) ) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Carrusel en la parte superior
                FirebaseImageCarouselWithAutoScroll(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Grid de números SOS debajo
                SosDialCardGrid(
                    modifier = Modifier.weight(1f)
                )
            }
        }
    )
}
@Composable
fun FirebaseImageCarouselWithAutoScroll(modifier: Modifier) {
    val imageUrls = remember { mutableStateListOf<String>() }

    // Cargar imágenes desde Firebase Storage
    LaunchedEffect(Unit) {
        val storage = FirebaseStorage.getInstance()
        val imagesRef = storage.reference.child("sos") // Carpeta en Storage

        imagesRef.listAll().addOnSuccessListener { listResult ->
            listResult.items.forEach { item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    imageUrls.add(uri.toString())
                }
            }
        }
    }

    if (imageUrls.isNotEmpty()) {
        AutoScrollingCarousel(imageUrls)
    } else {
        // Mostrar indicador de carga mientras se obtienen las URLs
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
@Composable
fun AutoScrollingCarousel(imageUrls: List<String>, scrollInterval: Long = 3000L) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(imageUrls) {
        if (imageUrls.isNotEmpty()) {
            while (true) {
                delay(scrollInterval)
                val nextIndex = (lazyListState.firstVisibleItemIndex + 1) % imageUrls.size
                coroutineScope.launch {
                    lazyListState.animateScrollToItem(nextIndex)
                }
            }
        }
    }

    LazyRow(
        state = lazyListState,
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(imageUrls) { imageUrl ->
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Imagen",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(400.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun SosDialCardGrid(modifier: Modifier = Modifier) {
    val sosNumbers = listOf(
        "Salud Responde: 6003607777",
        "Línea Prevención del Suicidio: *4141",
        "Fono Drogas y Alcohol SENDA: 1412",
        "Línea de Atención de Salud Mental: 800123456"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Dos columnas
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(sosNumbers) { entry ->
                val (title, number) = entry.split(": ")
                DialCard(title, number)
            }
        }
    }
}

@Composable
fun DialCard(title: String, phoneNumber: String) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth(0.4f) // Ancho reducido de la tarjeta
            .height(120.dp) // Ajusta la altura de la tarjeta
            .clickable {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            },
        elevation = CardDefaults.cardElevation(6.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Purple20) // Cambiar color de fondo aquí
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), // Menos padding para tarjetas más pequeñas
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp)) // Espacio más pequeño entre el título y el número
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = Color.White,
            )
        }

        }
    }
}