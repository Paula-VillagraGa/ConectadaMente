package com.example.conectadamente.ui.register


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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conectadamente.R
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.ui.theme.*
import com.example.conectadamente.ui.viewModel.UserAuthViewModel
import com.example.conectadamente.utils.validations.isValidEmail
import com.example.conectadamente.utils.validations.isRutValid
import com.example.conectadamente.utils.validations.isPasswordValid

//funciona
@Composable
fun RegisterPatientScreen(viewModel: UserAuthViewModel) {
    var name by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

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
                    .background(Blue30)
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
                .background(color = Color.White, shape = RoundedCornerShape(16.dp)) // Fondo blanco con bordes redondeados
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
                        .padding(bottom = 6.dp) // Espacio entre la imagen y el texto
                )
                Text(
                    text = "Regístrate",
                    style = TextStyle(
                        fontFamily = PoppinsFontFamily,   // Usar la familia de fuentes definida
                        fontStyle = FontStyle.Italic,    // Asegura que se usa el estilo itálico
                        fontSize = 30.sp,                // Tamaño de la fuente
                        color = Blue20,
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
                        when {
                            name.isEmpty() || rut.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                                message = "Todos los campos son obligatorios"
                            }
                            !isValidEmail(email) -> {
                                message = "El correo electrónico no tiene un formato válido"
                            }
                            !isRutValid(rut) -> {
                                message = "El RUT no tiene un formato válido"
                            }
                            !isPasswordValid(password) -> {
                                message = "La contraseña debe tener al menos 8 caracteres"
                            }
                            password != confirmPassword -> {
                                message = "Las contraseñas no coinciden"
                            }
                            else -> {
                                val patient = PatientModel(
                                    email = email,
                                    rut = rut,
                                    name = name
                                )

                                viewModel.registerPatient(
                                    patient = patient,
                                    password = password,
                                    onSuccess = {
                                        message = "Registro exitoso"
                                    },
                                    onError = { error ->
                                        message = "Error: $error"
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue30)
                ) {
                    Text("Registrar")
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
