package com.example.conectadamente.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.conectadamente.utils.isValidEmail


import com.google.firebase.auth.FirebaseAuth


open class UserAuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Método para registrar un usuario
    open fun registerUser(
        rut: String,
        name: String,
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Verificar si el email tiene un formato válido
        if (!isValidEmail(email)) {
            onError("El correo electrónico no tiene un formato válido.")
            return
        }
        // Registro en Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Usuario registrado con éxito
                    onSuccess("Usuario registrado correctamente.")
                    //Revisar en LogCat por posibles errores
                    Log.d("RegisterUser", "Registro exitoso para el usuario: $email")
                } else {
                    // Error durante el registro
                    onError(task.exception?.message ?: "Error desconocido.")
                }
            }
    }
}

