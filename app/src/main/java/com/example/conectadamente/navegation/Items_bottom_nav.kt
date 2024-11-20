package com.example.conectadamente.navegation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Items_bottom_nav(
    val icon: ImageVector,
    val title: String,
    val ruta: String
){
    object Item_bottom_nav1: Items_bottom_nav(
        Icons.Outlined.Person,
        "Perfil",
        NavScreen.PerfilUsuarioScreen.name
    )
    object Item_bottom_nav2: Items_bottom_nav(
        Icons.Outlined.Email,
        "Chats",
        NavScreen.ChatScreen.name
    )
    object Item_Home_nav: Items_bottom_nav(
        Icons.Outlined.Home,
        "Home",
        NavScreen.HomeScreen.name
    )
    object Item_bottom_nav3: Items_bottom_nav(
        Icons.Outlined.Face,
        "Recomendacion",
        NavScreen.RecomendacionScreen.name
    )
    object Item_bottom_nav4: Items_bottom_nav(
        Icons.Outlined.Info,
        "Formativo",
        NavScreen.FormativoScreen.name
    )
}