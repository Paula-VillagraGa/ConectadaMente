package com.example.conectadamente.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.utils.constants.DataState
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class UserAuthViewModel @Inject constructor(

) : ViewModel() {
    private val _authState = MutableStateFlow<DataState<String>>(DataState.Idle)
    val authState: StateFlow<DataState<String>> get() = _authState

    fun registerPatient(patient: PatientModel, password: String) {
        viewModelScope.launch {
            _authState.value = DataState.Loading
            try {
                // Intentamos registrar al usuario de manera asincrónica
                val authResult = registerUserWithEmailAndPassword(patient.email, password)

                if (authResult != null) {
                    val userId = authResult.user?.uid
                    if (userId != null) {
                        // Guardamos los datos del paciente en Firestore
                        val patientData = hashMapOf(
                            "name" to patient.name,
                            "rut" to patient.rut,
                            "email" to patient.email,
                            "uid" to userId
                        )

                        val patientRef =
                            FirebaseFirestore.getInstance().collection("patients").document(userId)

                        // Guardamos los datos del paciente en Firestore
                        patientRef.set(patientData).addOnSuccessListener {
                            _authState.value = DataState.Success("Registro exitoso")
                        }.addOnFailureListener { e ->
                            _authState.value =
                                DataState.Error("Error al guardar los datos: ${e.message}")
                        }
                    } else {
                        _authState.value = DataState.Error("No se pudo obtener el ID del usuario")
                    }
                } else {
                    _authState.value = DataState.Error("Error al registrar el usuario")
                }
            } catch (e: Exception) {
                _authState.value = DataState.Error("Error en el registro: ${e.message}")
            }
        }
    }

    private suspend fun registerUserWithEmailAndPassword(email: String, password: String): AuthResult? {
        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result)
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Error desconocido")
                        )
                    }
                }
        }
    }
}
