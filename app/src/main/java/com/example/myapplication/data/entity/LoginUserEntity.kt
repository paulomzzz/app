package com.example.myapplication.data.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "login_user")
data class LoginUserEntity(
    @PrimaryKey val id: Int = 1, // siempre 1 solo registro
    val nombre: String,
    val password: String
)
