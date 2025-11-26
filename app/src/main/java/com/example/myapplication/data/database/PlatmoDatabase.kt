package com.example.myapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.dao.LoginUserDao
import com.example.myapplication.data.dao.NodoDao
import com.example.myapplication.data.entity.NodoEntity
import com.example.myapplication.data.entity.LoginUserEntity

@Database(
    entities = [
        NodoEntity::class,
        LoginUserEntity::class
    ],
    version = 3, // subir versión para que Room regenere la BD
    exportSchema = false
)
abstract class PlatmoDatabase : RoomDatabase() {
    abstract fun nodoDao(): NodoDao
    abstract fun loginUserDao(): LoginUserDao
}
