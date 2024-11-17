package com.example.conectadamente.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conectadamente.ui.theme.*


@Composable
fun UserProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)) // Fondo similar a bg-slate-50
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF0e141b)
            )
            Text(
                text = "Profile",
                style = TextStyle(
                    fontFamily = PoppinsFontFamily,   // Usar la familia de fuentes definida
                    fontStyle = FontStyle.Italic,    // Asegura que se usa el estilo itálico
                    fontSize = 30.sp,                // Tamaño de la fuente
                    color = Color.White,
                ),
                color = Color(0xFF0e141b)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Profile Picture Section
        Text(
            text = "Profile Picture",
            style = TextStyle(
                fontFamily = PoppinsFontFamily,   // Usar la familia de fuentes definida
                fontStyle = FontStyle.Italic,    // Asegura que se usa el estilo itálico
                fontSize = 30.sp,                // Tamaño de la fuente
                color = Color.White,
            ),
            color = Color(0xFF0e141b)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 2f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Gray)
                .clickable { /* Edit Profile Photo Action */ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Editar Foto de Perfil",
                modifier = Modifier.clickable {},
                style = TextStyle(
                    color = Purple40,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily,
                    fontStyle = FontStyle.Normal

                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // User Info Section
        UserInfoItem(icon = Icons.Default.Person, label = "Allison Wu", isLarge = false)
        UserInfoItem(icon = Icons.Default.Email, label = "@allisonwu")
        UserInfoItem(icon = Icons.Default.Build, label = "Software Engineer at Netflix")
        UserInfoItem(
            icon = Icons.Default.Home,
            label = "San Francisco, California\nalisonw2043@gmail.com",
            isLarge = true
        )
        UserInfoItem(
            icon = Icons.Default.DateRange,
            label = "Birthday\nJanuary 1, 1990",
            isLarge = true
        )
        UserInfoItem(
            icon = Icons.Default.Search,
            label = "Interests\nHiking, Cooking, Photography",
            isLarge = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Button(
            onClick = { /* Edit Profile Action */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple20)
        ) {
            Text(text = "Edit Profile", color = Color(0xFF0e141b))
        }
        OutlinedButton(
            onClick = { /* Change Password Action */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Change Password", color = Color(0xFF0e141b))
        }
    }
}

@Composable
fun UserInfoItem(icon: ImageVector, label: String, isLarge: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (isLarge) 12.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF0e141b),
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFE7EDF3), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = TextStyle(
                fontFamily = PoppinsFontFamily,
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp,
                color = Color.White,
            ),
            color = Color(0xFF0e141b)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    UserProfileScreen()
}
