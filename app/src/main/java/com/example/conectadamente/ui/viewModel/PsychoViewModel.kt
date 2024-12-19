package com.example.conectadamente.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.data.repository.PsychoRepository
import androidx.lifecycle.*
import com.example.conectadamente.data.model.ReviewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PsychoViewModel @Inject constructor(
    private val psychoRepository: PsychoRepository
) : ViewModel() {

    // No necesitamos obtener psychoId de Firebase en el ViewModel. Lo pasamos como argumento.
    private val _psychoInfo = MutableLiveData<Result<PsychoModel>>()
    val psychoInfo: LiveData<Result<PsychoModel>> get() = _psychoInfo

    private val _reviews = MutableLiveData<List<ReviewModel>>()
    val reviews: LiveData<List<ReviewModel>> get() = _reviews

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun loadPsychoInfo(psychoId: String?) {
        if (psychoId.isNullOrEmpty()) {
            _error.value = "psychoId is null or empty"
            return
        }

        viewModelScope.launch {
            // Intentamos obtener la información del psicólogo
            val psychoResult = psychoRepository.getPsychoInfo(psychoId)
            _psychoInfo.value = psychoResult

            // Si la información del psicólogo fue cargada con éxito, cargamos las reseñas
            if (psychoResult.isSuccess) {
                val reviews = psychoRepository.getReviews(psychoId)
                _reviews.value = reviews
            } else {
                // En caso de error al obtener la información del psicólogo
                _error.value = "Failed to load psycho information"
            }
        }
    }
}
