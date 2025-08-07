package com.example.passwords.domain.use_cases

import com.example.passwords.domain.repository.FolderRepository
import com.example.passwords.domain.repository.PasswordRepository
import com.example.passwords.domain.entity.Folder
import com.example.passwords.domain.entity.Password
import javax.inject.Inject

class ImportPasswordsUseCase @Inject constructor(
    private val passwordRepository: PasswordRepository,
    private val folderRepository: FolderRepository
) {
    suspend operator fun invoke(fileName: String, passwords: List<String>) {
        val folder = Folder(name = fileName)
        val folderId = folderRepository.insertFolder(folder)

        passwords.forEach { passwordValue ->
            val password = Password(
                value = passwordValue,
                folderId = folderId
            )
            passwordRepository.insertPassword(password)
        }
    }
}