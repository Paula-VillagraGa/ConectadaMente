package com.example.conectadamente.ui.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.data.repository.AuthPsychoRepository
import com.example.conectadamente.utils.constants.DataState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class psychoAuthViewModel @Inject constructor(
    private val authPsychoRepository: AuthPsychoRepository,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) : ViewModel() {

    private val _authState = MutableStateFlow<DataState<String>>(DataState.Finished)
    val authState: StateFlow<DataState<String>> = _authState.asStateFlow()

    fun registerPsycho(psycho: PsychoModel, password: String, images: List<Uri>) {
        uploadDocumentsToStorage(
            uris = images,
            onUploadSuccess = { urls ->
                val psychoWithDocuments = psycho.copy(documentUrls = urls)
                savePsychoToFirestore(psychoWithDocuments)
            },
            onUploadError = { exception ->
                Log.e("StorageError", "Error al subir documentos", exception)
            }
        )
    }

    private fun uploadDocumentsToStorage(
        uris: List<Uri>,
        onUploadSuccess: (List<String>) -> Unit,
        onUploadError: (Exception) -> Unit
    ) {
        val uploadedUrls = mutableListOf<String>()

        uris.forEach { uri ->
            val fileName = uri.lastPathSegment ?: System.currentTimeMillis().toString()
            val fileRef = storage.reference.child("psychos/documents/$fileName")

            fileRef.putFile(uri)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        uploadedUrls.add(downloadUri.toString())
                        if (uploadedUrls.size == uris.size) {
                            onUploadSuccess(uploadedUrls)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    onUploadError(exception)
                }
        }
    }

    private fun savePsychoToFirestore(psycho: PsychoModel) {
        val docRef = firestore.collection("psychos").document()
        docRef.set(psycho)
            .addOnSuccessListener {
                Log.d("Firestore", "Psicólogo registrado exitosamente")
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error al guardar en Firestore", exception)
            }
    }
}