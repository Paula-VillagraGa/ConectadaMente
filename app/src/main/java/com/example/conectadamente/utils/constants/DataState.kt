package com.example.conectadamente.utils.constants

sealed class TipoDeError {
    data class ErrorDeRed(val mensaje: String, val codigo: Int? = null) : TipoDeError()
    data class ErrorDeValidacion(val campo: String, val detalle: String) : TipoDeError()
    data class ErrorDeBaseDeDatos(val excepcion: Throwable) : TipoDeError()
    data object ErrorDesconocido : TipoDeError()
}

sealed class DataState<out T> {
    object Idle : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val e: String) : DataState<Nothing>()
    data object Finished : DataState<Nothing>() // Estado de "completado"
}


val estadoDeRed: DataState<Nothing> = DataState.Error(
    TipoDeError.ErrorDeRed(mensaje = "No hay conexión a Internet", codigo = 503).toString()
)

val estadoDeValidacion: DataState<Nothing> = DataState.Error(
    TipoDeError.ErrorDeValidacion(campo = "correo electrónico", detalle = "Formato inválido").toString()
)

val estadoDeBaseDeDatos: DataState<Nothing> = DataState.Error(
    TipoDeError.ErrorDeBaseDeDatos(excepcion = Exception("Error al acceder a la base de datos")).toString()
)

val estadoDesconocido: DataState<Nothing> = DataState.Error(
    TipoDeError.ErrorDesconocido.toString()
)

