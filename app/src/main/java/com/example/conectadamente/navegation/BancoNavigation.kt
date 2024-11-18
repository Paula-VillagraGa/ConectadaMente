package com.example.conectadamente.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.conectadamente.ui.homeUser.Chats
import com.example.conectadamente.ui.homeUser.Formativo
import com.example.conectadamente.ui.homeUser.HomeScreen
/*import com.example.conectadamente.ui.home.Perfil*/
import com.example.conectadamente.ui.homeUser.Recomendacion

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
        /*
        composable(NavScreen.PerfilScreen.name){
            Perfil()
        }*/
        composable(NavScreen.ChatScreen.name){
            Chats()
        }
        composable(NavScreen.RecomendacionScreen.name){
            Recomendacion()
        }
        composable(NavScreen.FormativoScreen.name){
            Formativo()
        }
    }

}