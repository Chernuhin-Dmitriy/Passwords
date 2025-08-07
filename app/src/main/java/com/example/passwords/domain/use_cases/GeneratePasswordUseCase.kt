package com.example.passwords.domain.use_cases

import com.example.passwords.domain.entity.CharacterSetConfig
import com.example.passwords.domain.repository.PasswordRepository
import javax.inject.Inject
import kotlin.math.log2
import kotlin.math.pow
import kotlin.random.Random

class GeneratePasswordUseCase {
    operator fun invoke(config: CharacterSetConfig): GeneratedPassword {
        val charset = buildCharacterSet(config)
        val password = generatePassword(charset, config.length)
        val entropy = calculateEntropy(charset.length, config.length)

        return GeneratedPassword(
            password = password,
            entropy = entropy,
            characterSet = getCharacterSetDescription(config)
        )
    }

    private fun buildCharacterSet(config: CharacterSetConfig): String {
        val charset = StringBuilder()
        if (config.includeUppercase) charset.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        if (config.includeLowercase) charset.append("abcdefghijklmnopqrstuvwxyz")
        if (config.includeNumbers) charset.append("0123456789")
        if (config.includeSpecialChars) charset.append("!@#$%^&*()_+-=[]{}|;:,.<>?")
        return charset.toString()
    }

    private fun generatePassword(charset: String, length: Int): String {
        return (1..length)
            .map { charset[Random.nextInt(charset.length)] }
            .joinToString("")
    }

    private fun calculateEntropy(charsetSize: Int, length: Int): Double {
        return length * log2(charsetSize.toDouble())
    }

    private fun getCharacterSetDescription(config: CharacterSetConfig): String {
        val parts = mutableListOf<String>()
        if (config.includeUppercase) parts.add("Uppercase")
        if (config.includeLowercase) parts.add("Lowercase")
        if (config.includeNumbers) parts.add("Numbers")
        if (config.includeSpecialChars) parts.add("Special chars")
        return parts.joinToString(", ")
    }
}

data class GeneratedPassword(
    val password: String,
    val entropy: Double,
    val characterSet: String
)