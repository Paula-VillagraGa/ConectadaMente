package com.example.conectadamente.ui.homeUser.Recomendacion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun BuscarPorTagScreen(tag: String, navController: NavHostController) {
    val psicologos = remember { mutableStateOf(listOf<String>()) } // Lista simulada de psicólogos

    // Simular la búsqueda con un filtro (puedes reemplazarlo con una API real)
    LaunchedEffect(tag) {
        // Aquí puedes realizar una consulta real a tu base de datos
        psicologos.value = listOf(
            "Psicólogo 1 - Especialidad: $tag",
            "Psicólogo 2 - Especialidad: $tag"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Resultados para: $tag",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        psicologos.value.forEach { psicologo ->
            Text(
                text = psicologo,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
