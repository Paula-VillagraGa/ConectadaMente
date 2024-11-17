package com.example.conectadamente.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Create
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
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.ui.theme.*


@Composable
fun Perfil(patient: PatientModel) {
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
                contentDescription = "Volver",
                tint = Color(0xFF0e141b)
            )
            Text(
                text = "Perfil",
                style = TextStyle(
                    fontFamily = PoppinsFontFamily,   // Usar la familia de fuentes definida
                    fontStyle = FontStyle.Italic,    // Asegura que se usa el estilo itálico
                    fontSize = 26.sp,                // Tamaño de la fuente
                ),
                color = Purple30
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        //
        Text(
            text = "Foto de Perfil",
            style = TextStyle(
                fontFamily = PoppinsFontFamily,   // Usar la familia de fuentes definida
                fontStyle = FontStyle.Italic,    // Asegura que se usa el estilo itálico
                fontSize = 20.sp,                // Tamaño de la fuente
                color = Color.White,
            ),
            color = Color(0xFF0e141b)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(

            modifier = Modifier
                .size(100.dp) // Tamaño del círculo
                .clip(CircleShape) // Hace que sea completamente redondo
                .background(Color.Gray) // Color de fondo para simular la foto
                .clickable { /* Acción para cambiar la foto */ },
        ) {
            // Contenido interno, por ejemplo, un texto o icono de cámara
            Icon(
                imageVector = Icons.Default.Person, // Icono predeterminado para representar el perfil
                contentDescription = "Foto de perfil",
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp),// Tamaño del ícono
            )
        }

        // Botón para editar la foto de perfil
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp), // Separación debajo del círculo
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { /* Acción para editar foto */ },
                shape = RoundedCornerShape(50) // Borde redondeado
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "Editar foto",
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Información del usuario
        UserInfoItem(icon = Icons.Default.Person, label = "Nombre: ${patient.firstName} ${patient.lastName}")
        UserInfoItem(icon = Icons.Default.Email, label = "Correo: ${patient.email}")
        UserInfoItem(icon = Icons.Default.Call, label = "Teléfono: ${patient.phoneNumber}")
        UserInfoItem(
            icon = Icons.Default.Home,
            label = "Dirección: San Francisco, California\n${patient.email}",
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
            Text(text = "Editar Perfil", color = Color.White)
        }
        OutlinedButton(
            onClick = { /* Change Password Action */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cambiar Contraseña", color = Color(0xFF0e141b))
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
                .size(30.dp)
                .background(Color(0xFFE7EDF3), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = TextStyle(
                fontFamily = PoppinsFontFamily,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
                color = Color.White,
            ),
            color = Color(0xFF0e141b)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    // Crear un objeto de ejemplo de PatientModel
    val samplePatient = PatientModel(
        rut = "12345678-9",
        firstName = "Allison",
        lastName = "Wu",
        email = "allisonwu@example.com",
        phoneNumber = "+123456789"
    )

    // Llamar a la pantalla de perfil pasándole el paciente de ejemplo
    ProfileScreen(patient = samplePatient)
}

@Composable
fun ProfileScreen(patient: PatientModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Nombre: ${patient.firstName} ${patient.lastName}")
        Text(text = "Correo: ${patient.email}")
        Text(text = "Teléfono: ${patient.phoneNumber}")

        // Aquí puedes agregar más información del paciente
    }
}