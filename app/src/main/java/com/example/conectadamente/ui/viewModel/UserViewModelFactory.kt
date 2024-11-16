package com.example.conectadamente.ui.viewModel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.conectadamente.data.dao.PatientDao

// Implementa correctamente la interfaz ViewModelProvider.Factory
class UserViewModelFactory(private val patientDao: PatientDao) : ViewModelProvider.Factory {

    // Método que debe ser sobreescrito correctamente
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprobar que el modelo es el UserViewModel
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            // Retornar la instancia del ViewModel con la dependencia
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(patientDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
