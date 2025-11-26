package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.entity.LoginUserEntity

@Dao
interface LoginUserDao {

    @Query("SELECT * FROM login_user LIMIT 1")
    suspend fun getSavedUser(): LoginUserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: LoginUserEntity)

    @Query("DELETE FROM login_user")
    suspend fun deleteAll()
}
