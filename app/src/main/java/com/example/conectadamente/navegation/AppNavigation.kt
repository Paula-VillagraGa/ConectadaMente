package com.example.conectadamente.navegation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.ui.authPaciente.LoginScreen
import com.example.conectadamente.ui.authPaciente.SignInScreen
import com.example.conectadamente.ui.homeUser.ChatUsuarioScreen
import com.example.conectadamente.ui.homeUser.FormativoUsuarioScreen
import com.example.conectadamente.ui.homeUser.HomeScreen
import com.example.conectadamente.ui.register.RegisterPatientScreen
import com.example.conectadamente.ui.viewModel.UserAuthViewModel
import com.example.conectadamente.ui.homeUser.PerfilUsuarioScreen
import com.example.conectadamente.ui.homeUser.RecomendacionUsuarioScreen
import com.example.conectadamente.ui.authPsicologo.PsychologistLoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val scaffoldScreens = listOf(
        NavScreen.Home.route,
        NavScreen.Perfil.route,
        NavScreen.Chat.route,
        NavScreen.Recomendaciones.route,
        NavScreen.Formativo.route
    )

    ScaffoldOrContent(navController = navController, scaffoldScreens = scaffoldScreens) {
        NavHost(
            navController = navController,
            startDestination = NavScreen.Login.route
        ) {
            // Pantallas de navegación
            composable(NavScreen.Login.route) {
                LoginScreen(
                    navController = navController,
                    navigateToSignIn = { navController.navigate(NavScreen.SignIn.route) },
                    navigateToRegisterPatient = { navController.navigate(NavScreen.RegisterPatient.route) },
                    navigateToPsychologistLogin = { navController.navigate(NavScreen.PsychologistLogin.route) }
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


            composable(NavScreen.PsychologistLogin.route) {
                PsychologistLoginScreen()
            }
            composable(NavScreen.RegisterPatient.route) {
                val userAuthViewModel: UserAuthViewModel = hiltViewModel()
                RegisterPatientScreen(viewModel = userAuthViewModel)
            }
            composable(NavScreen.Home.route) { HomeScreen(navController) }
            composable(NavScreen.Perfil.route) { PerfilUsuarioScreen() }
            composable(NavScreen.Chat.route) { ChatUsuarioScreen(navController) }
            composable(NavScreen.Recomendaciones.route) { RecomendacionUsuarioScreen(navController) }
            composable(NavScreen.Formativo.route) { FormativoUsuarioScreen(navController) }
        }
    }
}

@Composable
fun ScaffoldOrContent(
    navController: NavController,
    scaffoldScreens: List<String>,
    content: @Composable (PaddingValues) -> Unit
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    if (currentDestination in scaffoldScreens) {
        Scaffold(
            bottomBar = { NavigationInferior(navController) },
            content = { paddingValues -> content(paddingValues) }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) { content(PaddingValues()) }
    }
}
