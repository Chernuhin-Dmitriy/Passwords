package com.example.passwords.di

import com.example.passwords.domain.repository.FolderRepository
import com.example.passwords.domain.repository.PasswordRepository
import com.example.passwords.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGeneratePasswordUseCase(): GeneratePasswordUseCase {
        return GeneratePasswordUseCase()
    }

    @Provides
    fun provideSavePasswordUseCase(
        passwordRepository: PasswordRepository
    ): SavePasswordUseCase {
        return SavePasswordUseCase(passwordRepository)
    }

    @Provides
    fun provideDeletePasswordUseCase(
        passwordRepository: PasswordRepository
    ): DeletePasswordUseCase {
        return DeletePasswordUseCase(passwordRepository)
    }

    @Provides
    fun provideGetPasswordsUseCase(
        passwordRepository: PasswordRepository
    ): GetPasswordsUseCase {
        return GetPasswordsUseCase(passwordRepository)
    }

    @Provides
    fun provideExportPasswordsUseCase(
        passwordRepository: PasswordRepository
    ): ExportPasswordsUseCase {
        return ExportPasswordsUseCase(passwordRepository)
    }

    @Provides
    fun provideImportPasswordsUseCase(
        passwordRepository: PasswordRepository,
        folderRepository: FolderRepository
    ): ImportPasswordsUseCase {
        return ImportPasswordsUseCase(passwordRepository, folderRepository)
    }
}