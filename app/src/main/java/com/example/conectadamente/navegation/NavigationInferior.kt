package com.example.conectadamente.navegation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.conectadamente.navegation.Items_bottom_nav. *




@Composable
fun NavigationInferior(
    navHostController: NavHostController
){
    val menu_items = listOf(
        Item_bottom_nav1,
        Item_bottom_nav2,
        Item_Home_nav,
        Item_bottom_nav3,
        Item_bottom_nav4
    )
    BottomAppBar {
        NavigationBar {
            menu_items.forEach {item->
                val  selected = currentRoute(navHostController) == item.ruta
                NavigationRailItem(
                    selected = selected,
                    onClick = {navHostController.navigate(item.ruta)},
                    icon = {
                        Icon(imageVector = item.icon,
                            contentDescription = item.title
                        )

                    },
                    label ={
                        Text(text = item.title)
                    }
                )

            }
        }
    }
}