package com.example.conectadamente.ui.authPsicologo

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.conectadamente.R
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.ui.theme.Blue20
import com.example.conectadamente.ui.theme.Blue30
import com.example.conectadamente.ui.theme.PoppinsFontFamily
import com.example.conectadamente.ui.theme.Purple50
import com.example.conectadamente.ui.theme.Purple80
import com.example.conectadamente.ui.viewModel.PsychoAuthViewModel
import com.example.conectadamente.utils.constants.DataState
import com.example.conectadamente.utils.getImageSize
import com.example.conectadamente.utils.validateRegistrationData


@Composable
fun RegisterPsychoScreen(viewModel: PsychoAuthViewModel = hiltViewModel()) {
    var name by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val registerState by viewModel.authState.collectAsState()


    val context = LocalContext.current


    LazyRow(modifier = Modifier.padding(top = 16.dp)) {
        items(selectedImages.size) { index ->
            val uri = selectedImages[index]
            Image(
                painter = rememberAsyncImagePainter(uri), // Usamos Coil para cargar imágenes desde URI
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp)) // Bordes redondeados para las imágenes
            )
        }
    }

// Botón de registro
    Button(
        onClick = {
            // Validar cantidad y tamaño de imágenes
            try {
                if (selectedImages.size > 5) {
                    throw IllegalArgumentException("No se pueden subir más de 5 documentos")
                }
                // Validar el tamaño de cada imagen
                if (selectedImages.any { getImageSize(it, context) > 5 * 1024 * 1024 }) {
                    throw IllegalArgumentException("Un documento excede el tamaño permitido (5 MB)")
                }

                // Registrar psicólogo
                val psycho = PsychoModel(
                    email = email,
                    rut = rut,
                    name = name
                )
                viewModel.registerPsycho(psycho, password, selectedImages)
            } catch (e: IllegalArgumentException) {
                message = e.message ?: "Error desconocido"
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text("Registrar")
    }


    // Launcher para seleccionar imágenes
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImages = uris
    }

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
                    painter = painterResource(id = R.drawable.cinco2),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(100.dp) // Tamaño de la imagen
                        .padding(bottom = 6.dp) // Espacio entre la imagen y el texto
                )
                Text(
                    text = "Regístrate",
                    style = TextStyle(
                        fontFamily = PoppinsFontFamily,
                        fontStyle = FontStyle.Italic,
                        fontSize = 30.sp,
                        color = Blue20,
                    ),
                    textAlign = TextAlign.Center
                )

                // Campo para el nombre
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                // Campo para el RUT
                OutlinedTextField(
                    value = rut,
                    onValueChange = { rut = it },
                    label = { Text("RUT") },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                // Campo para el N°verificador
                OutlinedTextField(
                    value = numero,
                    onValueChange = { numero = it },
                    label = { Text("N° Registro Prestador de Salud") },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                // Campo para el correo electrónico
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier
                        .fillMaxWidth()

                )

                // Campo para la contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                // Campo para confirmar la contraseña
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()


                )
                // Botón para seleccionar imágenes
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Text("Seleccionar Imágenes")
                }

                //carga de imágenes con uri y Coil
                LazyRow(modifier = Modifier.padding(top = 16.dp)) {
                    items(selectedImages.size) { index ->
                        val uri = selectedImages[index]
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Imagen seleccionada",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(4.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }

                // Botón de registro
                Button(
                    onClick = {
                        try {
                            // Validar cantidad de imágenes
                            if (selectedImages.size > 2) {
                                throw IllegalArgumentException("No se pueden subir más de 2 documentos")
                            }

                            // Validar tamaño de imágenes (máximo 5 MB por documento)
                            if (selectedImages.any {
                                    getImageSize(
                                        it,
                                        context
                                    ) > 5 * 1024 * 1024
                                }) {
                                throw IllegalArgumentException("Un documento excede el tamaño permitido (5 MB)")
                            }

                            // Validar datos de registro
                            val validationMessage = validateRegistrationData(
                                name = name,
                                rut = rut,
                                email = email,
                                password = password,
                                confirmPassword = confirmPassword
                            )

                            if (validationMessage != null) {
                                throw IllegalArgumentException(validationMessage)
                            }

                            // Crear objeto PsychoModel para registrar
                            val psycho = PsychoModel(
                                email = email,
                                rut = rut,
                                name = name
                            )

                            // Registrar psicólogo con documentos
                            viewModel.registerPsycho(psycho, password, selectedImages)

                            message = "Registro en proceso. Por favor, espera..."
                        } catch (e: IllegalArgumentException) {
                            message = e.message ?: "Error desconocido"
                        } catch (e: Exception) {
                            // Para otros errores inesperados
                            message = "Ocurrió un error inesperado: ${e.message}"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Registrar")
                }

                if (message.isNotBlank()) {
                    Text(
                        text = message,
                        color = if (message.startsWith("Error")) Color.Red else Color.Green,
                        modifier = Modifier.padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

            }
        }

        // Mostrar mensajes según el estado
        when (registerState) {
            is DataState.Loading -> {
                Text("Registrando...", color = Purple50)
            }

            is DataState.Success -> {
                Text(
                    "Registro exitoso. Tu cuenta será verificada en un plazo de 24 horas hábiles.",
                    color = Purple80,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            is DataState.Error -> {
                Text(
                    "Error: ${(registerState as DataState.Error).e}",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            DataState.Finished -> {
                Text(
                    "Proceso finalizado",
                    color = Purple80,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            DataState.Idle -> {} // Agregar esta rama
        }
    }
}
