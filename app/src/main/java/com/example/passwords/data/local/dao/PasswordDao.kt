package com.example.passwords.data.local.dao

import androidx.room.*
import com.example.passwords.data.local.entity.PasswordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @Query("SELECT * FROM passwords ORDER BY createdAt DESC")
    fun getAllPasswords(): Flow<List<PasswordEntity>>

    @Query("SELECT * FROM passwords WHERE folderId = :folderId")
    suspend fun getPasswordsByFolder(folderId: Long): List<PasswordEntity>

    @Query("SELECT * FROM passwords WHERE folderId IS NULL")
    suspend fun getGeneratedPasswords(): List<PasswordEntity>

    @Insert
    suspend fun insertPassword(password: PasswordEntity)

    @Delete
    suspend fun deletePassword(password: PasswordEntity)
}