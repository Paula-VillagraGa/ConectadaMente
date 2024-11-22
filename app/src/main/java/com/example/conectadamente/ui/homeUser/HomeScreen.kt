package com.example.conectadamente.ui.homeUser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.conectadamente.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.navegation.NavigationInferior
import com.example.conectadamente.ui.theme.*



@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar() },
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
                    .background(MaterialTheme.colorScheme.background)
            ) {
                SearchField()
                ContentSection()
                MentalHealthSection()
            }
        }
    }
}

// Barra superior (TopAppBar)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text("Inicio", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        actions = {
            IconButton(onClick = { /* Acción para configuración */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ico_forma),
                    contentDescription = "Settings",
                    tint = Color(0xFF100E1B)
                )
            }
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    )
}



// Campo de búsqueda
@Composable
fun SearchField() {
    OutlinedTextField(
        value = "",
        onValueChange = { /* Acción para el campo de búsqueda */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFE9E7F3), RoundedCornerShape(16.dp)),
        placeholder = {
            Text("Buscar", color = Color(0xFF3E4398))
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ico_recomen),
                contentDescription = "Buscar",
                tint = Color(0xFF5A4E97)
            )
        }
    )
}

// Tarjeta de recomendación utilizando aspectRatio para tamaño flexible
@Composable
fun RecommendationCardHome(title: String, imageRes: Int) {
    Column(
        modifier = Modifier
            .width(200.dp)  // Controla el ancho de la tarjeta
            .padding(8.dp)
            .background(Color(0xFFE9E7F3), RoundedCornerShape(12.dp))
    ) {
        // Imagen con una relación de aspecto más alta que ancha (1:1.5)
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,  // Asegura que la imagen se recorte para llenar el espacio
            modifier = Modifier
                .fillMaxWidth()  // La imagen ocupa todo el ancho disponible
                .aspectRatio(1f / 1.5f)  // Relación de aspecto invertida para que sea más larga que ancha
                .clip(RoundedCornerShape(12.dp))  // Redondea las esquinas de la imagen
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = Color(0xFF100E1B),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
@Composable
fun ContentSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()  // Asegura que la columna ocupe todo el ancho
            .padding(horizontal = 16.dp)
            .height(300.dp)  // Define una altura específica para la sección
    ) {
        Text(
            text = "Recomendaciones para ti",
            color = Blue30,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp)) // Espaciado entre el título y las tarjetas
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth() // Asegura que el LazyRow ocupe todo el ancho
        ) {
            items(3) { index ->
                RecommendationCardHome("Recomendación ${index + 1}", R.drawable.ic_google)
            }
        }

    }
}


@Composable
fun SupportCard(title: String, subtitle: String, imageRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE9E7F3), RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                text = title,
                color = Color(0xFF100E1B),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(text = subtitle, color = Color(0xFF5A4E97), fontSize = 12.sp)
        }
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))

        )
        Spacer(modifier = Modifier.width(16.dp)) // Espacio hacia abajo
    }
}

// Sección de salud mental
@Composable
fun MentalHealthSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Cuidando tu Salud Mental",
            color = Blue40,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        SupportCard("7 ways to manage anxiety", "Article · 3 min read", R.drawable.ico_perfil)
        Spacer(modifier = Modifier.height(8.dp))
        SupportCard("Therapist-led sessions", "Calm, Headspace, and more", R.drawable.ico_recomen)
    }
}

// Tarjetas de recomendación
@Composable
fun RecommendationCard(title: String, imageRes: Int) {
    Column(modifier = Modifier.width(150.dp)) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4 / 3f)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = Purple20,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController() // Simula un NavHostController para la vista previa
    HomeScreen(navController = navController)
}
