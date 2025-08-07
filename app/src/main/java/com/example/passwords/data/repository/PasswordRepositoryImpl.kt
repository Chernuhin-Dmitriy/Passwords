package com.example.passwords.data.repository

import com.example.passwords.data.local.dao.PasswordDao
import com.example.passwords.data.mapper.toDomain
import com.example.passwords.data.mapper.toEntity
import com.example.passwords.domain.entity.Password
import com.example.passwords.domain.repository.FolderRepository
import com.example.passwords.domain.repository.PasswordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordRepositoryImpl @Inject constructor(
    private val passwordDao: PasswordDao
) : PasswordRepository {

    override fun getAllPasswords(): Flow<List<Password>> {
        return passwordDao.getAllPasswords().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getPasswordsByFolder(folderId: Long): List<Password> {
        return passwordDao.getPasswordsByFolder(folderId).map { it.toDomain() }
    }

    override suspend fun getGeneratedPasswords(): List<Password> {
        return passwordDao.getGeneratedPasswords().map { it.toDomain() }
    }

    override suspend fun insertPassword(password: Password) {
        passwordDao.insertPassword(password.toEntity())
    }

    override suspend fun deletePassword(password: Password) {
        passwordDao.deletePassword(password.toEntity())
    }
}