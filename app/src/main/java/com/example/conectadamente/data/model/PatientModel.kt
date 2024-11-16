package com.example.conectadamente.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient")
data class PatientModel(
    @PrimaryKey val rut: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String
)
