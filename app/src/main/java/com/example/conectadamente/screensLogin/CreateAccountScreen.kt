package com.example.conectadamente.screensLogin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CreateAccountScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F1F1)) // Fondo gris claro
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear Cuenta",
            style = TextStyle(fontSize = 24.sp, color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ejemplo de un campo de texto para el nombre
        TextField(
            value = "",
            onValueChange = { /* Acción para actualizar el texto */ },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para crear cuenta
        Button(
            onClick = { /* Acción para crear cuenta */ },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Crear Cuenta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto cliqueable para ir de vuelta a la pantalla de inicio de sesión
        Text(
            text = "¿Ya tienes cuenta? Iniciar sesión",
            modifier = Modifier.clickable {
                navController.popBackStack() // Volver a la pantalla anterior (login)
            },
            style = TextStyle(color = Color(0xFF6200EE))
        )
    }
}
