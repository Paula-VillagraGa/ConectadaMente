package com.example.conectadamente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.screens.LoginScreen
import com.example.conectadamente.screens.CreateAccountScreen
import com.example.conectadamente.screens.SignInScreen
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
        }
    }
}
