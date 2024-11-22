package com.example.conectadamente.navegation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { NavigationInferior(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavScreen.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Navegación de pantallas iniciales
            composable(NavScreen.Login.route) {
                LoginScreen(
                    navController = navController,
                    navigateToSignIn = { navController.navigate(NavScreen.SignIn.route) },
                    navigateToRegisterPatient = { navController.navigate(NavScreen.RegisterPatient.route) }
                )
            }

            composable(NavScreen.SignIn.route) {
                SignInScreen(
                    navigateToRegisterPacient = { navController.navigate(NavScreen.RegisterPatient.route) },
                    navigateToHomeScreen = {
                        navController.navigate(NavScreen.Home.route) {
                            popUpTo(NavScreen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(NavScreen.RegisterPatient.route) {
                val userAuthViewModel: UserAuthViewModel = hiltViewModel()
                RegisterPatientScreen(viewModel = userAuthViewModel)
            }

            // Navegación de pantallas principales
            composable(NavScreen.Home.route) { HomeScreen(navController) }
            composable(NavScreen.Perfil.route) { PerfilUsuarioScreen() }
            composable(NavScreen.Chat.route) { ChatUsuarioScreen(navController) }
            composable(NavScreen.Recomendaciones.route) { RecomendacionUsuarioScreen(navController) }
            composable(NavScreen.Formativo.route) { FormativoUsuarioScreen(navController) }
        }
    }
}
