package com.example.conectadamente.ui.viewModel.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectadamente.data.repository.reviews.ReviewRepository
import com.example.conectadamente.utils.constants.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _ratingState = MutableLiveData<DataState<String>>()
    val ratingState: LiveData<DataState<String>> get() = _ratingState

    fun submitRating(
        psychoId: String,
        patientId: String,
        tags: List<String>,
        rating: Double
    ) {
        viewModelScope.launch {
            _ratingState.value = DataState.Loading

            val result = reviewRepository.submitRating(psychoId, patientId, tags, rating)
            _ratingState.value = if (result.isSuccess) {
                DataState.Success(result.getOrNull() ?: "Reseña enviada con éxito")
            } else {
                DataState.Error(result.exceptionOrNull()?.message ?: "Error al enviar la reseña")
            }
        }
    }
}
