    package com.example.conectadamente.data.dao

    import androidx.room.Dao
    import androidx.room.Insert
    import androidx.room.Query
    import com.example.conectadamente.data.model.PatientModel

    @Dao
    interface PatientDao {
        @Insert
        suspend fun insertPatient(patient: PatientModel)

        @Query("SELECT * FROM patient WHERE email = :email LIMIT 1")
        suspend fun getPatientByEmail(email: String): PatientModel?
    }