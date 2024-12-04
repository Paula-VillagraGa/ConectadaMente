package com.example.conectadamente.ui.viewModel.recommendations

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.RecommendationModel
import com.example.conectadamente.data.repository.recommendation.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.State

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    // State que almacenará las recomendaciones
    private val _recommendations = mutableStateOf<List<RecommendationModel>>(emptyList())
    val recommendations: State<List<RecommendationModel>> = _recommendations

    // Llamamos al repositorio para obtener las recomendaciones desde Firestore
    fun fetchRecommendations() {
        viewModelScope.launch {
            try {
                _recommendations.value = repository.getRecommendations()
            } catch (e: Exception) {
                // Manejo de errores si algo sale mal
                Log.e("BookViewModel", "Error fetching recommendations: ${e.message}")
            }
        }
    }
}
