package com.example.passwords.domain.repository

import com.example.passwords.domain.entity.Password
import kotlinx.coroutines.flow.Flow

interface PasswordRepository {
    fun getAllPasswords(): Flow<List<Password>>
    suspend fun getPasswordsByFolder(folderId: Long): List<Password>
    suspend fun getGeneratedPasswords(): List<Password>
    suspend fun insertPassword(password: Password)
    suspend fun deletePassword(password: Password)
}