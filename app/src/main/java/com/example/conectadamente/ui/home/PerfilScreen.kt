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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

/*
@Composable
fun Perfil(patient: PatientModel, onSave: (PatientModel) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(patient.name) }
    var email by remember { mutableStateOf(patient.email) }
    var phoneNumber by remember { mutableStateOf(patient.phoneNumber) }

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
                    fontFamily = PoppinsFontFamily,
                    fontStyle = FontStyle.Italic,
                    fontSize = 26.sp,
                ),
                color = Purple30
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Información del usuario
        if (isEditing) {
            // Dividimos 'name' en dos partes: nombre y apellido
            val nameParts = name.split(" ", limit = 2)
            val firstName = nameParts.getOrElse(0) { "" }
            val lastName = nameParts.getOrElse(1) { "" }

            // Campos de edición
            EditableUserInfoItem(
                icon = Icons.Default.Person,
                label = "Nombre",
                value = firstName,
                onValueChange = { firstName = it }
            )
            EditableUserInfoItem(
                icon = Icons.Default.Person,
                label = "Apellido",
                value = lastName,
                onValueChange = { lastName = it }
            )
            EditableUserInfoItem(
                icon = Icons.Default.Email,
                label = "Correo",
                value = email,
                onValueChange = { email = it }
            )
            EditableUserInfoItem(
                icon = Icons.Default.Call,
                label = "Teléfono",
                value = phoneNumber,
                onValueChange = { phoneNumber = it }
            )
        } else {
            // Si no está en edición, mostrar 'name' como un solo campo
            UserInfoItem(icon = Icons.Default.Person, label = "Nombre: $name")
            UserInfoItem(icon = Icons.Default.Email, label = "Correo: $email")
            UserInfoItem(icon = Icons.Default.Call, label = "Teléfono: $phoneNumber")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones
        if (isEditing) {
            Button(
                onClick = {
                    val updatedName = "$firstName $lastName"
                    val updatedPatient = patient.copy(
                        name = updatedName,
                        email = email,
                        phoneNumber = phoneNumber
                    )
                    onSave(updatedPatient)
                    isEditing = false // Deshabilitar la edición después de guardar
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple20)
            ) {
                Text(text = "Guardar", color = Color.White)
            }
        } else {
            // Botón para activar la edición
            Button(
                onClick = { isEditing = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple20)
            ) {
                Text(text = "Editar", color = Color.White)
            }
        }
    }
}
}
*/
