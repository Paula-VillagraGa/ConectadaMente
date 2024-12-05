package com.example.conectadamente.ui.homePsycho

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.conectadamente.ui.viewModel.PsychoAuthViewModel
import com.example.conectadamente.utils.constants.DataState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPsychoProfileScreen(
    viewModel: PsychoAuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val profileState by viewModel.profileState.collectAsState()
    val context = LocalContext.current
    var phone by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var selectedSpecializations by remember { mutableStateOf(listOf<String>()) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val specializations = listOf("Psicología Clínica", "Psicología Educacional", "Psicología Organizacional") // Lista de ejemplo

    LaunchedEffect(profileState) {
        if (profileState is DataState.Success) {
            val psycho = (profileState as DataState.Success).data
            phone = psycho?.phone.orEmpty()
            description = psycho?.descriptionPsycho.orEmpty()
            experience = psycho?.experience.orEmpty()
            selectedSpecializations = psycho?.specialization ?: listOf()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = experience,
                onValueChange = { experience = it },
                label = { Text("Experiencia") },
                modifier = Modifier.fillMaxWidth()
            )
            Text("Especializaciones:")
            LazyColumn {
                items(specializations) { spec ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = spec in selectedSpecializations,
                            onCheckedChange = {
                                selectedSpecializations = if (it) {
                                    selectedSpecializations + spec
                                } else {
                                    selectedSpecializations - spec
                                }
                            }
                        )
                        Text(text = spec)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Selección de imagen
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                }
                (context as Activity).startActivityForResult(intent, 1000)
            }) {
                Text("Subir Foto")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.updateProfile(
                    phone = phone,
                    description = description,
                    experience = experience,
                    specializations = selectedSpecializations,
                    imageUri = imageUri
                )
                navController.popBackStack()
            }) {
                Text("Guardar Cambios")
            }
        }
    }
}
