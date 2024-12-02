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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {


    private val _ratingState = MutableLiveData<DataState<String>>()
    val ratingState: LiveData<DataState<String>> get() = _ratingState

    suspend fun getAverageRating(psychoId: String): Double {
        return reviewRepository.getAverageRating(psychoId)
    }

    fun submitRating(
        psychoId: String,
        patientId: String,
        tags: List<String>,
        rating: Double
    ) {
        viewModelScope.launch {
            _ratingState.value = DataState.Loading

            try {
                // Verificar si ya existe una reseña para este paciente y psicólogo
                val existingReview = getExistingReview(psychoId, patientId)

                if (existingReview != null) {
                    // Si ya existe, actualizamos la reseña existente
                    firestore.collection("reviews")
                        .document(existingReview.id)
                        .set(
                            mapOf(
                                "psychoId" to psychoId,
                                "patientId" to patientId,
                                "tags" to tags,
                                "rating" to rating,
                                "timestamp" to System.currentTimeMillis()
                            )
                        )
                        .await()
                    _ratingState.value = DataState.Success("Reseña actualizada correctamente")
                } else {
                    // Si no existe, creamos una nueva
                    val newReviewRef = firestore.collection("reviews").document()
                    val reviewData = mapOf(
                        "id" to newReviewRef.id, // Añadir el ID generado automáticamente
                        "psychoId" to psychoId,
                        "patientId" to patientId,
                        "tags" to tags,
                        "rating" to rating,
                        "timestamp" to System.currentTimeMillis()
                    )

                    // Guardamos la nueva reseña
                    newReviewRef.set(reviewData).await()
                    _ratingState.value = DataState.Success("Reseña creada correctamente con ID: ${newReviewRef.id}")
                }
            } catch (e: Exception) {
                _ratingState.value = DataState.Error("Error: ${e.message}")
            }
        }

    }// update reviews
    fun upsertRating(
        psychoId: String,
        patientId: String,
        tags: List<String>,
        rating: Double
    ) {
        viewModelScope.launch {
            _ratingState.value = DataState.Loading

            try {
                // Verificar si ya existe una reseña
                val existingReview = getExistingReview(psychoId, patientId)

                if (existingReview != null) {
                    // Si ya existe, actualizamos la reseña existente
                    firestore.collection("reviews")
                        .document(existingReview.id) // Necesitamos el ID del documento
                        .set(
                            mapOf(
                                "psychoId" to psychoId,
                                "patientId" to patientId,
                                "tags" to tags,
                                "rating" to rating,
                                "timestamp" to System.currentTimeMillis()
                            )
                        )
                        .await()
                    _ratingState.value = DataState.Success("Reseña actualizada correctamente")
                } else {
                    // Si no existe, creamos una nueva
                    firestore.collection("reviews")
                        .add(
                            mapOf(
                                "psychoId" to psychoId,
                                "patientId" to patientId,
                                "tags" to tags,
                                "rating" to rating,
                                "timestamp" to System.currentTimeMillis()
                            )
                        )
                        .await()
                        .let { documentReference ->
                            // Agregar el ID generado al documento
                            documentReference.update("id", documentReference.id).await()
                            _ratingState.value = DataState.Success("Reseña creada correctamente con ID: ${documentReference.id}")
                        }
                    _ratingState.value = DataState.Success("Reseña creada correctamente")
                }
            } catch (e: Exception) {
                _ratingState.value = DataState.Error("Error: ${e.message}")
            }
        }
    }
    suspend fun getExistingReview(psychoId: String, patientId: String): ReviewModel? {
        return try {
            val reviewQuery = firestore.collection("reviews")
                .whereEqualTo("psychoId", psychoId)
                .whereEqualTo("patientId", patientId)
                .get()
                .await()

            if (reviewQuery.documents.isNotEmpty()) {
                val doc = reviewQuery.documents.first()
                ReviewModel(
                    psychoId = doc.getString("psychoId") ?: "",
                    patientId = doc.getString("patientId") ?: "",
                    tags = doc.get("tags") as? List<String> ?: emptyList(),
                    rating = doc.getDouble("rating") ?: 0.0,
                    timestamp = doc.getLong("timestamp") ?: 0L,
                    id = doc.id // Asigna el ID del documento
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

}
