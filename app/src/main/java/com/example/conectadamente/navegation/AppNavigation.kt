package com.example.conectadamente.navegation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.conectadamente.ui.homePsycho.EditAppointmentScreen
import com.example.conectadamente.ui.authPaciente.LoginScreen
import com.example.conectadamente.ui.authPaciente.RegisterPatientScreen
import com.example.conectadamente.ui.authPaciente.SignInScreen
import com.example.conectadamente.ui.authPsicologo.ProfesionalPerfilReseñasScreen
import com.example.conectadamente.ui.authPsicologo.PsychoSignInScreen
import com.example.conectadamente.ui.authPsicologo.PsychologistLoginScreen
import com.example.conectadamente.ui.authPsicologo.RegisterPsychoScreen
import com.example.conectadamente.ui.homePsycho.AvailabilityScreen
import com.example.conectadamente.ui.homePsycho.ChatPsychoScreen
import com.example.conectadamente.ui.homePsycho.CompletedAppointmentsScreen
import com.example.conectadamente.ui.homePsycho.EditPsychoProfileScreen
import com.example.conectadamente.ui.homePsycho.PsychoHomeScreen
import com.example.conectadamente.ui.homeUser.ProfilePsyFromPatScreen
import com.example.conectadamente.ui.homePsycho.PsychoProfileScreen
import com.example.conectadamente.ui.homePsycho.ReservedAppointmentsScreen
import com.example.conectadamente.ui.homeUser.AppointmentPatientScreen
import com.example.conectadamente.ui.homeUser.EditProfilePatientScreen
import com.example.conectadamente.ui.homeUser.HomeScreen
import com.example.conectadamente.ui.homeUser.PerfilUsuarioScreen
import com.example.conectadamente.ui.homeUser.Recomendacion.BuscarPorTagScreen
import com.example.conectadamente.ui.homeUser.Recomendacion.RecomendacionScreen
import com.example.conectadamente.ui.homeUser.calendar.AgendarScreen
import com.example.conectadamente.ui.homeUser.recommendationsPatient.ArticleScreen
import com.example.conectadamente.ui.homeUser.recommendationsPatient.BookRecommendations
import com.example.conectadamente.ui.homeUser.recommendationsPatient.SosDialCardScreen
import com.example.conectadamente.ui.viewModel.PsychoAuthViewModel
import com.example.conectadamente.ui.viewModel.PsychoViewModel
import com.example.conectadamente.ui.viewModel.UserAuthViewModel
import com.example.conectadamente.ui.viewModel.reviews.ReviewViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val scaffoldScreens = listOf(
        NavScreen.Home.route,
        NavScreen.Perfil.route,
        NavScreen.AppointmentPatient.route,
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
                    navigateToPsychoHomeScreen = {
                        navController.navigate(NavScreen.PsychoHome.route) {
                            popUpTo(NavScreen.Login.route) { inclusive = true }
                        }},

                    navigateToHomeScreen = {
                        navController.navigate(NavScreen.Home.route) {
                            popUpTo(NavScreen.Login.route) { inclusive = true }

                        }
                    }
                )
            }

            composable(NavScreen.Perfil.route){
                PerfilUsuarioScreen(viewModel= hiltViewModel(), navigateToEditProfile = {navController.navigate(NavScreen.EditPatientProfile.route) }, navController = navController)
            }

            //Editar perfil paciente
            composable(NavScreen.EditPatientProfile.route) {
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

            composable(NavScreen.AppointmentPatient.route){
                AppointmentPatientScreen(viewModel = hiltViewModel(), psychoId = "", navController)
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

            composable(
                route = NavScreen.ReseñasDetalle.route + "/{psychoId}",
                arguments = listOf(navArgument("psychoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val psychoId = backStackEntry.arguments?.getString("psychoId")
                val viewModel: PsychoViewModel = hiltViewModel()

                // Llamar al ViewModel para cargar la información
                LaunchedEffect(psychoId) {
                    viewModel.loadPsychoInfo(psychoId)
                }

                val psycho = viewModel.psychoInfo.observeAsState().value?.getOrNull()
                val reviews = viewModel.reviews.observeAsState(emptyList()).value
                val error = viewModel.error.observeAsState().value

                // Si hay un error, podemos mostrar un mensaje de error o navegar a otra pantalla
                error?.let {
                    // Maneja el error aquí (mostrar un mensaje de error, por ejemplo)
                    Log.e("PsychoViewModel", it)
                    return@composable
                }

                if (psycho != null) {
                    ProfesionalPerfilReseñasScreen(
                        psycho = psycho,
                        reviews = reviews,
                        navController = navController
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
            composable(
                route = "reviews_screen/{psychoId}",
                arguments = listOf(navArgument("psychoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val psychoId = backStackEntry.arguments?.getString("psychoId")
                val viewModel: PsychoViewModel = hiltViewModel()

                // Llamar al ViewModel para cargar la información
                LaunchedEffect(psychoId) {
                    viewModel.loadPsychoInfo(psychoId)
                }

                val psycho = viewModel.psychoInfo.observeAsState().value?.getOrNull()
                val reviews = viewModel.reviews.observeAsState(emptyList()).value
                val error = viewModel.error.observeAsState().value

                // Si hay un error, podemos mostrar un mensaje de error o navegar a otra pantalla
                error?.let {
                    // Maneja el error aquí (mostrar un mensaje de error, por ejemplo)
                    Log.e("PsychoViewModel", it)
                    return@composable
                }

                if (psycho != null) {
                    ProfesionalPerfilReseñasScreen(
                        psycho = psycho,
                        reviews = reviews,
                        navController = navController
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }


            composable(NavScreen.Formativo.route) { RecomendacionScreen(navController) }

            // Recomendaciones
            composable(
                route = "${NavScreen.ListRecomendation.route}/{inputText}/{tag}",
                arguments = listOf(
                    navArgument("inputText") { type = NavType.StringType },
                    navArgument("tag") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                // Obtener los parámetros de la entrada de la pila de navegación
                val inputText = backStackEntry.arguments?.getString("inputText")
                val tag = backStackEntry.arguments?.getString("tag")

                if (!inputText.isNullOrEmpty() && !tag.isNullOrEmpty()) {
                    // Pasar los parámetros a la pantalla de recomendaciones
                    BuscarPorTagScreen(inputText = inputText, tag = tag, navController = navController)
                } else {
                    val context = LocalContext.current
                    // Manejo de error si los parámetros no son válidos
                    Toast.makeText(context, "Faltan parámetros en la navegación", Toast.LENGTH_SHORT).show()
                }
            }





            //Agendar Citas
            composable(NavScreen.DisponibilidadCalendario.route) {
                AvailabilityScreen(viewModel = hiltViewModel(), navController)
            }
            composable("agendarCita/{psychoId}/{patientId}") { backStackEntry ->
                val psychoId = backStackEntry.arguments?.getString("psychoId") ?: ""
                val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
                AgendarScreen(viewModel = hiltViewModel(), psychoId = psychoId, patientId = patientId, navController)
            }

            composable(NavScreen.CitasReservadas.route) {
                ReservedAppointmentsScreen(viewModel = hiltViewModel(), navController)
            }
            composable(
                route = "editarCita/{appointmentId}?fechaHora={fechaHora}&paciente={paciente}",
                arguments = listOf(
                    navArgument("appointmentId") { type = NavType.StringType },
                    navArgument("fechaHora") { type = NavType.StringType },
                    navArgument("paciente") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backStackEntry ->
                val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
                val fechaHora = backStackEntry.arguments?.getString("fechaHora") ?: ""
                val paciente = backStackEntry.arguments?.getString("paciente") ?: ""
                EditAppointmentScreen(
                    appointmentId = appointmentId,
                    fechaHora = fechaHora,
                    paciente = paciente,
                    navController = navController,
                    viewModel = hiltViewModel()
                )
            }
            composable(NavScreen.Home.route){
                HomeScreen(navController)
            }
            composable(NavScreen.CompletedAppointments.route) {
                CompletedAppointmentsScreen(viewModel = hiltViewModel())
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

    when (currentDestination) {
        in scaffoldScreens -> {
            Scaffold(
                bottomBar = { NavigationInferior(navController) },
                content = { paddingValues -> content(paddingValues) }
            )
        }
        in scaffoldPsycho -> {
            Scaffold(
                bottomBar = { NavigationInferiorPsycho(navController) },
                content = { paddingValues -> content(paddingValues) }
            )
        }
        else -> {
            Box(modifier = Modifier.fillMaxSize()) { content(PaddingValues()) }
        }
    }
}