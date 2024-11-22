package com.example.conectadamente


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.conectadamente.navegation.NavigationWrapper
import com.example.conectadamente.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establecemos el contenido con el wrapper de navegación
        setContent {
            MyApplicationTheme {
                //Navegación acá
                NavigationWrapper()
            }
        }
    }
}
