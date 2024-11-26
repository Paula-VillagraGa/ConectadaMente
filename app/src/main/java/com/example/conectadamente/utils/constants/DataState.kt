package com.example.conectadamente.utils.constants

import java.lang.Exception

sealed class DataState<out T> {
    object Idle : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val e: Throwable) : DataState<Nothing>()
    object Finished : DataState<Nothing>() // Estado de "completado"
}
