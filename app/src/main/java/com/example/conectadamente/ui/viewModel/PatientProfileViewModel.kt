package com.example.conectadamente.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.data.repository.PatientsRepo.AuthUserRepository
import com.example.conectadamente.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientProfileViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository, // Repositorio de Firestore
    private val authRepository: AuthUserRepository // Repositorio de autenticación
) : ViewModel() {

    private val _patientData = MutableLiveData<PatientModel?>()
    val patientData: MutableLiveData<PatientModel?> get() = _patientData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Obtener los datos del paciente actual
    fun fetchPatientData() {
        val currentUserId = authRepository.getCurrentUserId() // Obtener UID del usuario autenticado
        if (currentUserId != null) {
            viewModelScope.launch {
                val result = firestoreRepository.getPatientById(currentUserId)
                when {
                    result.isSuccess -> {
                        _patientData.value = result.getOrNull() // Obtener los datos si la operación fue exitosa
                    }
                    result.isFailure -> {
                        _error.value = result.exceptionOrNull()?.message ?: "Error desconocido" // Obtener el mensaje de error
                    }
                }
            }
        } else {
            _error.value = "Usuario no autenticado"
        }
    }

    // Actualizar los datos del paciente
    fun updatePatientData(updatedPatient: PatientModel, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            viewModelScope.launch {
                val result = firestoreRepository.updatePatientData(updatedPatient.copy(id = userId))
                when {
                    result.isSuccess -> {
                        _patientData.value = updatedPatient
                        onSuccess()
                    }
                    result.isFailure -> {
                        _error.value = result.exceptionOrNull()?.message ?: "Error desconocido"
                        onError(result.exceptionOrNull()?.message ?: "Error desconocido")
                    }
                }
            }
        } else {
            _error.value = "Usuario no autenticado"
        }
    }
}
