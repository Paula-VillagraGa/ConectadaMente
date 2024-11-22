package com.example.conectadamente.navegation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.ui.auth.LoginScreen
import com.example.conectadamente.ui.auth.SignInScreen
import com.example.conectadamente.ui.homeUser.ChatUsuarioScreen
import com.example.conectadamente.ui.homeUser.FormativoUsuarioScreen
import com.example.conectadamente.ui.homeUser.HomeScreen
import com.example.conectadamente.ui.register.RegisterPatientScreen
import com.example.conectadamente.ui.viewModel.UserAuthViewModel
import com.example.conectadamente.ui.homeUser.PerfilUsuarioScreen
import com.example.conectadamente.ui.homeUser.RecomendacionUsuarioScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    // Definimos el NavHost con las rutas y pantallas
    NavHost(navController = navController, startDestination = "login") {

        // Pantalla de Login
        composable("login") {
            LoginScreen(
                navController = navController,
                navigateToSignIn = { navController.navigate("sign_in") },
                navigateToGoogleSignIn = { /* Implementar lógica de Google Sign-In aquí */ },
                navigateToCreateAccount = { /* Implementar lógica para crear cuenta */ },
                navigateToRegisterPatient = { navController.navigate("register_patient") }
            )
        }

        // Pantalla de Iniciar Sesión
        composable("sign_in") {
            SignInScreen(
                navigateToRegisterPacient = { navController.navigate("register_patient") },
                navigateToHomeScreen = {
                    navController.navigate("home_screen") {
                        popUpTo("login") { inclusive = true } // Limpia el login de la pila
                    }
                }
            )
        }

        // Pantalla de Registro de Paciente
        composable("register_patient") {
            val userAuthViewModel: UserAuthViewModel = hiltViewModel()
            RegisterPatientScreen(viewModel = userAuthViewModel)
        }

        // Pantalla Principal
        composable("home_screen") {
            HomeScreen(navController = navController)
        }

        // Pantalla de Perfil de Usuario
        composable("perfil_usuario") {
            PerfilUsuarioScreen()
        }

        // Pantalla de Chat de Usuario
        composable("chat_usuario") {
            ChatUsuarioScreen(navController = navController)
        }

        // Pantalla de Recomendaciones de Usuario
        composable("recomendaciones_usuario") {
            RecomendacionUsuarioScreen(navController = navController)
        }

        // Pantalla de Material Formativo para el Usuario
        composable("formativo_usuario") {
            FormativoUsuarioScreen(navController = navController)
        }
    }
}
