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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PsychoAuthViewModel @Inject constructor(
    private val authPsychoRepository: AuthPsychoRepository,
    private val reviewRepository: ReviewRepository,
    val firestore: FirebaseFirestore
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

    fun updateProfile(
        phone: String,
        description: String,
        experience: String,
        specializations: List<String>,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            // Obtener el UID del usuario actualmente autenticado
            val psychoId = FirebaseAuth.getInstance().currentUser?.uid
            psychoId?.let {
                try {
                    // 1. Recuperar los datos actuales del perfil del psicólogo antes de realizar cualquier actualización
                    val currentProfileDoc = firestore.collection("psychos").document(it).get().await()

                    if (currentProfileDoc.exists()) {
                        // Convertir los datos actuales del perfil en un objeto PsychoModel
                        val currentData = currentProfileDoc.toObject(PsychoModel::class.java)

                        // 2. Crear un mapa con los nuevos datos, comparando con los datos actuales para no sobreescribir sin necesidad
                        val profileData = mutableMapOf<String, Any?>()

                        if (currentData != null) {
                            if (phone.isNotBlank() && phone != currentData.phone) profileData["phone"] = phone
                            if (description.isNotBlank() && description != currentData.descriptionPsycho) profileData["descriptionPsycho"] = description
                            if (experience.isNotBlank() && experience != currentData.experience) profileData["experience"] = experience
                            if (specializations.isNotEmpty() && specializations != currentData.specialization) profileData["specialization"] = specializations
                        }

                        // 3. Si hay cambios, actualizamos los datos en Firestore
                        if (profileData.isNotEmpty()) {
                            firestore.collection("psychos").document(it).update(profileData).await()
                        }

                        // 4. Si hay una imagen seleccionada, actualizamos la foto
                        imageUri?.let { uri ->
                            val storageRef = FirebaseStorage.getInstance().reference.child("profile_pictures/$it.jpg")
                            storageRef.putFile(uri).await()
                            val downloadUrl = storageRef.downloadUrl.await().toString()

                            // Actualizar la URL de la foto en Firestore
                            firestore.collection("psychos").document(it).update("photoUrl", downloadUrl).await()
                        }

                        // 5. Después de la actualización, volvemos a obtener los datos más recientes para el perfil
                        val updatedProfileDoc = firestore.collection("psychos").document(it).get().await()

                        if (updatedProfileDoc.exists()) {
                            val updatedProfile = updatedProfileDoc.toObject(PsychoModel::class.java)
                            updatedProfile?.let { profile ->
                                // Actualizar el estado con el perfil actualizado
                                _profileState.value = DataState.Success(profile)
                            }
                        } else {
                            _profileState.value = DataState.Error("Perfil no encontrado.")
                        }

                    } else {
                        _profileState.value = DataState.Error("Perfil no encontrado.")
                    }

                } catch (e: Exception) {
                    _profileState.value = DataState.Error("Error al actualizar el perfil: ${e.message}")
                }
            }
        }
    } // Función para cargar el perfil del psicólogo
    fun loadProfile() {
        viewModelScope.launch {
            try {
                val profile = authPsychoRepository.getCurrentPsychologistProfile()
                _profileState.value = DataState.Success(profile)
            } catch (e: Exception) {
                _profileState.value = DataState.Error(e.toString())
            }
        }
    }
}