package com.example.conectadamente.ui.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.data.repository.AuthPsychoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BuscarPorTagViewModel @Inject constructor(
    private val authPsychoRepository: AuthPsychoRepository
) : ViewModel() {
    var psychologists = mutableStateOf<List<PsychoModel>>(emptyList())
    var tagsQuery = mutableStateOf<List<String>>(emptyList())
    var specializationQuery = mutableStateOf<List<String>>(emptyList())

    init {
        // Si se desea obtener todos los psicólogos inicialmente sin filtro, se puede usar esto
        fetchPsychologists()
    }

    fun fetchPsychologists() {
        viewModelScope.launch {
            psychologists.value = authPsychoRepository.getPsychologists() // Obtiene todos los psicólogos
        }
    }


    // Esta función permite obtener los psicólogos filtrados por tags y especializaciones
    fun searchPsychologists() {
        viewModelScope.launch {
            // Filtra primero por tags
            var filteredList = if (tagsQuery.value.isNotEmpty()) {
                authPsychoRepository.getPsychosByTagsSpecific(tagsQuery.value)
            } else {
                authPsychoRepository.getPsychologists() // Obtiene todos los psicólogos si no hay filtro
            }

            // Ahora, filtra por especializaciones
            if (specializationQuery.value.isNotEmpty()) {
                filteredList = filteredList.filter { psycho ->
                    psycho.specialization?.any { specialization ->
                        specializationQuery.value.contains(specialization)
                    } == true
                }
            }

            psychologists.value = filteredList
        }
    }

    // Este es el método que se utiliza para realizar el filtrado de psicólogos
    val filteredPsychologists: List<PsychoModel>
        get() = psychologists.value.filter { psycho ->
            // Compara las etiquetas específicas de cada psicólogo con las etiquetas de búsqueda
            psycho.tagsSpecific?.any { tag -> tagsQuery.value.contains(tag) } == true
        }
}
