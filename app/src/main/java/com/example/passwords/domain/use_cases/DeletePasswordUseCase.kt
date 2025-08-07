package com.example.passwords.domain.use_cases

import com.example.passwords.domain.entity.Password
import com.example.passwords.domain.repository.PasswordRepository
import javax.inject.Inject

class DeletePasswordUseCase @Inject constructor(
    private val passwordRepository: PasswordRepository
) {
    suspend operator fun invoke(password: Password) {
        passwordRepository.deletePassword(password)
    }
}