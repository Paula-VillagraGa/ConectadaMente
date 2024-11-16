package com.example.conectadamente.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.data.database.AppDatabase
import com.example.conectadamente.ui.auth.CreateAccountScreen
import com.example.conectadamente.ui.auth.LoginScreen
import com.example.conectadamente.ui.auth.SignInScreen
import com.example.conectadamente.ui.register.RegisterPatientScreen
import com.example.conectadamente.ui.viewModel.UserViewModel
import com.example.conectadamente.ui.viewModel.UserViewModelFactory



@Composable
fun NavigationWrapper(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login"){
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
                navigateToRegisterPacient = { navController.navigate("register_patient") }
            ) // Define esta pantalla según lo necesites
        }

        composable("create_account") {
            CreateAccountScreen(
                navigateToLoginScreen = { navController.navigate("login") }
            ) // Define esta pantalla según lo necesites
        }

        composable("register_patient") {
            val context = LocalContext.current
            val userViewModel: UserViewModel = viewModel(
                factory = UserViewModelFactory(AppDatabase.getDatabase(context).patientDao())
            )
            // Ahora se pasa el navController como parámetro
            RegisterPatientScreen(viewModel = userViewModel)
        }


    }
}
