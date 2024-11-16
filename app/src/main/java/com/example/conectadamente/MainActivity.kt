package com.example.conectadamente


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.activity.viewModels
import com.example.conectadamente.core.navigation.NavigationWrapper
import com.example.conectadamente.data.database.AppDatabase
import com.example.conectadamente.ui.register.RegisterPatientScreen
import com.example.conectadamente.ui.theme.MyApplicationTheme
import com.example.conectadamente.core.navigation. *
import com.example.conectadamente.components. *
import com.example.conectadamente.navegation. *
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
            MainScreen()
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    Scaffold (
        bottomBar = {
            NavigationInferior(navController)
        }
    ) {padding->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ){
            BancoNavigation(navController = navController)
        }
    }
}