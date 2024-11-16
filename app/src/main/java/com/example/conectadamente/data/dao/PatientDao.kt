    package com.example.conectadamente.data.dao

    import androidx.room.Dao
    import androidx.room.Insert
    import androidx.room.Query
    import com.example.conectadamente.data.model.PatientModel

    @Dao
    interface PatientDao {
        @Insert
        suspend fun insertPatient(patient: PatientModel)

        // Definir el método para obtener un paciente por RUT
        @Query("SELECT * FROM patient WHERE rut = :rut LIMIT 1")
        suspend fun getPatientByRut(rut: String): PatientModel?
    }
