package com.example.conectadamente.ui.authPaciente

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.conectadamente.R
import com.example.conectadamente.ui.theme.PoppinsFontFamily
import com.example.conectadamente.ui.theme.*
import com.example.conectadamente.ui.viewModel.AuthViewModel
import com.example.conectadamente.utils.constants.DataState

@Composable
fun SignInScreen(
    navigateToRegisterPacient: () -> Unit = {},
    navigateToHomeScreen: () -> Unit = {},
    navigateToPsychoProfile: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val viewModel: AuthViewModel = hiltViewModel()
    val loginState by viewModel.loginState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize() // Ocupa toda la pantalla
    ) {
        // Fondo dividido en dos
        Column(modifier = Modifier.fillMaxSize()) {
            // Parte superior
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Purple30)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    for (i in 0 until 5) {
                        drawCircle(
                            color = Purple60,
                            radius = 40f,
                            center = Offset(
                                x = (100f * i + 50f),
                                y = (200f * i + 30f)
                            )
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.usuario1),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(bottom = 16.dp)
                    )
                    Text(
                        text = "Iniciar Sesión",
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily,
                            fontStyle = FontStyle.Normal,
                            fontSize = 30.sp,
                            color = Color.White,
                        ),
                        textAlign = TextAlign.Left // Centrar el texto
                    )
                }
            }

            // Parte inferior (mitad de abajo)
            Box(
                modifier = Modifier
                    .weight(1f) // Ocupa la mitad de la pantalla
                    .fillMaxWidth()
                    .background(Color(0xFFD7D7D7))
            )
        }

        // Box centrado con el formulario de login
        Box(
            modifier = Modifier
                .align(Alignment.Center) // Centra el formulario en toda la pantalla
                .padding(16.dp)
                .padding(top = 100.dp)
                .background(color = Color.White, shape = RoundedCornerShape(16.dp)) // Fondo blanco con bordes redondeados
                .fillMaxWidth(0.9f) // Ocupa el 90% del ancho de la pantalla
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos dentro del Column
            ) {
                // Campo para ingresar el correo electrónico
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
                // Campo para ingresar la contraseña
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(), // Para ocultar la contraseña
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón de iniciar sesión
                Button(onClick = {
                    viewModel.handleLogin(email, password) // Llamar al método handleLogin desde el ViewModel
                }) {
                    Text("Iniciar sesión")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mensaje de error
                Text(
                    text = message,
                    color = Purple60,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Texto para crear una cuenta
                Text(
                    text = "¿No tienes cuenta? Crear Cuenta",
                    modifier = Modifier.clickable { navigateToRegisterPacient() }, // Navegar a la pantalla de crear cuenta
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

    // Comprobación del estado de login
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is DataState.Success -> {
                // Navegar según el rol
                when (state.data) {
                    "psicologo" -> navigateToPsychoProfile()
                    "paciente" -> navigateToHomeScreen()
                    "unverified" -> message = "Cuenta no verificada. Por favor, espera la validación."
                    "none" -> message = "No tienes una cuenta registrada."
                    else -> message = "Ocurrió un error inesperado."
                }
            }
            is DataState.Error -> {
                message = state.e?: "Error desconocido"
            }
            else -> {}
        }
    }
}
