package com.example.sesionroom

import android.app.Application
import androidx.room.Room
import com.example.sesionroom.model.local.DeudorDataBase

class SesionROOM: Application() {

    companion object{
        lateinit var database: DeudorDataBase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            this,
            DeudorDataBase::class.java,
            "misdeudores_db"
        ).allowMainThreadQueries().build()
    }
}