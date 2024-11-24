package com.example.conectadamente.ui.authPaciente


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
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
fun RegisterPatientScreen(viewModel: UserAuthViewModel = hiltViewModel()) {
    var name by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val registerState by viewModel.authState.collectAsState()


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
                    .background(Purple10)
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
                    .background(Color(0xFFD7D7D7))
            )
        }

        // Box centrado con el formulario de registro
        Box(
            modifier = Modifier
                .align(Alignment.Center) // Centra el formulario en toda la pantalla
                .padding(16.dp)
                .background(
                    color = Color.White,
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
                )

                // Campo para el RUT
                OutlinedTextField(
                    value = rut,
                    onValueChange = { rut = it },
                    label = { Text("RUT") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo para el correo electrónico
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo para la contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo para confirmar la contraseña
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
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
                    colors = ButtonDefaults.buttonColors(containerColor = Purple80)
                ) {
                    Text("Registrar")
                }

                // Mostrar mensajes según el estado
                when (registerState) {
                    is DataState.Loading -> Text("Registrando...", color = Color.Gray)
                    is DataState.Success -> Text(
                        (registerState as DataState.Success).data,
                        color = Color.Green
                    )
                    is DataState.Error -> Text(
                        "Error: ${(registerState as DataState.Error).exception.message}",
                        color = Color.Red
                    )
                    DataState.Finished -> {}
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

