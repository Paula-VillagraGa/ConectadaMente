package com.example.conectadamente.navegation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assistant
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavScreen(val route: String, val title: String, val icon: ImageVector?) {
    object Login : NavScreen("login", "Login", null)
    object SignIn : NavScreen("sign_in", "Iniciar Sesión", null)
    object RegisterPatient : NavScreen("register_patient", "Registro", null)
    object Home : NavScreen("home_screen", "Inicio", Icons.Outlined.Home)
    object Perfil : NavScreen("perfil_usuario", "Perfil", Icons.Outlined.Person)
    object Chat : NavScreen("chat_usuario", "Chat", Icons.Outlined.Email)
    object Formativo : NavScreen("formativo_usuario", "Asistente", Icons.Outlined.Assistant)
    object PsychologistLogin : NavScreen("psychologist_login", "Login", null)
    object RegisterPsycho : NavScreen("register_psycho", "Registro", null)
    object PsychoSignIn : NavScreen("psycho_sign_in", "Iniciar Sesión Psicólogo", null)
    object PsychoProfile: NavScreen("psycho_profile", "psychoprofile", null)

}
// Lista para navegación inferior
val bottomNavItems = listOf(
    NavScreen.Home,
    NavScreen.Perfil,
    NavScreen.Chat,
    NavScreen.Formativo
)
