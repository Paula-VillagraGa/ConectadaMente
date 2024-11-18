package com.example.conectadamente.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.data.repository.AuthUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UserAuthViewModel @Inject constructor(
    private val authUserRepository: AuthUserRepository // Inyección de dependencias
) : ViewModel() {

    // Aquí, puedes manejar el registro del paciente
    fun registerPatient(
        patient: PatientModel,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Llamamos al método del repositorio para registrar al paciente en Firebase
        authUserRepository.registerPatientInFirebase(
            patient = patient,
            password = password,
            onSuccess = {
                // Si el registro fue exitoso, ejecutamos la acción de éxito
                onSuccess()
            },
            onError = { errorMessage ->
                // Si ocurrió un error, pasamos el mensaje de error al callback
                onError(errorMessage)
            }
        )
    }
}
