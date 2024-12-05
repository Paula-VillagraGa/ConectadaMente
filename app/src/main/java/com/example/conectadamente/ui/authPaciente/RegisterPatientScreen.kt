package com.example.conectadamente.ui.authPaciente


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.conectadamente.R
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.ui.theme.*
import com.example.conectadamente.ui.viewModel.UserAuthViewModel
import com.example.conectadamente.utils.constants.DataState
import com.example.conectadamente.utils.validateRegistrationData



@Composable
fun RegisterPatientScreen(viewModel: UserAuthViewModel = hiltViewModel(),  navigateToRegisterPsycho: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val registerState by viewModel.authState.collectAsState()


    //request
    val focusRequesterRut = FocusRequester()
    val focusRequesterEmail = FocusRequester()
    val focusRequesterPassword = FocusRequester()
    val focusRequesterPasswordB = FocusRequester()

    val rutFocused = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize() // Ocupa toda la pantalla
    ) {
        // Fondo dividido en dos mitades
        Column(modifier = Modifier.fillMaxSize()) {
            // Parte superior (mitad de arriba)
            Box(
                modifier = Modifier
                    .weight(1f) // Ocupa la mitad de la pantalla
                    .fillMaxWidth()
                    .background(Purple60)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 30.dp), // Ajuste para posicionar más abajo
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(16.dp)) // Espaciado opcional

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
        // Botón en la esquina superior derecha
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Button(
                onClick = { navigateToRegisterPsycho() },
                modifier = Modifier.wrapContentWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple20
                )
            ) {
                Text(
                    text = "Registrar como Psicólogo",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp
                    )
                )
            }
        }

        // Box centrado con el formulario de registro
        Box(
            modifier = Modifier
                .align(Alignment.Center) // Centra el formulario en toda la pantalla
                .padding(16.dp)
                .background(
                    color = Color(0xFFEAEAEA),
                    shape = RoundedCornerShape(16.dp)
                ) // Fondo blanco con bordes redondeados
                .fillMaxWidth(0.9f) // Ocupa el 90% del ancho de la pantalla
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.relajarse1),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(150.dp) // Tamaño de la imagen
                        .padding(bottom = 6.dp)
                )
                Text(
                    text = "Regístrate",
                    style = TextStyle(
                        fontFamily = PoppinsFontFamily,
                        fontStyle = FontStyle.Italic,
                        fontSize = 30.sp,
                        color = Purple40,
                    ),
                    textAlign = TextAlign.Center // Centrar el texto
                )

                // Campo para el nombre
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth()
                    .focusRequester(FocusRequester.Default),
                    keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words,imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusRequesterRut.requestFocus() })
                )

                // Campo para el RUT
                OutlinedTextField(
                    value = rut,
                    onValueChange = { rut = it },
                    label = { Text("RUT") },
                    modifier = Modifier.fillMaxWidth()
                        .focusRequester(focusRequesterRut)
                        .onFocusChanged { rutFocused.value = it.isFocused },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusRequesterEmail.requestFocus() })
                )
                // Mostrar mensaje si el campo de RUT está enfocado
                if (rutFocused.value) {
                    Text(
                        text = "Ingrese el RUT sin puntos y con guión",
                        style = TextStyle(color = Color.Gray, fontSize = 12.sp), // Estilo del texto
                        modifier = Modifier.padding(top = 4.dp) // Espaciado entre el campo y el mensaje
                    )
                }

                // Campo para el correo electrónico
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier.fillMaxWidth()
                        .focusRequester(focusRequesterEmail),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusRequesterPassword.requestFocus() })
                )

                // Campo para la contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                        .focusRequester((focusRequesterPassword)),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusRequesterPasswordB.requestFocus() })
                )

                // Campo para confirmar la contraseña
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                        .focusRequester((focusRequesterPasswordB)),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { })
                )

                // Botón de registro
                Button(
                    onClick = {
                        // Validaciones
                        val validationMessage = validateRegistrationData(name, rut, email, password, confirmPassword)
                        if (validationMessage != null) {
                            message = validationMessage
                        }
                            else {
                                val patient = PatientModel(
                                    email = email,
                                    rut = rut,
                                    name = name
                                )
                                viewModel.registerPatient(patient, password)
                            }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple50)
                ) {
                    Text("Registrar")
                }

                // Mostrar mensajes según el estado
                when (registerState) {
                    is DataState.Loading -> Text("Registrando...", color = Color.Gray)
                    is DataState.Success -> Text(
                        (registerState as DataState.Success).data,
                        color = Purple50
                    )
                    is DataState.Error -> Text(
                        "Error: ${(registerState as DataState.Error).e}",
                        color = Purple50
                    )
                    DataState.Finished -> {}
                    DataState.Idle -> {}
                }

                Text(
                    text = message,
                    color = Blue40,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

