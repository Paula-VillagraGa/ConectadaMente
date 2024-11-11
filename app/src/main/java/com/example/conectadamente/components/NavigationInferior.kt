package com.example.conectadamente.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.conectadamente.models.Items_bottom_nav. *
import com.example.conectadamente.navegation.currentRoute

@Composable
fun NavigationInferior(
    navHostController: NavHostController
){
    val menu_items = listOf(
        Item_bottom_nav1,
        Item_bottom_nav2,
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