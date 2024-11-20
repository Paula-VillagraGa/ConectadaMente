package com.example.conectadamente.navegation




    sealed class NavScreen(val name: String) {
        //Pantallas de Barra Inferior
        object PerfilUsuarioScreen : NavScreen("perfil_usuario")
        object ChatScreen : NavScreen("chat_usuario")
        object HomeScreen : NavScreen("home_screen")
        object RecomendacionScreen : NavScreen("recomendacion_screen")
        object FormativoScreen : NavScreen("formativo_screen")

        //Pantallas de aplicación
        object Login : NavScreen("login")
        object SignIn : NavScreen("sign_in")
        object CreateAccount : NavScreen("create_account")
        object RegisterPatient : NavScreen("register_patient")
    }
