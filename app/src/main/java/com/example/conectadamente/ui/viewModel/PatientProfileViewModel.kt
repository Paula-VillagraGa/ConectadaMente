package com.example.conectadamente.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.data.repository.PatientsRepo.PatientRepository
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
                val data = repository.getCurrentPatientData()
                _patientData.value = data
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
