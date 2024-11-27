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
class HomeUserViewModel @Inject constructor(
    private val authPsychoRepository: AuthPsychoRepository
) : ViewModel() {
    var psychologists = mutableStateOf<List<PsychoModel>>(emptyList())
    var query = mutableStateOf("")

    init {
        fetchPsychologists()
    }

    fun fetchPsychologists() {
        viewModelScope.launch {
            psychologists.value = authPsychoRepository.getPsychologists()
        }
    }
    val filteredPsychologists: List<PsychoModel>
        get() = psychologists.value.filter {
            it.name?.contains(query.value, ignoreCase = true) == true
        }

    fun searchPsychologists() {
        viewModelScope.launch {
            psychologists.value = authPsychoRepository.getPsychosByName(query.value)
        }
    }
}
