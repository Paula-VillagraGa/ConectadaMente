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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.conectadamente.R
import com.example.conectadamente.ui.theme.PoppinsFontFamily
import com.example.conectadamente.ui.theme.*
import com.example.conectadamente.ui.viewModel.AuthViewModel
import com.example.conectadamente.utils.constants.DataState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navigateToRegisterPacient: () -> Unit = {},
    navigateToHomeScreen: () -> Unit = {},
    navigateToPsychoHomeScreen: () -> Unit,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val emailFocusRequester = FocusRequester()
    val passwordFocusRequester = FocusRequester()

    val viewModel: AuthViewModel = hiltViewModel()
    val loginState by viewModel.loginState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current // Controlador del teclado

    Scaffold(
        modifier = Modifier.padding(0.dp),
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    navigationIconContentColor = Color.White, // Color del ícono de navegación
                    titleContentColor = Color.White // Color del título
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize() // Ocupa toda la pantalla
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp),
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
                        .background(Color.White)
                )
            }

            // Box centrado con el formulario de login
            Box(
                modifier = Modifier
                    .align(Alignment.Center) // Centra el formulario en toda la pantalla
                    .padding(16.dp)
                    .padding(top = 100.dp)
                    .background(
                        color = Color(0xFFFAF3F3),
                        shape = RoundedCornerShape(16.dp)
                    ) // Fondo blanco con bordes redondeados
                    .fillMaxWidth(0.9f) // Ocupa el 90% del ancho de la pantalla
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos dentro del Column
                ) {
                    TextField(
                        value = email,
                        onValueChange = { email = it.trim().lowercase() }, // Convierte a minúsculas y elimina espacios
                        label = { Text("Correo Electrónico") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(emailFocusRequester),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email, // Configura el teclado para correos electrónicos
                            imeAction = ImeAction.Next // Cambia de campo cuando se presiona "Enter"
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { passwordFocusRequester.requestFocus() } // Cambia al campo de contraseña
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Brown10,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Campo para ingresar la contraseña
                    TextField(
                        value = password,
                        onValueChange = { password = it.trim() }, // Elimina los espacios en blanco
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(), // Para ocultar la contraseña
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done // Acción para finalizar
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide() // Ocultar teclado
                                viewModel.handleLogin(email, password) // Iniciar sesión cuando se presiona "Done"
                            }
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Brown10,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón de iniciar sesión
                    Button(onClick = {
                        viewModel.handleLogin(email, password)
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

        var isLoading by remember { mutableStateOf(true) }

        // Comprobación del estado de login
        LaunchedEffect(loginState) {
            when (val state = loginState) {
                is DataState.Loading -> {
                    isLoading = true // Iniciar el loading
                }

                is DataState.Success -> {
                    isLoading = false // Detener el loading
                    // Aquí va la lógica para navegar según el rol del usuario
                    when (state.data) {
                        "psicologo" -> navigateToPsychoHomeScreen()
                        "paciente" -> navigateToHomeScreen()
                        "unverified" -> message =
                            "Cuenta no verificada. Por favor, espera la validación."

                        "none" -> message = "No tienes una cuenta registrada."
                        else -> message = "Ocurrió un error inesperado."
                    }
                }

                is DataState.Error -> {
                    isLoading = false
                    message = state.e
                }

                else -> {
                    isLoading = false // Detener el loading
                }
            }
        }

        // UI con el CircularProgressIndicator
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (isLoading) {
                CircularProgressIndicator() // Mostrar el loading mientras se está cargando
            } else {
                // Mostrar el mensaje o la siguiente vista después de la carga
                Text(text = message, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
