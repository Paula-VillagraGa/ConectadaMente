package com.example.conectadamente.utils.constants

import java.lang.Exception

sealed class DataState <out T>{
    object Idle : DataState<Nothing>() // Estado inicial o sin acción
    object Loading: DataState<Nothing>()
    object Finished: DataState<Nothing>()
    data class Success<out T>(
        val data: T
    ): DataState<T>()
    data class Error (
        val e:Exception
    ): DataState<Nothing>()
}


