package com.example.conectadamente.ui.viewModel.recommendations

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.conectadamente.data.model.ArticleModel
import com.example.conectadamente.data.repository.recommendation.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import kotlinx.coroutines.launch


@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _articles = MutableLiveData<List<ArticleModel>>()
    val articles: LiveData<List<ArticleModel>> get() = _articles

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        getArticles()
    }

    fun getArticles() {
        viewModelScope.launch {
            try {
                val articlesList = articleRepository.getArticles()
                _articles.value = articlesList
            } catch (e: Exception) {
                _error.value = "Error al obtener artículos: ${e.message}"
            }
        }
    }
}
