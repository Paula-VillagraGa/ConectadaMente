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
            if(documents.isEmpty()){
                throw IllegalArgumentException("Debe subir al menos un documento")
            }
            val documentUrls = uploadDocumentsToStorage(documents)

            // Crear usuario en Firebase Authentication
            val authResult = auth.createUserWithEmailAndPassword(psycho.email, password).await()
            val uid = authResult.user?.uid ?: throw IllegalStateException("UID no encontrado")


            // Crear modelo del psicólogo con documentos y estado de verificación
            val psychoWithDocuments = psycho.copy(
                isVerified = false,
                documentUrls = documentUrls
            )

            db.collection("psychos").document(uid).set(psychoWithDocuments).await()

            emit(DataState.Success("Registro exitoso. Tu cuenta será verificada en un plazo de 24 horas hábiles."))
        } catch (e: Exception) {
            emit(DataState.Error(e)) // Emitir error en caso de fallo
        } finally {
            emit(DataState.Finished) // Emitir estado finalizado
        }
    }

    // Subir documentos a Firebase Storage
    private suspend fun uploadDocumentsToStorage(documents: List<Uri>): List<String> {
        val documentUrls = mutableListOf<String>()
        try{
            documents.forEachIndexed { index, uri ->
                val storageRef = storage.reference
                val documentRef = storageRef.child("documents/${uri.lastPathSegment}")
                val uploadTask = documentRef.putFile(uri).await()

        }
    }catch (e: Exception){
        throw IllegalArgumentException("Error al subir los documentos", e)
        }
        return documentUrls
    }


    // Obtener el UID
    fun getCurrentPsychoId(): String? {
        return auth.currentUser?.uid
    }

    // Obtener datos del usuario actual
    fun getPsychoData(): Flow<DataState<PatientModel>> = flow {
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

}
