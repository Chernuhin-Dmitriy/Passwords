package com.example.passwords.domain.repository

import com.example.passwords.domain.entity.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    fun getAllFolders(): Flow<List<Folder>>
    suspend fun insertFolder(folder: Folder): Long
    suspend fun deleteFolder(folder: Folder)
}