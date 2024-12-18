package com.example.conectadamente.ui.viewModel.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.model.ReviewModel
import com.example.conectadamente.data.repository.reviews.ReviewRepository
import com.example.conectadamente.utils.constants.DataState
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _ratingState = MutableStateFlow<DataState<String>>(DataState.Loading)
    val ratingState: StateFlow<DataState<String>> get() = _ratingState

    private val _isSubmitting = MutableLiveData<Boolean>()
    val isSubmitting: LiveData<Boolean> get() = _isSubmitting

    // Obtener el promedio de las calificaciones
    suspend fun getAverageRating(psychoId: String): Double {
        return reviewRepository.getAverageRating(psychoId)
    }

    // Verificar si la cita ha sido realizada antes de permitir la reseña
    suspend fun canSubmitReview(patientId: String, psychoId: String): Boolean {
        val appointment = reviewRepository.getAppointment(patientId, psychoId)

        // Verificamos que la cita no sea null y que su estado sea "Realizada"
        return appointment?.estado == "Realizada"
    }
    fun submitRating(
        psychoId: String,
        patientId: String,
        tags: List<String>,
        rating: Double
    ) {
        viewModelScope.launch {
            _isSubmitting.value = true
            _ratingState.value = DataState.Loading

            try {
                val existingReview = reviewRepository.getExistingReview(psychoId, patientId)

                if (existingReview != null) {
                    // Actualizar la reseña
                    reviewRepository.updateReview(existingReview.id, psychoId, patientId, tags, rating)
                    _ratingState.value = DataState.Success("Reseña actualizada correctamente")
                } else {
                    // Crear una nueva reseña
                    reviewRepository.createReview(psychoId, patientId, tags, rating)
                    _ratingState.value = DataState.Success("Reseña creada correctamente")
                }
            } catch (e: Exception) {
                _ratingState.value = DataState.Error("Error: ${e.message}")
            } finally {
                _isSubmitting.value = false // Restablecer el estado de envío
            }
        }
    }

    // Obtener una reseña existente
    suspend fun getExistingReview(psychoId: String, patientId: String): ReviewModel? {
        return reviewRepository.getExistingReview(psychoId, patientId)
    }
}
