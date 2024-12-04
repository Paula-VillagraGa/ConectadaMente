package com.example.conectadamente.ui.homeUser

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.conectadamente.R
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.navegation.NavScreen
import com.example.conectadamente.ui.theme.Purple20
import com.example.conectadamente.ui.theme.Purple30
import com.example.conectadamente.ui.theme.Purple50
import com.example.conectadamente.ui.theme.Purple60
import com.example.conectadamente.ui.theme.Purple80
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
                ContentSection(navController)
                    }
                }
            }
        }

// Barra superior (TopAppBar)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    CenterAlignedTopAppBar(
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.logo2), // Reemplaza con tu recurso de imagen
                contentDescription = "Logo",
                modifier = Modifier
                    .size(220.dp) // Ajusta el tamaño según necesites
                    .padding(start = 8.dp)
                    .padding(top = 30.dp)// Aplica un recorte circular si es necesario
            )
        },
        title = {
            Text(
                text = "", // Puedes añadir un título si lo necesitas
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* Acción para configuración */ }) {
                    Icon(
                        imageVector = Icons.Filled.Settings, // Ícono predeterminado de configuración
                        contentDescription = "Settings",
                        tint = Color(0xFF100E1B)
                    )
                }
            }
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    )
}



// Campo de búsqueda para psicólogos por nombre
@OptIn(ExperimentalMaterial3Api::class)
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
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent

        ),

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
            .padding(vertical = 2.dp)
            .background(Color(0xFFDCDBDB), RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate("profile/${psychologist.id}")
            }
            .padding(8.dp)
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


@Composable
fun ContentSection(navController: NavHostController) {
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
                onClick = {  navController.navigate(NavScreen.BookRecommendations.route) },
                modifier = Modifier
                    .weight(0.5f)
                    .height(200.dp),
                color = Purple20
            )
            SupportCardC(
                title = "Título 2",
                subtitle = "Ciencia y Salud Mental",
                imageRes = R.drawable.cientifico,
                onClick = {  },
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
            SupportCardC(
                title = "Título 3",
                subtitle = "Ciencia y Salud Mental",
                imageRes = R.drawable.laboratory,
                onClick = {  navController.navigate(NavScreen.ArticlesRecommendations.route)},
                modifier = Modifier
                    .weight(0.6f)
                    .height(200.dp),
                color = Purple60
            )
            SupportCard(
                title = "Título 4",
                subtitle = "Descripción breve",
                imageRes = R.drawable.danza2,
                onClick = { },
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
            onClick = { },
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
                .weight(1f)
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
fun SupportCardC(
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
        Box( // Usamos un Box para controlar la posición de la imagen
            modifier = Modifier
                .fillMaxWidth() // Hace que la imagen ocupe el ancho del contenedor
        )
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp) // Tamaño de la imagen
                .align(Alignment.CenterHorizontally) // Mueve la imagen hacia la derecha
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
