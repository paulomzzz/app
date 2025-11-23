package com.example.myapplication.api

import com.example.myapplication.model.Nodo
import com.example.myapplication.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("nodos")
    suspend fun obtenerNodos(): List<Nodo>

    @POST("usuarios/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Map<String, Any>>

    @POST("usuarios/login")
    suspend fun login(@Body usuario: Usuario): Response<Map<String, String>>
}
