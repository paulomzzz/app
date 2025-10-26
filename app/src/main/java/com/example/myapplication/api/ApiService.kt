package com.example.myapplication.api

import com.example.myapplication.model.Nodo
import retrofit2.http.GET

interface ApiService {

    @GET("nodos")
    suspend fun obtenerNodos(): List<Nodo>
}
