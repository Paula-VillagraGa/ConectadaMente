package com.example.conectadamente.ui.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.data.repository.AuthPsychoRepository
import com.example.conectadamente.utils.constants.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class PsychoAuthViewModel @Inject constructor(
    private val authPsychoRepository: AuthPsychoRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<DataState<String>>(DataState.Finished)
    val authState: StateFlow<DataState<String>> = _authState.asStateFlow()
    private val _profileState = MutableStateFlow<PsychoModel?>(null)
    val profileState: StateFlow<PsychoModel?> get() = _profileState



    fun registerPsycho(psycho: PsychoModel, password: String, documents: List<Uri>) {
        viewModelScope.launch {
            authPsychoRepository.registerPsycho(psycho, password, documents).collect { state ->
                _authState.value = state
            }
        }
    }

    // Función para cargar el perfil del psicólogo
    fun loadCurrentProfile() {
        viewModelScope.launch {
            // Asumimos que ya tienes el UID del psicólogo autenticado
            val currentUserUid = authPsychoRepository.getCurrentPsychoId() // Implementa esto si es necesario
            val profile = authPsychoRepository.getCurrentPsychologistProfile()
            _profileState.value = profile
        }
    }

}
