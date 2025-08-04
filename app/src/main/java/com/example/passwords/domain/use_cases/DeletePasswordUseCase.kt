package com.example.passwords.domain.use_cases

import com.example.passwords.domain.entity.Password
import com.example.passwords.domain.repository.PasswordRepository

class DeletePasswordUseCase(
    private val passwordRepository: PasswordRepository
) {
    suspend operator fun invoke(password: Password) {
        passwordRepository.deletePassword(password)
    }
}