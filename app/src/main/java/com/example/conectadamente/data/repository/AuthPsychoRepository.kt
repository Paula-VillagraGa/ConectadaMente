package com.example.conectadamente.data.repository

import android.net.Uri
import android.util.Log
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.utils.constants.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class AuthPsychoRepository @Inject constructor() {
    // Conexiones con Firebase
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    // Definir el estado del perfil
    private val _profileState = MutableStateFlow<DataState<PsychoModel>>(DataState.Idle)
    val profileState: StateFlow<DataState<PsychoModel>> = _profileState



    fun registerPsycho(
        psycho: PsychoModel,
        password: String,
        documents: List<Uri>
    ): Flow<DataState<String>> = flow {
        emit(DataState.Loading)
        try {
            if (documents.isEmpty()) {
                throw IllegalArgumentException("Debe subir al menos un documento.")
            }

            // Validar documentos
            if (documents.any { uri -> uri.lastPathSegment.isNullOrEmpty() || uri.scheme !in listOf("content", "file") }) {
                throw IllegalArgumentException("Uno o más documentos no tienen un formato válido.")
            }


            // Crear usuario en Firebase Authentication
            val authResult = try {
                auth.createUserWithEmailAndPassword(psycho.email, password).await()
            } catch (e: FirebaseAuthUserCollisionException) {
                throw Exception("El correo electrónico ya está en uso.")
            } catch (e: FirebaseAuthWeakPasswordException) {
                throw Exception("La contraseña debe tener al menos 6 caracteres.")
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                throw Exception("El correo electrónico es inválido.")
            }

            val uid = authResult.user?.uid ?: throw IllegalStateException("UID no encontrado")

            // Subir documentos a Storage usando el UID
            val documentUrls = uploadDocumentsToStorage(uid, documents)
            if (documentUrls.size != documents.size) {
                throw Exception("Se subieron ${documentUrls.size} de ${documents.size} documentos. Intente nuevamente para completar.")
            }

            // Crear modelo del psicólogo con documentos y estado de verificación
            val psychoWithDocuments = psycho.copy(
                verified = false,
                documentUrls = documentUrls,
                id = uid
            )

            // Guardar datos del psicólogo en Firestore
            db.collection("psychos").document(uid).set(psychoWithDocuments).await()

            emit(DataState.Success("Registro exitoso. Tu cuenta será verificada en un plazo de 24 horas hábiles."))
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error en registerPsycho", e)
            emit(DataState.Error(e))
        } finally {
            emit(DataState.Finished)
        }
    }  // Obtener el UID del Psycho
    fun getCurrentPsychoId(): String? {
        return auth.currentUser?.uid
    }

    private suspend fun uploadDocumentsToStorage(uid: String, documents: List<Uri>): List<String> {
        val documentUrls = mutableListOf<String>()

        coroutineScope { // Aseguramos que todo corra dentro de un coroutineScope
            documents.mapIndexed { index, uri ->
                launch {
                    try {
                        val storageRef = storage.reference
                        val documentRef = storageRef.child("certificados/$uid/document_$index")

                        // Subir archivo y obtener URL
                        documentRef.putFile(uri).await()
                        val downloadUrl = documentRef.downloadUrl.await()
                        synchronized(documentUrls) {
                            documentUrls.add(downloadUrl.toString())
                        }
                    } catch (e: Exception) {
                        Log.e("FirestoreRepository", "Error al subir archivo: ${uri.lastPathSegment}", e)
                    }
                }
            }.joinAll() // Esperamos que todas las tareas finalicen
        }

        return documentUrls
    } suspend fun getCurrentPsychologistProfile(): PsychoModel? {
        return try {
            val currentUser = auth.currentUser // Usuario autenticado
            val uid = currentUser?.uid ?: throw Exception("Usuario no autenticado")
            val doc = db.collection("psychos").document(uid).get().await()
            if (doc.exists()) {
                doc.toObject(PsychoModel::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al obtener el perfil del usuario actual", e)
            null
        }
    }

    suspend fun getPsychologists(): List<PsychoModel> {
        return try {
            val documents = db.collection("psychos").get().await()
            documents.map { document ->
                PsychoModel(
                    id = document.getString("id")?:"",
                    name = document.getString("name") ?: "",
                    /*specialty = document.getString("specialty") ?: "",*/
                    rating = document.getDouble("rating") ?: 0.0
                )
            }
        } catch (e: Exception) {
            emptyList() // Retornar una lista vacía si ocurre un error
        }
    }
    //Buscar psicólogos por nombre
    suspend fun getPsychosByName(query: String): List<PsychoModel> {
        return try {
            val documents = db.collection("psychos")
                .whereGreaterThanOrEqualTo("name", query)
                .whereLessThanOrEqualTo("name", query + "\uf8ff")
                .get()
                .await()
            documents.map { document ->

                PsychoModel(

                    id = document.getString("id")?:"",
                    name = document.getString("name") ?: "",
                    /*specialization = document.get("specialization")as? List<String> ?: emptyList(),*/
                    rating = document.getDouble("rating") ?: 0.0
                )
            }
        } catch (e: Exception) {
            emptyList() // Retornar una lista vacía si ocurre un error
        }
    }
}

