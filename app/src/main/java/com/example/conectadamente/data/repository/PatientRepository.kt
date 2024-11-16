package com.example.conectadamente.data.repository

import com.example.conectadamente.data.dao.PatientDao
import com.example.conectadamente.data.model.PatientModel

class PatientRepository(private val patientDao: PatientDao) {
    // Método que llama a getPatientByRut
    suspend fun getPatientByRut(rut: String): PatientModel? {
        return patientDao.getPatientByRut(rut)

    suspend fun registerPatient(patient: PatientModel) {
        patientDao.insertPatient(patient)
    }

    suspend fun getPatientByRut(rut: String): PatientModel? {
        return patientDao.getPatientByRut(rut)
    }
}}
