package com.example.conectadamente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.core.navigation.NavigationWrapper
import com.example.conectadamente.screensLogin.LoginScreen
import com.example.conectadamente.screensLogin.CreateAccountScreen
import com.example.conectadamente.screensLogin.SignInScreen
import com.example.conectadamente.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme() {
                NavigationWrapper()
            }
        }
    }
}
