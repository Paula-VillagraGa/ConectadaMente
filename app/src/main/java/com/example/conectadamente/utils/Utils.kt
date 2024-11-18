package com.example.conectadamente.utils



// Función para validar el formato de un correo electrónico
fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    return email.matches(emailRegex)
}