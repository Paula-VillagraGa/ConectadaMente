package com.example.conectadamente



import android.app.Application
import androidx.room.Room
import com.example.conectadamente.data.database.AppDatabase
import com.example.conectadamente.data.repository.PatientRepository

class MainApplication : Application() {
    lateinit var repository: PatientRepository

    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
        repository = PatientRepository(db.patientDao())
    }
}
