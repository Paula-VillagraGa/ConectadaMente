package com.example.conectadamente.ui.homeUser.Recomendacion

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.conectadamente.R
import com.example.conectadamente.navegation.NavScreen
import com.example.conectadamente.ui.theme.Purple30
import com.example.conectadamente.ui.theme.Purple50
import kotlinx.coroutines.delay


// Barra superior (TopAppBar)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    CenterAlignedTopAppBar(
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.logo2), // Reemplaza con tu recurso de imagen
                contentDescription = "Logo",
                modifier = Modifier
                    .size(220.dp) // Ajusta el tamaño según necesites
                    .padding(start = 8.dp)
                    .padding(top = 30.dp)// Aplica un recorte circular si es necesario
            )
        },
        title = {
            Text(
                text = "", // Puedes añadir un título si lo necesitas
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecomendacionScreen(navController: NavHostController) {
    // Estados para el texto de entrada y resultado
    val inputText = remember { mutableStateOf("") }
    val resultText = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }

    // Configuración de Retrofit
    val BASE_URL = "https://us-central1-proyectoconectadamente.cloudfunctions.net/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)

    // Contexto para mostrar Toast
    val context = LocalContext.current

    Scaffold(
        topBar = { com.example.conectadamente.ui.homeUser.Recomendacion.TopAppBar() },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Cabecera
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¡Bienvenid@ a ConectadaMente, aplicación diseñada para brindarte apoyo en tu camino hacia el bienestar emocional! " +
                                "Nuestro objetivo es ayudarte a encontrar el profesional de la salud mental adecuado para ti. " +
                                "Para ello, utilizamos un avanzado algoritmo basado en procesamiento de lenguaje natural (NLTK), " +
                                "que analiza cuidadosamente el texto que compartes. Este algoritmo identifica patrones en tus palabras y," +
                                " con base en ellos, predice una etiqueta que refleja tus principales preocupaciones o necesidades emocionales",
                        fontSize = 10.sp,
                        color = Color(0xFF1C160C),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Entrada de texto y botón
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = inputText.value,
                        onValueChange = { inputText.value = it },
                        placeholder = { Text("Escribe tu necesidad", color = Purple50) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .border(1.dp, Color(0xFFE9DFCE), shape = RoundedCornerShape(8.dp)),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (inputText.value.isNotEmpty()) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    isLoading.value = true // Activar indicador de carga
                                    delay(4000) // Simular tiempo de espera de 4 segundos
                                    makePrediction(inputText.value, apiService, resultText, context)
                                    isLoading.value = false // Desactivar indicador de carga
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Por favor ingresa un texto",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Purple30)
                    ) {
                        Text("Enviar", style = TextStyle(color = Color.White, fontSize = 16.sp))
                    }

                    // Mostrar CircularProgressIndicator mientras se está cargando
                    if (isLoading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .size(32.dp),
                            color = Purple30
                        )
                    }

                    Button(
                        onClick = {
                            if (resultText.value.isNotEmpty()) {
                                navController.navigate("${NavScreen.ListRecomendation.route}/${resultText.value}")
                            } else {
                                Toast.makeText(
                                    context,
                                    "No hay predicción para buscar",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Purple30),
                        enabled = !isLoading.value // Deshabilitar mientras se carga
                    ) {
                        Text("Buscar Psicólogos")
                    }
                }

                // Resultado y advertencia
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Resultado: ${resultText.value}",
                        fontSize = 16.sp,
                        color = Color(0xFF1C160C),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 30.dp, bottom = 80.dp)
                    )
                    Text(
                        text = "Warning: Esto es una aplicación de prueba y la predicción puede tener errores",
                        fontSize = 14.sp,
                        color = Color(0xFF9F1246),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 80.dp)
                    )
                }
            }
        }
    }
}
// Datos de entrada para la petición
data class InputData(val text: String)

// Respuesta esperada de la API
data class PredictionResponse(val prediction: String)

// Interfaz de Retrofit
interface ApiService {
    @POST("predict")
    suspend fun predict(@Body inputData: InputData): Response<PredictionResponse>
}

suspend fun makePrediction(
    inputText: String,
    apiService: ApiService,
    resultText: MutableState<String>,
    context: android.content.Context
) {
    try {
        val inputData = InputData(inputText)
        val response = apiService.predict(inputData)

        if (response.isSuccessful) {
            val prediction = response.body()?.prediction
            resultText.value = prediction ?: "No se recibió resultado válido"
        } else {
            resultText.value = "Error en la predicción: ${response.code()}"
        }
    } catch (e: Exception) {
        resultText.value = "Error en la conexión: ${e.message}"
    }
}
