package com.example.passwords.domain.use_cases

import com.example.passwords.domain.repository.PasswordRepository
import javax.inject.Inject

class ExportPasswordsUseCase @Inject constructor(
    private val passwordRepository: PasswordRepository
) {
    suspend operator fun invoke(folderId: Long? = null): List<String> {
        return if (folderId == null) {
            // Export only generated passwords (without folder)
            passwordRepository.getGeneratedPasswords().map { it.value }
        } else {
            // Export all passwords from specific folder
            passwordRepository.getPasswordsByFolder(folderId).map { it.value }
        }
    }
}