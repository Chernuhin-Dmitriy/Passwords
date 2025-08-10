package com.example.passwords.di

import com.example.passwords.data.repository.FolderRepositoryImpl
import com.example.passwords.data.repository.PasswordRepositoryImpl
import com.example.passwords.domain.repository.FolderRepository
import com.example.passwords.domain.repository.PasswordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPasswordRepository(
        passwordRepositoryImpl: PasswordRepositoryImpl
    ): PasswordRepository

    @Binds
    @Singleton
    abstract fun bindFolderRepository(
        folderRepositoryImpl: FolderRepositoryImpl
    ): FolderRepository
}