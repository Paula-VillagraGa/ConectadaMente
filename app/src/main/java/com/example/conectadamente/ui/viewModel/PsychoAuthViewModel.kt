package com.example.conectadamente.ui.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.data.repository.AuthPsychoRepository
import com.example.conectadamente.data.repository.FirestoreRepository
import com.example.conectadamente.utils.constants.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class psychoAuthViewModel @Inject constructor(
    private val authPsychoRepository: AuthPsychoRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<DataState<String>>(DataState.Finished)
    val authState: StateFlow<DataState<String>> = _authState.asStateFlow()

    // Función para registrar psicólogos
    fun registerPsycho(psycho: PsychoModel, password: String, documents: List<Uri>) {
        viewModelScope.launch {
            authPsychoRepository.registerPsycho(psycho, password, documents)
                .onEach { state ->
                    _authState.value = state
                }
                .launchIn(viewModelScope)
        }
    }

    // Restablecer estado después de una operación
    fun resetAuthState() {
        _authState.value = DataState.Finished
    }
}
