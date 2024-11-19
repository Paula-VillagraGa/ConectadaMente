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
import com.example.conectadamente.components.NavigationInferior
import com.example.conectadamente.ui.theme.*



@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar() },
        bottomBar = { BottomNavigationBar(navController) }
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

// Barra de navegación inferior
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationInferior(navController)
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

// Sección de contenido recomendado
@Composable
fun ContentSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Recomendaciones para ti",
            color = Blue30,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item { RecommendationCard("Relaxing Soundscapes", R.drawable.ic_google) }
            item { RecommendationCard("Meditative Art", R.drawable.ico_perfil) }
            item { RecommendationCard("Stress Relief Music", R.drawable.ico_forma) }
        }
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
            painter = painterResource(id = R.drawable.emocion_triste),
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


// Tarjetas de soporte
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
            Text(text = title, color = Color(0xFF100E1B), fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController() // Simula un NavHostController para la vista previa
    HomeScreen(navController = navController)
}
