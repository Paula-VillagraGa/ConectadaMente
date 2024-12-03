package com.example.conectadamente.navegation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController


@Composable
fun NavigationInferior(navController: NavController) {
    val currentRoute = currentRoute(navController)

    BottomAppBar {
        NavigationBar {
            bottomNavItems.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(imageVector = item.icon!!, contentDescription = item.title)
                    },
                    label = { Text(item.title) },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun NavigationInferiorPsycho(navController: NavController) {
    val currentRoute = currentRoute(navController)

    BottomAppBar {
        NavigationBar {
            bottomNavItemsPsycho.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(imageVector = item.icon!!, contentDescription = item.title)
                    },
                    label = { Text(item.title) },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}


