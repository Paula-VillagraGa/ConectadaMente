package com.example.conectadamente.ui.homePsycho

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.R
import com.example.conectadamente.navegation.NavScreen
import com.example.conectadamente.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PsychoHomeScreen(navController: NavHostController) {
    // Habilitar la navegación hacia atrás
    BackHandler {
        navController.popBackStack() // Navega hacia atrás en el stack de navegación
    }

    Scaffold(
        topBar = {
            TopAppBar() // Usamos tu TopAppBar personalizado aquí
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PsychologistHomeContent(navController)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    CenterAlignedTopAppBar(
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.logo1), // Reemplaza con tu recurso de imagen
                contentDescription = "Logo",
                modifier = Modifier
                    .size(220.dp) // Ajusta el tamaño según necesites
                    .padding(start = 8.dp)
                    .padding(top = 30.dp) // Aplica un recorte circular si es necesario
            )
        },
        title = {
            Text(
                text = "", // Puedes añadir un título si lo necesitas
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Acción para configuración */ },
                    modifier = Modifier.padding(top = 25.dp) // Ajusta el padding superior para bajar el ícono
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu, // Ícono predeterminado de configuración
                        contentDescription = "Settings",
                        tint = Color(0xFF100E1B)
                    )
                }
            }
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    )
}


    @Composable
fun PsychologistHomeContent(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Fila superior con 2 tarjetas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SupportCard(
                title = "Psicología Clínica",
                subtitle = "Tu Disponibilidad",
                imageRes = R.drawable.calendario, // Cambia la imagen
                onClick = { navController.navigate(NavScreen.DisponibilidadCalendario.route) },
                modifier = Modifier
                    .weight(0.6f)
                    .height(250.dp),
                color = Purple60
            )
            SupportCard(
                title = "Terapias Online",
                subtitle = "Tus Citas",
                imageRes = R.drawable.trabajos, // Cambia la imagen
                onClick = { navController.navigate(NavScreen.CitasReservadas.route)},
                modifier = Modifier
                    .height(250.dp)
                    .weight(0.7f),
                color = Purple20
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fila del medio con 2 tarjetas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SupportCard(
                title = "Terapia Infantil",
                subtitle = "Para niños y adolescentes",
                imageRes = R.drawable.ico_chat, // Cambia la imagen
                onClick = { /* Acción para terapia infantil */ },
                modifier = Modifier
                    .weight(0.6f)
                    .height(250.dp),
                color = Color(0xFFFFF59D)
            )
            SupportCard(
                title = "Mindfulness",
                subtitle = "Enfoque en la meditación",
                imageRes = R.drawable.ico_chat, // Cambia la imagen
                onClick = { /* Acción para mindfulness */ },
                modifier = Modifier
                    .weight(0.5f)
                    .height(250.dp),
                    color = Purple30
            )
        }
    }
}

@Composable
fun SupportCard(
    title: String,
    subtitle: String,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFE9E7F3)
) {
    Column(
        modifier = modifier
            .background(color, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(bottom = 10.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth() // Ajusta la imagen al ancho disponible
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PsychologistHomeContentPreview() {
    // Usamos rememberNavController para emular la navegación, aunque no se está utilizando en el preview
    val navController = rememberNavController()

    // El colorScheme se puede definir para que tenga un tema claro en el preview
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        // Llamamos a PsychologistHomeContent, que es el composable que deseas previsualizar
        PsychoHomeScreen(navController)
    }
}