package com.example.conectadamente.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.dao.PatientDao
import com.example.conectadamente.data.model.PatientModel
import kotlinx.coroutines.launch


open class UserViewModel(private val patientDao: PatientDao) : ViewModel() {

    // Método para registrar un paciente
    open fun registerPatient(patient: PatientModel, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                patientDao.insertPatient(patient)
                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
    }

    // Método para iniciar sesión con correo electrónico y contraseña
    open fun loginPatient(email: String, password: String, onResult: (PatientModel?) -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val patient = patientDao.getPatientByEmail(email)
                if (patient != null && patient.password == password) {
                    onResult(patient) // Si el correo y la contraseña coinciden, retorna el paciente
                } else {
                    onResult(null) // Si no coinciden, retorna null
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }
}

/*
open class UserViewModel(private val patientDao: PatientDao) : ViewModel() {

    open fun registerPatient(patient: PatientModel, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                patientDao.insertPatient(patient)
                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
    }
}
*/