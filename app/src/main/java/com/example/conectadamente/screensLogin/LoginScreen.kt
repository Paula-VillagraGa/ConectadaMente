package com.example.conectadamente.screensLogin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.R


@Composable
fun LoginScreen(navigateToSignIn: () ->Unit, navigateToGoogleSignIn:() -> Unit, navigateToCreateAccount: ()-> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.salud_mental), // Reemplaza con el nombre de tu imagen
            contentDescription = "Logo",
            modifier = Modifier
                .size(150.dp) // Ajusta el tamaño de la imagen según tus necesidades
                .padding(bottom = 32.dp) // Espacio debajo de la imagen
        )

        // Botón para ir a la pantalla de iniciar sesión
        Button(
            onClick = { navigateToSignIn() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = "Iniciar Sesión", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para ingresar con cuenta de Google
        Button(
            onClick = { navigateToGoogleSignIn() }, // Añadir la navegación a la pantalla de Google
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDB4437))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ícono de Google
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Ingresar con Google", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto cliqueable para crear cuenta
        Text(
            text = "¿No tienes cuenta? Crear Cuenta",
            modifier = Modifier.clickable {
                navigateToCreateAccount() // Navegar a la pantalla de crear cuenta
            },
            style = TextStyle(
                color = Color(0xFF6200EE),
                fontSize = 16.sp
            )
        )
    }
}
