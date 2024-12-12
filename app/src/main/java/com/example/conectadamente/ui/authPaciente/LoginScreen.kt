package com.example.conectadamente.ui.authPaciente

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectadamente.R
import com.example.conectadamente.ui.theme.*

@Composable
fun LoginScreen(
    navController: NavController,
    navigateToSignIn: () -> Unit,
    navigateToRegisterPatient: () -> Unit

) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.meditacion),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(300.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "Bienvenido a ConectadaMente",
                    style = TextStyle(
                        fontFamily = PoppinsFontFamily,
                        fontStyle = FontStyle.Italic,
                        fontSize = 30.sp,
                        color = Purple20,
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { navigateToSignIn() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple20)
        ) {
            Text(text = "Iniciar Sesión", style = TextStyle(color = Color.White, fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navigateToRegisterPatient() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple30)
        ) {
            Text(text = "Registrarse", style = TextStyle(color = Color.White, fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Icono de Instagram (PNG)
        Row(
            modifier = Modifier
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("instagram.com/conectadamente.cl/"))
                    context.startActivity(intent)
                }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_instagram), // Asegúrate de que ic_instagram.png esté en res/drawable
                contentDescription = "Instagram",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "conectadamente.cl",
                style = TextStyle(color = Purple20, fontSize = 16.sp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Icono de Correo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:conectadamente.app@gmail.com")
                        putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre la aplicación ConectadaMente")
                    }
                    context.startActivity(emailIntent)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email), // Reemplaza con tu recurso de ícono
                    contentDescription = "Correo",
                    tint = Color(0xFF0072C6),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "conectadamente.app@gmail.com",
                style = TextStyle(color = Purple20, fontSize = 16.sp)
            )
        }
    }
}
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current), navigateToSignIn = {}, navigateToRegisterPatient = {})
}