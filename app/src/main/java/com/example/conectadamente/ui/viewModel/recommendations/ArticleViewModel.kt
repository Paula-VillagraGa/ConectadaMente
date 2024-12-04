package com.example.conectadamente.ui.viewModel.recommendations

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.conectadamente.data.model.ArticleModel
import com.example.conectadamente.data.repository.recommendation.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State

import kotlinx.coroutines.launch


@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _articles = mutableStateOf<List<ArticleModel>>(emptyList())
    val articles: State<List<ArticleModel>> get() = _articles

    init {
        fetchArticles()
    }

    private fun fetchArticles() {
        // Llamar a la función suspendida para obtener los artículos
        viewModelScope.launch {
            val articlesList = articleRepository.getArticles()  // Obtener la lista de artículos desde Firestore
            _articles.value = articlesList.sortedByDescending { it.date }  // Ordenar por fecha descendente
        }
    }
}
