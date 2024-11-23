package com.example.conectadamente.data.repository

import android.net.Uri
import com.example.conectadamente.data.model.PatientModel
import com.example.conectadamente.data.model.PsychoModel
import com.example.conectadamente.utils.constants.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject



class AuthPsychoRepository @Inject constructor() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance() // Instancia de Storage

    fun registerPsycho(
        psycho: PsychoModel,
        password: String,
        documents: List<Uri>
    ): Flow<DataState<String>> = flow {
        emit(DataState.Loading) // Emitir estado de carga
        try {
            // Crear usuario en Firebase Authentication
            val authResult = auth.createUserWithEmailAndPassword(psycho.email, password).await()
            val uid = authResult.user?.uid ?: throw IllegalStateException("UID no encontrado")

            // Subir documentos a Storage y obtener las URLs
            val documentUrls = mutableListOf<String>()
            for ((index, document) in documents.withIndex()) {
                val storageRef = storage.reference.child("psychos/$uid/document_$index.jpg")
                val uploadTask = storageRef.putFile(document).await()
                val url = storageRef.downloadUrl.await().toString()
                documentUrls.add(url)
            }

            // Crear modelo del psicólogo con documentos y estado de verificación
            val psychoWithDocuments = psycho.copy(
                isVerified = false,
                documentUrls = documentUrls
            )

            // Guardar datos en Firestore
            db.collection("psychos").document(uid).set(psychoWithDocuments).await()

            emit(DataState.Success("Registro exitoso. Tu cuenta será verificada en un plazo de 24 horas hábiles."))
        } catch (e: Exception) {
            emit(DataState.Error(e)) // Emitir error en caso de fallo
        } finally {
            emit(DataState.Finished) // Emitir estado finalizado
        }
    }


    // Obtener el UID
    fun getCurrentPsychoId(): String? {
        return auth.currentUser?.uid
    }

    // Obtener datos del usuario actual
    fun getUserData(): Flow<DataState<PatientModel>> = flow {
        emit(DataState.Loading) // Estado
        try {
            val userId = getCurrentPsychoId() ?: throw IllegalStateException("Psicólogo no autenticado")
            val documentSnapshot = db.collection("psychos").document(userId).get().await()
            val patient = documentSnapshot.toObject(PatientModel::class.java)
                ?: throw IllegalStateException("Datos del psicólogo no encontrados")

            emit(DataState.Success(patient)) // Emitir datos del paciente
        } catch (e: Exception) {
            emit(DataState.Error(e)) // Emitir error en caso de fallo
        } finally {
            emit(DataState.Finished) // Emitir estado finalizado
        }
    }



    // Iniciar sesión con Flow
    fun login(email: String, password: String): Flow<DataState<String>> = flow {
        emit(DataState.Loading) // Emitir estado de carga
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            emit(DataState.Success("Inicio de sesión exitoso")) // Emitir éxito
        } catch (e: Exception) {
            emit(DataState.Error(e)) // Emitir error en caso de fallo
        } finally {
            emit(DataState.Finished) // Emitir estado finalizado
        }
    }

    // Cerrar sesión del usuario actual
    fun logout(): Flow<DataState<String>> = flow {
        emit(DataState.Loading) // Emitir estado de carga
        try {
            auth.signOut()
            emit(DataState.Success("Cierre de sesión exitoso")) // Emitir éxito
        } catch (e: Exception) {
            emit(DataState.Error(e)) // Emitir error en caso de fallo
        } finally {
            emit(DataState.Finished) // Emitir estado finalizado
        }
    }
}
