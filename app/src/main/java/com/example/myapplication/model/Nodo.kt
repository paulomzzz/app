package com.example.myapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class Nodo(
    val id: Long? = null,
    val nombre: String,
    val localizacion: String,
    val temperatura: Double,
    val nivelAgua: Double,
    val nivelHumedad: Double,
    val operativo: Boolean
)
