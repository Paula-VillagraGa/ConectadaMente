package com.example.conectadamente.ui.homeUser

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import android.util.Log
import androidx.navigation.NavHostController

// Datos de entrada para la petición
data class InputData(val text: String)

// Respuesta esperada de la API
data class PredictionResponse(val prediction: String)

// Interfaz de Retrofit
interface ApiService {
    @POST("predict")
    suspend fun predict(@Body inputData: InputData): Response<PredictionResponse>
}
@Composable
fun RecomendacionScreen(navController: NavHostController) {
    // Estado de la UI
    val inputText = remember { mutableStateOf("") }
    val resultText = remember { mutableStateOf("") }

    // Configuración de Retrofit
    val BASE_URL = "https://us-central1-proyectoconectadamente.cloudfunctions.net/"  // URL de firebase
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)

    // Obtener el contexto de la actividad
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Campo de texto para ingresar la información
        TextField(
            value = inputText.value,
            onValueChange = { inputText.value = it },
            label = { Text("Ingresa el texto para predecir") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        // Botón para hacer la predicción
        Button(
            onClick = {
                if (inputText.value.isNotEmpty()) {
                    Log.d("PredictText", "Texto enviado: ${inputText.value}")
                    // Llamada al servicio API usando corutinas
                    CoroutineScope(Dispatchers.Main).launch {
                        makePrediction(inputText.value, apiService, resultText, context)
                    }
                } else {
                    // Uso del contexto local para mostrar el Toast
                    Toast.makeText(context, "Por favor ingresa un texto", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Predecir")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el resultado
        Text(text = "Resultado: ${resultText.value}")
    }
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
            if (prediction != null) {
                // Actualizar el resultado con la predicción recibida
                resultText.value = prediction
            } else {
                resultText.value = "No se recibió resultado válido"
            }
        } else {
            resultText.value = "Error en la predicción: ${response.code()}"
        }
    } catch (e: Exception) {
        resultText.value = "Error en la conexión: ${e.message}"
    }
}