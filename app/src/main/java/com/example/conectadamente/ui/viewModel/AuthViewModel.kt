package com.example.conectadamente.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.repository.AuthRepository
import com.example.conectadamente.utils.constants.DataState
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _loginState = MutableStateFlow<DataState<String>>(DataState.Idle)
    val loginState: StateFlow<DataState<String>> get() = _loginState

    fun handleLogin(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = DataState.Loading
            try {
                val user = authRepository.signInWithEmailAndPassword(email, password)
                val role = authRepository.getRoleForUser(user.uid)
                _loginState.value = DataState.Success(role)
            } catch (e: Exception) {
                _loginState.value = DataState.Error(e)
            }
        }
    }

        fun logout() {
            viewModelScope.launch {
                authRepository.logout().collect { state ->
                    _loginState.value = state
                }
            }
        }
    }
