package com.example.conectadamente.ui.viewModel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.data.repository.AuthPsychoRepository
import com.example.conectadamente.data.repository.reviews.ReviewRepository
import com.example.conectadamente.utils.constants.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PsychoAuthViewModel @Inject constructor(
    private val authPsychoRepository: AuthPsychoRepository,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {

    private val _authState = MutableStateFlow<DataState<String>>(DataState.Finished)
    val authState: StateFlow<DataState<String>> = _authState.asStateFlow()

    private val _psychologistsState = MutableStateFlow<List<PsychoModel>>(emptyList())
    val psychologistsState: StateFlow<List<PsychoModel>> = _psychologistsState.asStateFlow()

    private val _selectedPsychologist = MutableStateFlow<PsychoModel?>(null)
    val selectedPsychologist: StateFlow<PsychoModel?> = _selectedPsychologist.asStateFlow()

    private val _profileState = MutableStateFlow<DataState<PsychoModel?>>(DataState.Loading)
    val profileState: StateFlow<DataState<PsychoModel?>> = _profileState.asStateFlow()

    var query = mutableStateOf("")

    init {
        fetchPsychologists()
    }

    fun fetchPsychologists() {
        viewModelScope.launch {
            val psychologists = authPsychoRepository.getPsychologists()
            _psychologistsState.value = psychologists
        }
    }

    fun registerPsycho(psycho: PsychoModel, password: String, documents: List<Uri>) {
        viewModelScope.launch {
            authPsychoRepository.registerPsycho(psycho, password, documents).collect { state ->
                _authState.value = state
            }
        }
    }

    fun searchPsychologists() {
        viewModelScope.launch {
            val filtered = authPsychoRepository.getPsychosByName(query.value)
            _psychologistsState.value = filtered
        }
    }

    fun getPsychoById(id: String) {
        viewModelScope.launch {
            try {
                // Llamada al repositorio para obtener los datos
                val psycho = authPsychoRepository.getPsychoById(id)
                _profileState.value = DataState.Success(psycho)
            } catch (e: Exception) {
                _profileState.value = DataState.Error("Error al cargar el perfil")
            }
        }
    }

    fun loadCurrentProfile() {
        viewModelScope.launch {
            _profileState.value = DataState.Loading // Cambia el estado a cargando
            try {
                val profile = authPsychoRepository.getCurrentPsychologistProfile()
                _profileState.value = DataState.Success(profile)
            } catch (e: Exception) {
                _profileState.value = DataState.Error("Error al cargar el perfil")
            }
        }
    }
        private val _psychoState = MutableLiveData<PsychoModel>()
        val psychoState: LiveData<PsychoModel> get() = _psychoState

        // Obtener la información del psicólogo junto con su calificación promedio
        fun getPsychoDetails(psychoId: String) {
            viewModelScope.launch {
                // Obtener las reseñas y calcular el promedio
                val averageRating = reviewRepository.getAverageRating(psychoId)

                // Obtener los detalles del psicólogo
                val psycho = authPsychoRepository.getPsychoById(psychoId)
                if (psycho != null) {
                    _psychoState.value = psycho.copy(rating = averageRating)
                }
            }
        }
    }

