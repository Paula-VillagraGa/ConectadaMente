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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.components.NavigationInferior
import com.example.conectadamente.navegation.BancoNavigation
import com.example.conectadamente.screensLogin.LoginScreen
import com.example.conectadamente.screensLogin.CreateAccountScreen
import com.example.conectadamente.screensLogin.SignInScreen
import com.example.conectadamente.screensMenu.HomeScreen
import com.example.conectadamente.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme() {
                val navController = rememberNavController()

                // Configura el NavHost con las pantallas
                NavHost(navController = navController, startDestination = "login_screen") {
                    composable("login_screen") { LoginScreen(navController = navController) }
                    composable("create_account_screen") { CreateAccountScreen(navController = navController) }
                    composable("sign_in_screen") { SignInScreen(navController = navController) } // Aquí si tienes una pantalla para inicio de sesión
                }
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


