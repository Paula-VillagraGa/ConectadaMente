package com.example.conectadamente.navegation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.ui.auth.CreateAccountScreen
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

    // Usamos el NavHost para definir las rutas y pantallas
    NavHost(navController = navController, startDestination = "login") {

        // Definimos las pantallas de la navegación, como lo tenías antes
        composable("perfil_usuario") {
            PerfilUsuarioScreen(navController = navController)
        }

        composable("chat_usuario") {
            ChatUsuarioScreen(navController = navController)
        }

        composable("home_screen") {
            HomeScreen(navController = navController)
        }

        composable("recomendaciones_usuario") {
            RecomendacionUsuarioScreen(navController = navController)
        }

        composable("formativo_usuario") {
            FormativoUsuarioScreen(navController = navController)
        }

        // Pantallas que ya tenías configuradas
        composable("login") {
            LoginScreen(
                navController = navController,
                navigateToSignIn = { navController.navigate("sign_in") },
                navigateToGoogleSignIn = { navController.navigate("google_sign_in") },
                navigateToCreateAccount = { navController.navigate("create_account") },
                navigateToRegisterPatient = { navController.navigate("register_patient") }
            )
        }

        composable("sign_in") {
            SignInScreen(
                navigateToRegisterPacient = { navController.navigate("register_patient") },
                navigateToHomeScreen = { navController.navigate("home_screen") }
            )
        }

        composable("create_account") {
            CreateAccountScreen(
                navigateToLoginScreen = { navController.navigate("login") }
            )
        }

        composable("register_patient") {
            val userAuthViewModel: UserAuthViewModel = hiltViewModel()
            RegisterPatientScreen(viewModel = userAuthViewModel)
        }

        composable("home_screen") {
            HomeScreen(navController = navController)
        }
    }
}