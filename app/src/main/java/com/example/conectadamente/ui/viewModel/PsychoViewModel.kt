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

    private val _psychoInfo = MutableLiveData<Result<PsychoModel>>()
    val psychoInfo: LiveData<Result<PsychoModel>> get() = _psychoInfo

    private val _reviews = MutableLiveData<List<ReviewModel>>()
    val reviews: LiveData<List<ReviewModel>> get() = _reviews

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    /**
     * Función principal para cargar información del psicólogo y sus reseñas.
     */
    fun loadPsychoInfo(psychoId: String?) {
        if (psychoId.isNullOrEmpty()) {
            _error.value = "El ID del psicólogo no puede estar vacío."
            return
        }

        viewModelScope.launch {
            // Obtener la información del psicólogo.
            val psychoResult = psychoRepository.getPsychoInfo(psychoId)
            _psychoInfo.value = psychoResult

            if (psychoResult.isSuccess) {
                // Si la información del psicólogo es correcta, cargar las reseñas.
                val reviewsList = psychoRepository.getReviews(psychoId)
                val reviewsWithNames = reviewsList.map { review ->
                    val patientName = psychoRepository.getPatientName(review.patientId)
                    review.copy(patientName = patientName ?: "Desconocido")
                }
                _reviews.value = reviewsWithNames
            } else {
                // Manejar errores en caso de falla al cargar la información.
                _error.value = psychoResult.exceptionOrNull()?.localizedMessage ?: "Error desconocido."
            }
        }
    }
}
