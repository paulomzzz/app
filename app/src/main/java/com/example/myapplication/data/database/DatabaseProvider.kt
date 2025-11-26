package com.example.myapplication.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: PlatmoDatabase? = null

    fun getDatabase(context: Context): PlatmoDatabase {
        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                PlatmoDatabase::class.java,
                "platmo_db"
            )
                .fallbackToDestructiveMigration() // 👈 EVITA CRASHES AL CAMBIAR VERSIONES
                .build()

            INSTANCE = instance
            instance
        }
    }
}
