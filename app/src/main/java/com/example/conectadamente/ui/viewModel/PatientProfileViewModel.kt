package com.example.conectadamente.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.data.repository.patientRepository.PatientRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientProfileViewModel @Inject constructor(
    private val repository: PatientRepository
) : ViewModel() {

    private val _patientData = MutableLiveData<PatientModel?>()
    val patientData: LiveData<PatientModel?> get() = _patientData

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error


    fun fetchCurrentPatientData() {
        viewModelScope.launch {
            try {
                // Obtener el UID del paciente actual desde Firebase Authentication
                val userId = FirebaseAuth.getInstance().currentUser?.uid

                if (userId != null) {
                    // Usamos el UID para obtener los datos del paciente desde Firestore
                    val data = repository.getCurrentPatientData()
                    _patientData.value = data
                } else {
                    // Si no hay usuario autenticado, puedes manejar el error aquí
                    _error.value = "Usuario no autenticado"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    fun updatePatientData(updatedPatient: PatientModel) {
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    // Actualizar los datos del paciente en Firestore
                    repository.updatePatientData(userId, updatedPatient)
                    _patientData.value = updatedPatient  // Actualizamos los datos localmente
                } else {
                    _error.value = "Usuario no autenticado"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
