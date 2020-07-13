package com.example.sesionroom.model

import androidx.room.*

//Indica que se va a insertar un dato a la base de datos deudor
@Dao
interface DeudorDAO {
    @Insert
    fun crearDeudor(deudor: Deudor)

    @Query("SELECT * FROM `tabla_deudor ` WHERE nombre LIKE:nombre")

    fun buscarDeudor(nombre: String) : Deudor

    @Update
    fun actualizarDeudor(deudor: Deudor)

    @Delete
    fun borrarDeudor(deudor: Deudor)
}