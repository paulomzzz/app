package com.example.myapplication.data.repository

import com.example.myapplication.api.ApiService
import com.example.myapplication.data.dao.NodoDao
import com.example.myapplication.data.entity.NodoEntity
import com.example.myapplication.model.Nodo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NodoRepository(
    private val api: ApiService,
    private val dao: NodoDao
) {

    val nodos: Flow<List<Nodo>> = dao.obtenerNodos().map { entidades ->
        entidades.map { e ->
            Nodo(
                id = e.id?.toLong() ?: 0L,
                nombre = e.nombre,
                localizacion = e.localizacion,
                temperatura = e.temperatura,
                nivelAgua = e.nivelAgua,
                nivelHumedad = e.nivelHumedad,
                operativo = e.operativo
            )
        }
    }

    suspend fun fetchAndSaveNodos() {
        val nodosRemotos: List<Nodo> = api.obtenerNodos()

        val entidades = nodosRemotos.map { nodo ->
            NodoEntity(
                localId = 0,
                id = nodo.id?.toInt() ?: 0,  // <-- CORREGIDO
                nombre = nodo.nombre,
                localizacion = nodo.localizacion,
                temperatura = nodo.temperatura,
                nivelAgua = nodo.nivelAgua,
                nivelHumedad = nodo.nivelHumedad,
                operativo = nodo.operativo
            )
        }

        dao.borrarNodos()
        dao.insertarNodos(entidades)
    }
}
