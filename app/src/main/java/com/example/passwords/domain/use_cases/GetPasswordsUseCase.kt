package com.example.passwords.domain.use_cases

import com.example.passwords.domain.entity.Password
import com.example.passwords.domain.repository.PasswordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPasswordsUseCase @Inject constructor(
    private val passwordRepository: PasswordRepository
) {
    operator fun invoke(): Flow<List<Password>> {
        return passwordRepository.getAllPasswords()
    }
}