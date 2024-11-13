package com.example.conectadamente.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.ui.auth.CreateAccountScreen
import com.example.conectadamente.ui.auth.LoginScreen
import com.example.conectadamente.ui.auth.SignInScreen



@Composable
fun NavigationWrapper(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login){
        composable<Login>{
            LoginScreen(
                navigateToSignIn = { navController.navigate(SignIn) },
                navigateToGoogleSignIn = { navController.navigate(GoogleSignIn) },
                navigateToCreateAccount = { navController.navigate(CreateAccount)})

        }
        composable<SignIn> {
            SignInScreen(
                navigateToCreateAccount = {navController.navigate(CreateAccount)}
            ) // Define esta pantalla según lo necesites
        }



        composable<CreateAccount> {
            CreateAccountScreen(
                navigateToLoginScreen = {navController.navigate(Login) }
            ) // Define esta pantalla según lo necesites
        }



            }

        }

