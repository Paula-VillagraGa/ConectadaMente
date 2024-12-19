package com.example.conectadamente.navegation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assistant
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavScreen(val route: String, val title: String, val icon: ImageVector?) {
    object Login : NavScreen("login", "Login", null)
    object SignIn : NavScreen("sign_in", "Iniciar Sesión", null)
    //Paciente ->
    object RegisterPatient : NavScreen("register_patient", "Registro", null)
    object Home : NavScreen("home_screen", "Inicio", Icons.Outlined.Home)
    object Perfil : NavScreen("perfil_usuario", "Perfil", Icons.Outlined.Person)
    object ArticlesRecommendations2 : NavScreen("articles_recommendations2", "Articulos", Icons.Outlined.Newspaper)
    object Formativo : NavScreen("formativo_usuario", "Asistente", Icons.Outlined.Assistant)
    object ListRecomendation : NavScreen("ListRecommendation_usuario", "ListRecomendation", null)
    object PsychologistLogin : NavScreen("psychologist_login", "Login", null)
    object EditPatientProfile: NavScreen("edit_perfil_patient", "Editar Perfil", Icons.Outlined.Info)
    object AppointmentPatient: NavScreen("appointment_patient", "Citas", Icons.Outlined.CalendarMonth)
    //Psicologo ->
    object PsychoSignIn : NavScreen("psycho_sign_in", "Iniciar Sesión Psicólogo", null)
    object PsychoProfile: NavScreen("psycho_profile", "Perfil", Icons.Outlined.Person)
    object PsychoHome: NavScreen("psycho_home", "Inicio", Icons.Outlined.Home)
    object ChatPsycho: NavScreen("chat_psycho", "Chat", Icons.Outlined.Chat)
    object RegisterPsycho : NavScreen("register_psycho", "Registro", null)
    object PsychoEdit : NavScreen("psycho_edit", "Editar Perfil", Icons.Outlined.Info)

    //Recomendaciones ->
    object BookRecommendations: NavScreen("book_recommendations", "Recomendaciones", null)
    object ArticlesRecommendations : NavScreen("articles_recommendations", "Articulos", Icons.Outlined.Newspaper)
    object CallSosRecommendations : NavScreen("call_sos_recommendations", "Recomendaciones", null)

    //Agendar citas
    object DisponibilidadCalendario: NavScreen("disponibilidad_calendario", "Disponibilidad", null)
    object AgendarCita: NavScreen("agendar_cita", "Agendar Cita", null)
    object CitasReservadas: NavScreen("citas_reservadas", "Citas Reservadas", null)
    object CompletedAppointments : NavScreen("completed_appointments", "Citas Realizadas", null)

    //Reseñas a psicologo
    object ReseñasDetalle: NavScreen("reseñas_detalle", "Reseñas", null)
}
// Lista para navegación inferior
val bottomNavItems = listOf(
    NavScreen.Home,
    NavScreen.Perfil,
    NavScreen.AppointmentPatient,
    NavScreen.Formativo,
)
// Lista para navegación inferior
val bottomNavItemsPsycho = listOf(
    NavScreen.PsychoHome,
    NavScreen.PsychoProfile,
    NavScreen.ArticlesRecommendations
)
