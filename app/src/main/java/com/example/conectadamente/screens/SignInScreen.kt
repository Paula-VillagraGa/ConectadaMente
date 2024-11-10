package com.example.conectadamente.screens

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun SignInScreen(navController: NavController) {
    val email = remember { TextFieldValue() }
    val password = remember { TextFieldValue() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo para ingresar el email

        TextField(
            value = "",
            onValueChange = { /* Acción para actualizar el texto */ },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo para ingresar la contraseña

        TextField(
            value = "",
            onValueChange = { /* Acción para actualizar el texto */ },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )


        // Botón de iniciar sesión
        Button(
            onClick = {
                // Acción para iniciar sesión
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = "Iniciar Sesión", color = Color.White)
        }

        // Regresar a la pantalla de login
        Text(
            text = "¿No tienes cuenta? Crear Cuenta",
            modifier = Modifier.clickable {
                navController.navigate("create_account_screen") // Navegar a la pantalla de crear cuenta
            },
            style = TextStyle(
                color = Color(0xFF6200EE),
                fontSize = 16.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    SignInScreen(navController = rememberNavController()) // Usando NavController para navegar
}
