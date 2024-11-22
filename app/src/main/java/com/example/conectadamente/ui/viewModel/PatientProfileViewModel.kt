package com.example.conectadamente.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.data.repository.AuthUserRepository
import com.example.conectadamente.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PatientProfileViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository, // Repositorio de Firestore
    private val authRepository: AuthUserRepository // Repositorio de autenticación
) : ViewModel() {

    private val _patientData = MutableLiveData<PatientModel>()
    val patientData: LiveData<PatientModel> get() = _patientData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Obtener los datos del paciente actual
    fun fetchPatientData() {
        val currentUserId = authRepository.getCurrentUserId() // Obtener UID del usuario autenticado
        if (currentUserId != null) {
            firestoreRepository.getPatientById(
                userId = currentUserId,
                onSuccess = { patient ->
                    _patientData.value = patient
                },
                onError = { errorMessage ->
                    _error.value = errorMessage
                }
            )
        } else {
            _error.value = "Usuario no autenticado"
        }
    }

    // Actualizar los datos del paciente
    fun updatePatientData(updatedPatient: PatientModel, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            firestoreRepository.updatePatientData(
                patient = updatedPatient.copy(id = userId),
                onSuccess = {
                    _patientData.value = updatedPatient
                    onSuccess()
                },
                onError = { errorMessage ->
                    _error.value = errorMessage
                    onError(errorMessage)
                }
            )
        } else {
            _error.value = "Usuario no autenticado"
        }
    }
}
