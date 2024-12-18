package com.example.conectadamente.utils.extensions

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

fun String.isNumeric(): Boolean = this.all { it.isDigit() }
fun String.capitalizeFirstLetter(): String = this.replaceFirstChar { it.uppercase() }


//Formato de rut chile
fun formatRut(rut: String): String {
    val rutWithoutDots = rut.replace(".", "").replace("-", "") // Eliminar puntos y guión

    if (rutWithoutDots.length > 1) {
        val body = rutWithoutDots.substring(0, rutWithoutDots.length - 1) // Parte numérica
        val verifier = rutWithoutDots.substring(rutWithoutDots.length - 1) // Dígito verificador

        val formattedBody = body.reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
        return "$formattedBody-$verifier"
    }
    return rut
}

@Composable
fun ShowSnackBarMessage(snackbarHostState: SnackbarHostState, message: String) {
    // Llamamos a showSnackbar para mostrar el mensaje emergente
    LaunchedEffect(message) {
        snackbarHostState.showSnackbar(message)
    }
}

