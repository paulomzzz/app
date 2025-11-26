package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow
import com.example.myapplication.data.entity.NodoEntity


@Dao
interface NodoDao {

    // Inserta una lista de nodos. Si hay conflicto (mismo primary key), los reemplaza.

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNodos(nodos: List<NodoEntity>)

    // Devuelve todos los nodos como Flow -> la UI puede observar y recibir actualizaciones
    // cada vez que cambie la tabla.
    @Query("SELECT * FROM nodos ORDER BY localId ASC")
    fun obtenerNodos(): Flow<List<NodoEntity>>

    // Borrar todos los nodos (útil para limpieza / sincronización completa)
    @Query("DELETE FROM nodos")
    suspend fun borrarNodos()

    // Métodos opcionales para operaciones individuales:
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNodo(nodo: NodoEntity)

    @Update
    suspend fun actualizarNodo(nodo: NodoEntity)

    @Delete
    suspend fun eliminarNodo(nodo: NodoEntity)
}
