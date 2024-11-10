package com.example.conectadamente.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.screensLogin.CreateAccountScreen
import com.example.conectadamente.screensLogin.LoginScreen
import com.example.conectadamente.screensLogin.SignInScreen


@Composable
fun NavigationWrapper(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login_screen"){
        composable("login_screen") { LoginScreen(navController = navController) }
        composable("create_account_screen") { CreateAccountScreen(navController = navController) }
        composable("sign_in_screen") { SignInScreen(navController = navController)

            }
        }
    }
