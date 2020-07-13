package com.example.sesionroom.model

import androidx.room.Database
import androidx.room.RoomDatabase
//Clase que no va a ser instanciada
//el database se crea una sola vez, a cada susario se le crea un DAO
//dentro del arrayof

@Database(entities = arrayOf(Deudor::class), version = 1)
abstract class DeudorDataBase: RoomDatabase() {

    abstract  fun DeudorDAO() : DeudorDAO

}