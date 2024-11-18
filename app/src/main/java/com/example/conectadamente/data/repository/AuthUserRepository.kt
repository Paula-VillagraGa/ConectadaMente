package com.example.conectadamente.data.repository

import com.example.conectadamente.data.model.PatientModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.conectadamente.utils.validations.isValidEmail
import javax.inject.Inject

class AuthUserRepository @Inject constructor() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Nueva función para registrar un paciente
    fun registerPatientInFirebase(
        patient: PatientModel, // Pasamos el objeto PatientModel
        password: String, // Contraseña
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Validaciones
        if (!isValidEmail(patient.email)) {
            onError("El correo electrónico no tiene un formato válido")
            return
        }

        if (password.length < 8) {
            onError("La contraseña debe tener al menos 8 caracteres")
            return
        }

        // Registro del usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(patient.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid ?: return@addOnCompleteListener onError("UID no encontrado")

                    // Ahora guardamos los datos del paciente en Firestore
                    db.collection("patients").document(uid)
                        .set(patient) // Guardamos el modelo de paciente con el UID como identificador
                        .addOnSuccessListener {
                            onSuccess("Paciente registrado correctamente")
                        }
                        .addOnFailureListener { e ->
                            onError("Error al guardar datos: ${e.message}")
                        }
                } else {
                    onError("Error al registrar paciente: ${task.exception?.message}")
                }
            }
    }}
