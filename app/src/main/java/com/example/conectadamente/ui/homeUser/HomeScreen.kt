package com.example.conectadamente.ui.homeUser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.conectadamente.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.ui.theme.*
import com.example.conectadamente.ui.viewModel.HomeUserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeUserViewModel = hiltViewModel()) {

    Scaffold(
        topBar = { TopAppBar() },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                SearchField( query = viewModel.query.value,
                    onQueryChanged = { newQuery -> viewModel.query.value = newQuery },
                    onSearch = { viewModel.searchPsychologists() },
                    searchResults = viewModel.filteredPsychologists,
                    navController = navController)
                ContentSection(onCardClick = { cardTitle ->
                    // Acción a realizar según la tarjeta seleccionada
                    when (cardTitle) {
                        "Recomendación 1" -> {
                            // Navegar o mostrar contenido relacionado con la Recomendación 1
                            navController.navigate("mental_health")
                        }
                        "Recomendación 2" -> {
                            // Navegar o mostrar contenido relacionado con la Recomendación 2
                        }
                        else -> {
                            // Otra acción
                        }
                    }
                })
            }
        }
    }
}

// Barra superior (TopAppBar)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text("", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        actions = {
            IconButton(onClick = { /* Acción para configuración */ }) {
                Icon(
                    imageVector = Icons.Filled.Settings, // Ícono predeterminado de configuración
                    contentDescription = "Settings",
                    tint = Color(0xFF100E1B)
                )
            }
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    )
}



// Campo de búsqueda para psicólogos por nombre
@Composable
fun SearchField(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    searchResults: List<PsychoModel>,
    navController: NavHostController
) {
    var showResults by remember { mutableStateOf(false) }
    var filteredResults by remember { mutableStateOf(searchResults) }
    val coroutineScope = rememberCoroutineScope()


    // Para realizar un debounce, esperando un pequeño tiempo después de que el usuario deje de escribir
    LaunchedEffect(query) {
        coroutineScope.launch {
            delay(500)
            filteredResults = searchResults.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
    }
    OutlinedTextField(
        value = query,
        onValueChange = {   onQueryChanged(it)
            if (it.isNotEmpty()) {
                showResults = true
            } else {
                showResults = false
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFE9E7F3), RoundedCornerShape(16.dp)),
        placeholder = {
            Text("Buscar Psicólogo", color = Purple50)
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ico_recomen),
                contentDescription = "Buscar",
                tint = Color(0xFF5A4E97)
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            })
    )
    if (showResults) {
        SearchResults(
            psychologists = filteredResults,
            navController = navController
        )
    }

}
    @Composable
    fun SearchResults(psychologists: List<PsychoModel>, navController: NavHostController) {
        LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            items(psychologists) { psychologist ->
                SearchResultCard(psychologist,  navController = navController)
            }
        }
    }
// Componente para cada resultado
@Composable
fun SearchResultCard(psychologist: PsychoModel, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFFDCDBDB), RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate("profile/${psychologist.id}")
            }
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ico_perfil),
            contentDescription = "Psicólogo",
            tint = Color(0xFF5A4E97),
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = psychologist.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Purple80
        )
    }
}

// Tarjeta de recomendación utilizando aspectRatio para tamaño flexible
@Composable
fun RecommendationCardHome(title: String, imageRes: Int) {
    Column(
        modifier = Modifier
            .width(200.dp)  // Controla el ancho de la tarjeta
            .padding(8.dp)
            .background(Color(0xFFE9E7F3), RoundedCornerShape(12.dp))
    ) {
        // Imagen con una relación de aspecto más alta que ancha (1:1.5)
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,  // Asegura que la imagen se recorte para llenar el espacio
            modifier = Modifier
                .fillMaxWidth()  // La imagen ocupa todo el ancho disponible
                .aspectRatio(1f / 1.5f)  // Relación de aspecto invertida para que sea más larga que ancha
                .clip(RoundedCornerShape(12.dp))  // Redondea las esquinas de la imagen
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = Color(0xFF100E1B),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

//prueba diseño nuevo
@Composable
fun ContentSection(onCardClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Fila superior con 2 tarjetas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SupportCard(
                title = "",
                subtitle = "Lecturas Recomendadas",
                imageRes = R.drawable.leer2,
                onClick = { onCardClick("Título 1") },
                modifier = Modifier
                    .weight(0.5f)
                    .height(200.dp),
                color = Purple20
            )
            SupportCard(
                title = "Título 2",
                subtitle = "Actividades Recomendadas",
                imageRes = R.drawable.telefono,
                onClick = { onCardClick("Título 2") },
                modifier = Modifier
                    .height(200.dp)
                    .weight(0.8f),
                color = Purple30
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fila del medio con 2 tarjetas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SupportCard(
                title = "Título 3",
                subtitle = "Descripción breve",
                imageRes = R.drawable.telefono,
                onClick = { onCardClick("Título 3") },
                modifier = Modifier
                    .weight(0.6f)
                    .height(200.dp),
                color = Purple60
            )
            SupportCard(
                title = "Título 4",
                subtitle = "Descripción breve",
                imageRes = R.drawable.danza2,
                onClick = { onCardClick("Título 4") },
                modifier = Modifier
                    .weight(0.5f)
                    .height(200.dp),
                color = Purple50
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta larga inferior
        SupportCardB(
            title = "Contactos de emergencia",
            subtitle = "",
            imageRes = R.drawable.telefono_inteligente,
            onClick = { onCardClick("Título 5") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            color = Purple20
        )
    }
}

@Composable
fun SupportCard(
    title: String,
    subtitle: String,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFE9E7F3)
) {
    Column(
        modifier = modifier
            .background(color, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(bottom = 10.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth() // Ajusta la imagen al ancho disponible
                .weight(1f) // Da mayor espacio a la imagen
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun SupportCardB(
    title: String,
    subtitle: String,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFE9E7F3)
) {
    Row(
        modifier = modifier
            .background(color, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically // Alinea verticalmente los elementos al centro
    ) {
        // Sección de texto a la izquierda
        Column(
            modifier = Modifier
                .weight(2f) // Ajusta el espacio ocupado por el texto
                .padding(end = 4.dp) // Espacio entre el texto y la imagen
        ) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp

            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = Color.White,
                fontSize = 14.sp
            )
        }

        // Imagen a la derecha
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(1f) // Ajusta el espacio ocupado por la imagen
                .size(150.dp) // Tamaño fijo para la imagen
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

@Composable
fun HomeScreenPreviewSimplified() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Pantalla Simplificada", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        ContentSection(onCardClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSimplified() {
    HomeScreenPreviewSimplified()
}
