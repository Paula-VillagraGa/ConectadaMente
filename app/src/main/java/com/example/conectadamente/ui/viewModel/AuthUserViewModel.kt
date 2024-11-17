package com.example.conectadamente.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.repository.AuthUserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthUserRepository) : ViewModel() {

    fun registerUser(email: String, password: String, onComplete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            authRepository.registerUser(email, password, onComplete)
        }
    }

    fun loginUser(email: String, password: String, onComplete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            authRepository.loginUser(email, password, onComplete)
        }
    }

    fun getCurrentUser() = authRepository.getCurrentUser()

    fun logout() = authRepository.logout()
}
