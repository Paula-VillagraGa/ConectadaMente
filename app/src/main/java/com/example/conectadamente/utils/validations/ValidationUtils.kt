package com.example.conectadamente.utils

// Expresiones regulares
private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$"
private const val PASSWORD_REGEX = "^.{8,}$"
private const val RUT_REGEX = "^[0-9]+[-|–]{1}[0-9kK]{1}$"


// Validar email
fun isValidEmail(email: String): Boolean {
    return email.matches(Regex(EMAIL_REGEX))
}

// Validar contraseña
fun isPasswordValid(password: String): Boolean {
    return password.matches(Regex(PASSWORD_REGEX))
}

// Validar RUT
fun isRutValid(rut: String): Boolean {
    return rut.matches(Regex(RUT_REGEX))
}

// Validar los datos de registro
fun validateRegistrationData(
    name: String,
    rut: String,
    email: String,
    password: String,
    confirmPassword: String
): String? {
    return when {
        name.isEmpty() || rut.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
            "Todos los campos son obligatorios"
        }
        !isValidEmail(email) -> {
            "El correo electrónico no tiene un formato válido"
        }
        !isRutValid(rut) -> {
            "El RUT no tiene un formato válido"
        }
        !isPasswordValid(password) -> {
            "La contraseña debe tener al menos 8 caracteres"
        }
        password != confirmPassword -> {
            "Las contraseñas no coinciden"
        }
        else -> null
    }
}
