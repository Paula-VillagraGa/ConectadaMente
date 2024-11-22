package com.example.conectadamente.navegation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.conectadamente.ui.homeUser.ChatUsuarioScreen
import com.example.conectadamente.ui.homeUser.FormativoUsuarioScreen
import com.example.conectadamente.ui.homeUser.HomeScreen
import com.example.conectadamente.ui.homeUser.PerfilUsuarioScreen
import com.example.conectadamente.ui.homeUser.RecomendacionUsuarioScreen

@Composable
fun BancoNavigation(
    navController: NavHostController
){
    NavHost(
        navController = navController ,
        startDestination = NavScreen.HomeScreen.name
    ){
        composable(NavScreen.HomeScreen.name){
            HomeScreen(navController)
        }

        composable(NavScreen.PerfilUsuarioScreen.name){
            PerfilUsuarioScreen()
        }
        composable(NavScreen.ChatScreen.name){
            ChatUsuarioScreen(navController)
        }
        composable(NavScreen.RecomendacionScreen.name){
            RecomendacionUsuarioScreen(navController)
        }
        composable(NavScreen.FormativoScreen.name){
            FormativoUsuarioScreen(navController)
        }
    }

}