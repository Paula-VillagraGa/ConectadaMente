package com.example.conectadamente.ui.authPaciente

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
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
    navigateToRegisterPatient: () -> Unit,
    navigateToPsychologistLogin: () -> Unit
) {
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

        Spacer(modifier = Modifier.height(16.dp))
    }
    // Texto cliqueable en la esquina superior derecha
    Text(
        text = "Ingresar como Psicólogo",
        modifier = Modifier
            .padding(8.dp)
            .clickable { navigateToPsychologistLogin() },
        style = TextStyle(
            color = Purple20,
            fontSize = 14.sp,
            textDecoration = TextDecoration.Underline
        )
    )
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current), navigateToSignIn = {}, navigateToRegisterPatient = {}, navigateToPsychologistLogin = {})
}