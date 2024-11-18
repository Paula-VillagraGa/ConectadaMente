package com.example.conectadamente.utils.validations



import com.example.conectadamente.utils.constants.*

fun isValidEmail(email: String): Boolean {
    return email.matches(Regex(EMAIL_REGEX))
}

fun isPasswordValid(password: String): Boolean {
    return password.matches(Regex(PASSWORD_REGEX))
}

fun isRutValid(rut: String): Boolean {
    return rut.matches(Regex(RUT_REGEX))
}
