package com.example.conectadamente.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.ui.theme.*
import com.example.conectadamente.R



import com.example.conectadamente.ui.home.HomeScreen


@Composable
fun SignInScreen(navigateToRegisterPacient : ()-> Unit ={}, navigateToHomeScreen: () -> Unit ={}) {
    val email = remember { TextFieldValue() }
    val password = remember { TextFieldValue() }

    Box(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda la pantalla
    ) {
        // Fondo dividido en dos mitades
        Column(modifier = Modifier.fillMaxSize()) {
            // Parte superior (mitad de arriba)
            Box(
                modifier = Modifier
                    .weight(1f) // Ocupa la mitad de la pantalla
                    .fillMaxWidth()
                    .background(Purple30)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp), // Ajuste para posicionar más abajo
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(16.dp)) // Espaciado opcional
                    Image(
                        painter = painterResource(id = R.drawable.logo3),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(80.dp) // Tamaño de la imagen
                            .padding(bottom = 16.dp) // Espacio entre la imagen y el texto)
                    )
                    Text(
                        text = "Iniciar Sesión", // Reemplaza con tu texto
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily,   // Usar la familia de fuentes definida
                            fontStyle = FontStyle.Italic,    // Asegura que se usa el estilo itálico
                            fontSize = 30.sp,                // Tamaño de la fuente
                            color = Color.White,
                        ),
                        textAlign = TextAlign.Center // Centrar el texto
                    )
                }
            }

            // Parte inferior (mitad de abajo)
            Box(
                modifier = Modifier
                    .weight(1f) // Ocupa la mitad de la pantalla
                    .fillMaxWidth()
                    .background(Color(0xFFD7D7D7)) // Lila claro
            )
        }

        // Box centrado con el formulario de login
        Box(
            modifier = Modifier
                .align(Alignment.Center) // Centra el formulario en toda la pantalla
                .padding(16.dp)
                .background(color = Color.White, shape = RoundedCornerShape(16.dp)) // Fondo blanco con bordes redondeados
                .fillMaxWidth(0.9f) // Ocupa el 90% del ancho de la pantalla
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos dentro del Column
            ) {
                // Campo para ingresar el correo electrónico
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

                Spacer(modifier = Modifier.height(20.dp))

                // Botón de iniciar sesión
                Button(
                    onClick = { navigateToHomeScreen() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple20)
                ) {
                    Text(text = "Iniciar Sesión", color = Color.White)
                }

                // Texto para crear una cuenta
                Text(
                    text = "¿No tienes cuenta? Crear Cuenta",
                    modifier = Modifier.clickable {
                        navigateToRegisterPacient() // Navegar a la pantalla de crear cuenta
                    },
                    style = TextStyle(
                        color = Purple40,
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily,
                        fontStyle = FontStyle.Normal

                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    // Envuelve tu pantalla en un tema si lo estás utilizando
    MyApplicationTheme {
        // Llama a SignInScreen pasando una función vacía como lambda para evitar errores de navegación
        SignInScreen()
    }
}
