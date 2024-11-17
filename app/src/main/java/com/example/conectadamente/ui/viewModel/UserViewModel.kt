package com.example.conectadamente.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.dao.PatientDao
import com.example.conectadamente.data.model.PatientModel
import kotlinx.coroutines.launch


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
