package com.example.conectadamente.ui.homeUser.Recomendacion

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.conectadamente.R
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.ui.homeUser.SearchField
import com.example.conectadamente.ui.homeUser.SearchResultCard
import com.example.conectadamente.ui.theme.Purple30
import com.example.conectadamente.ui.viewModel.BuscarPorTagViewModel

@Composable
fun BuscarPorTagScreen(tag: String, navController: NavHostController) {
    // Usamos hiltViewModel para obtener el ViewModel
    val viewModel: BuscarPorTagViewModel = hiltViewModel()
    // Llamamos a la función del ViewModel para filtrar los psicólogos por la etiqueta (tag)
    LaunchedEffect(tag) {
        viewModel.tagsQuery.value = listOf(tag)  // Establecemos la etiqueta para la búsqueda
        viewModel.searchPsychologists()  // Realizamos la búsqueda por etiquetas
    }

    // Obtenemos la lista de psicólogos filtrados
    val filteredPsychologists = viewModel.filteredPsychologists

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
    ) {
        // Puedes agregar un campo de búsqueda adicional aquí si lo deseas
        // Como la búsqueda es por tags, este campo puede estar vacío por ahora.
        SearchField(
            query = "", // No estamos buscando por nombre aquí
            onQueryChanged = { },
            onSearch = { },
            searchResults = filteredPsychologists,
            navController = navController
        )

        // Mostrar los psicólogos filtrados
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredPsychologists) { psicologo ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            // Navegar al detalle del psicólogo
                            navController.navigate("detallePsicologo/${psicologo.id}")
                        }
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .shadow(4.dp, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.Gray) // Aquí puedes reemplazar con una imagen de perfil
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = psicologo.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(
                                text = psicologo.tagsSpecific?.joinToString(", ") ?: "",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}



