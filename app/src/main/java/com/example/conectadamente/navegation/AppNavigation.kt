package com.example.conectadamente.navegation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.conectadamente.ui.authPaciente.LoginScreen
import com.example.conectadamente.ui.authPaciente.RegisterPatientScreen
import com.example.conectadamente.ui.authPaciente.SignInScreen
import com.example.conectadamente.ui.authPsicologo.PsychoSignInScreen
import com.example.conectadamente.ui.authPsicologo.PsychologistLoginScreen
import com.example.conectadamente.ui.authPsicologo.RegisterPsychoScreen
import com.example.conectadamente.ui.homePsycho.ChatPsychoScreen
import com.example.conectadamente.ui.homePsycho.DisponibilidadScreen
import com.example.conectadamente.ui.homePsycho.EditPsychoProfileScreen
import com.example.conectadamente.ui.homePsycho.PsychoHomeScreen
import com.example.conectadamente.ui.homeUser.ProfilePsyFromPatScreen
import com.example.conectadamente.ui.homePsycho.PsychoProfileScreen
import com.example.conectadamente.ui.homeUser.ChatUsuarioScreen
import com.example.conectadamente.ui.homeUser.EditProfilePatientScreen
import com.example.conectadamente.ui.homeUser.HomeScreen
import com.example.conectadamente.ui.homeUser.PerfilUsuarioScreen
import com.example.conectadamente.ui.homeUser.Recomendacion.BuscarPorTagScreen
import com.example.conectadamente.ui.homeUser.Recomendacion.RecomendacionScreen
import com.example.conectadamente.ui.viewModel.BuscarPorTagViewModel
import com.example.conectadamente.ui.homeUser.recommendationsPatient.ArticleScreen
import com.example.conectadamente.ui.homeUser.recommendationsPatient.BookRecommendations
import com.example.conectadamente.ui.homeUser.recommendationsPatient.SosDialCardScreen
import com.example.conectadamente.ui.viewModel.PatientProfileViewModel
import com.example.conectadamente.ui.viewModel.PsychoAuthViewModel
import com.example.conectadamente.ui.viewModel.UserAuthViewModel


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val scaffoldScreens = listOf(
        NavScreen.Home.route,
        NavScreen.Perfil.route,
        NavScreen.Chat.route,
        NavScreen.Formativo.route
    )
    val scaffoldPsycho = listOf(
        NavScreen.PsychoHome.route,
        NavScreen.PsychoProfile.route,
        NavScreen.ChatPsycho.route
    )


    ScaffoldOrContent(navController = navController, scaffoldScreens = scaffoldScreens, scaffoldPsycho = scaffoldPsycho) {
        NavHost(
            navController = navController,
            startDestination = NavScreen.Login.route
        ) {
            // Pantallas de navegación
            composable(NavScreen.Login.route) {
                LoginScreen(
                    navController = navController,
                    navigateToSignIn = { navController.navigate(NavScreen.SignIn.route) },
                    navigateToRegisterPatient = { navController.navigate(NavScreen.RegisterPatient.route) }
                )
            }
            composable(NavScreen.SignIn.route) {
                SignInScreen(
                    navController = navController,
                    navigateToRegisterPacient = { navController.navigate(NavScreen.RegisterPatient.route) },
                    navigateToPsychoHomeScreen = {navController.navigate(NavScreen.PsychoHome.route)},
                    navigateToHomeScreen = {
                        navController.navigate(NavScreen.Home.route) {
                            popUpTo(NavScreen.Login.route) { inclusive = true }

                        }
                    }
                )
            }

            //Editar perfil paciente
            composable(NavScreen.EditPatientProfile.route) {
                val viewModel: PatientProfileViewModel = hiltViewModel()
                EditProfilePatientScreen(
                    onProfileSaved = {
                        navController.popBackStack()
                    }, navController = navController
                )
            }
            composable(NavScreen.PsychologistLogin.route) {
                PsychologistLoginScreen(
                    navigateToRegisterPsycho = { navController.navigate(NavScreen.RegisterPsycho.route) },
                )
            }
            composable(NavScreen.RegisterPsycho.route) {
                val psychoAuthViewModel: PsychoAuthViewModel = hiltViewModel()
                RegisterPsychoScreen(viewModel = psychoAuthViewModel)
            }

            composable (NavScreen.PsychoSignIn.route) {
                PsychoSignInScreen(
                    navigateToRegisterPsycho = { navController.navigate(NavScreen.RegisterPsycho.route) },
                    /*navigateToPsychoProfile = { ={navController.navigate(NavScreen.PsychoHome.route) */)
            }

            composable(NavScreen.RegisterPatient.route) {
                val userAuthViewModel: UserAuthViewModel = hiltViewModel()
                RegisterPatientScreen(viewModel = userAuthViewModel, navigateToRegisterPsycho = {navController.navigate(NavScreen.RegisterPsycho.route)})
            }
            //Psicologo ->


            composable(NavScreen.PsychoProfile.route){
                PsychoProfileScreen(navController)
            }

            composable(NavScreen.PsychoEdit.route){
                EditPsychoProfileScreen(navController = navController)
            }

            composable(NavScreen.PsychoHome.route){
                PsychoHomeScreen(navController)
            }

            composable(NavScreen.ChatPsycho.route){
                ChatPsychoScreen(navController)
            }

            composable("profile/{psychologistId}") { backStackEntry ->
                val psychologistId = backStackEntry.arguments?.getString("psychologistId")
                ProfilePsyFromPatScreen(psychologistId = psychologistId, navController=navController)
            }

            //Paciente
            composable(NavScreen.Home.route) { HomeScreen(navController) }

            composable(NavScreen.Perfil.route) {PerfilUsuarioScreen(
                navController = navController,
                navigateToEditProfile = {
                    navController.navigate(NavScreen.EditPatientProfile.route)
                }
            )}
            composable(NavScreen.Chat.route) { ChatUsuarioScreen(navController) }
            composable(NavScreen.Formativo.route) { RecomendacionScreen(navController) }

            // Recomendaciones
            composable(
                route = "${NavScreen.ListRecomendation.route}/{tag}",
                arguments = listOf(navArgument("tag") { type = NavType.StringType })
            ) { backStackEntry ->
                // Obtener el tag de la entrada de la pila de navegación
                val tag = backStackEntry.arguments?.getString("tag")
                if (tag != null) {
                    // Pasar el tag y el navController a la pantalla
                    BuscarPorTagScreen(tag = tag, navController = navController)
                }
            }




            //Agendar Citas
            composable(NavScreen.DisponibilidadCalendario.route) {
                DisponibilidadScreen(viewModel = hiltViewModel())
            }

            //Recomendaciones para Paciente

            composable(NavScreen.BookRecommendations.route) {
                BookRecommendations(navController)
            }

            composable(NavScreen.ArticlesRecommendations.route) {
                ArticleScreen(navController)
            }

            composable(NavScreen.CallSosRecommendations.route) {
                SosDialCardScreen(navController)
            }
        }
    }
}

@Composable
fun ScaffoldOrContent(
    navController: NavController,
    scaffoldScreens: List<String>,
    scaffoldPsycho: List<String>,
    content: @Composable (PaddingValues) -> Unit
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    if (currentDestination in scaffoldScreens) {
        Scaffold(
            bottomBar = { NavigationInferior(navController) },
            content = { paddingValues -> content(paddingValues) }
        )
    } else if (currentDestination in scaffoldPsycho) {
        Scaffold(
            bottomBar = { NavigationInferiorPsycho(navController) },
            content = { paddingValues -> content(paddingValues) }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) { content(PaddingValues()) }
    }
}