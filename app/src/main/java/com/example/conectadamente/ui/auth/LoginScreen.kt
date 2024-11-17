package com.example.conectadamente.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectadamente.R
import com.example.conectadamente.ui.theme.*



@Composable
fun LoginScreen(navController: NavController, navigateToSignIn: () ->Unit, navigateToGoogleSignIn:() -> Unit, navigateToCreateAccount: ()-> Unit, navigateToRegisterPatient:()-> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Espaciado alrededor del Box
            contentAlignment = Alignment.Center // Centra el contenido dentro del Box
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally // Alinea la imagen y el texto al centro dentro del Column
            ) {
                Image(
                    painter = painterResource(id = R.drawable.meditacion), // Reemplaza con tu imagen
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(300.dp) // Tamaño de la imagen
                        .padding(bottom = 16.dp) // Espacio entre la imagen y el texto
                )
                Text(
                    text = "Bienvenido a ConectadaMente", // Reemplaza con tu texto
                    style = TextStyle(
                        fontFamily = PoppinsFontFamily,   // Usar la familia de fuentes definida
                        fontStyle = FontStyle.Italic,    // Asegura que se usa el estilo itálico
                        fontSize = 30.sp,                // Tamaño de la fuente
                        color = Purple20,
                    ),
                    textAlign = TextAlign.Center // Centrar el texto
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Botón para ir a la pantalla de iniciar sesión
        Button(
            onClick = { navController.navigate("sign_in") },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),

            colors = ButtonDefaults.buttonColors(containerColor = Purple20)
        ) {
            Text(text = "Iniciar Sesión", style = TextStyle(color = Color.White,fontWeight = FontWeight.Normal,
                fontFamily = PoppinsFontFamily, fontStyle = FontStyle.Normal, fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para registrarse
        Button(
            onClick = { navigateToRegisterPatient() }, //Registrar Paciente
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple30)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ícono de Google
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Registrarse",  style = TextStyle(color = Color.White,fontWeight = FontWeight.Normal,
                    fontFamily = PoppinsFontFamily, fontStyle = FontStyle.Normal, fontSize = 16.sp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))



    }
}
