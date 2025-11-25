package com.example.myapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.dao.NodoDao
import com.example.myapplication.data.entity.NodoEntity

@Database(
    entities = [NodoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PlatmoDatabase : RoomDatabase() {

    abstract fun nodoDao(): NodoDao
}
