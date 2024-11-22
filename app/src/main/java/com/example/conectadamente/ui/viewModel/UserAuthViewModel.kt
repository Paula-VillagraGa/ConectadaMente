package com.example.conectadamente.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.data.repository.AuthUserRepository
import com.example.conectadamente.utils.constants.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAuthViewModel @Inject constructor(
    private val authUserRepository: AuthUserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<DataState<String>>(DataState.Finished)
    val authState: StateFlow<DataState<String>> = _authState

    // Registrar un paciente
    fun registerPatient(patient: PatientModel, password: String) {
        viewModelScope.launch {
            authUserRepository.registerPatient(patient, password).collect { state ->
                _authState.value = state
            }
        }
    }

    // Iniciar sesión
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authUserRepository.login(email, password).collect { state ->
                _authState.value = state
            }
        }
    }
}
