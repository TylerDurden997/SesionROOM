package com.example.sesionroom.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Aqui se crea el objeto deudor, los parametros que contienen
@Entity(tableName = "tabla_deudor ")
class Deudor (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")  val Id: Int,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "telefono") val telefono: String,
    @ColumnInfo(name = "canidad") val cantidad: Long
)
