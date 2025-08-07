package com.example.passwords.data.repository

import com.example.passwords.data.local.dao.FolderDao
import com.example.passwords.data.mapper.toDomain
import com.example.passwords.data.mapper.toEntity
import com.example.passwords.domain.entity.Folder
import com.example.passwords.domain.repository.FolderRepository
import com.example.passwords.domain.repository.PasswordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Singleton
class FolderRepositoryImpl @Inject constructor(
    private val folderDao: FolderDao
) : FolderRepository {

    override fun getAllFolders(): Flow<List<Folder>> {
        return folderDao.getAllFolders().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertFolder(folder: Folder): Long {
        return folderDao.insertFolder(folder.toEntity())
    }

    override suspend fun deleteFolder(folder: Folder) {
        folderDao.deleteFolder(folder.toEntity())
    }
}