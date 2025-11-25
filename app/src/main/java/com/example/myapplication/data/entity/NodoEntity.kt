package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nodos")
data class NodoEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val id: Int?,    // <-- aquí asegúrate que sea Int?
    val nombre: String,
    val localizacion: String,
    val temperatura: Double,
    val nivelAgua: Double,
    val nivelHumedad: Double,
    val operativo: Boolean
)
