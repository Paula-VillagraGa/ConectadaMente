package com.example.conectadamente


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.conectadamente.core.navigation.NavigationWrapper
import com.example.conectadamente.data.database.AppDatabase
import com.example.conectadamente.ui.register.RegisterPatientScreen
import com.example.conectadamente.ui.theme.MyApplicationTheme
import com.example.conectadamente.ui.viewModel.UserViewModel
import com.example.conectadamente.ui.viewModel.UserViewModelFactory

class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(AppDatabase.getDatabase(this).patientDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establecemos el contenido con el wrapper de navegación
        setContent {
            MyApplicationTheme {
                NavigationWrapper()
            }
        }
    }
}