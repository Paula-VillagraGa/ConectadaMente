package com.example.conectadamente.ui.homePsycho

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.R
import com.example.conectadamente.navegation.NavScreen
import com.example.conectadamente.ui.theme.*
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


@Composable
fun PsychoHomeScreen(navController: NavHostController) {
    BackHandler {
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(navController = navController) // Usamos tu TopAppBar personalizado aquí
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
fun TopAppBar(navController: NavHostController) {
    val context = LocalContext.current
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
                    onClick = {
                        Firebase.auth.signOut()
                        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                        navController.navigate(NavScreen.Login.route) {
                            popUpTo(NavScreen.Home.route) { inclusive = true } // Limpia la pila de navegación
                        }
                    },
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
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(40.dp))
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


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SupportCard(
                title = "Mis Pacientes",
                subtitle = "Tus Pacientes",
                imageRes = R.drawable.familia,
                onClick = { navController.navigate(NavScreen.CompletedAppointments.route) },
                modifier = Modifier
                    .weight(0.6f)
                    .height(250.dp),
                color = Purple50
            )
            val psychoId = FirebaseAuth.getInstance().currentUser?.uid
            SupportCard(
                title = "Reseñas",
                subtitle = "Tus Reseñas",
                imageRes = R.drawable.cartas2,
                onClick = { navController.navigate(NavScreen.ReseñasDetalle.route + "/$psychoId")},
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
    val navController = rememberNavController()

    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        PsychoHomeScreen(navController)
    }
}