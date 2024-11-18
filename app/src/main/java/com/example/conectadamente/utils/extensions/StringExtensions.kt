package com.example.conectadamente.utils.extensions

fun String.isNumeric(): Boolean = this.all { it.isDigit() }
fun String.capitalizeFirstLetter(): String = this.replaceFirstChar { it.uppercase() }
