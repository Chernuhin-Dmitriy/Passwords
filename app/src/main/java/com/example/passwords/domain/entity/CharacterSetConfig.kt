package com.example.passwords.domain.entity

data class CharacterSetConfig(
    val includeUppercase: Boolean = true,
    val includeLowercase: Boolean = true,
    val includeNumbers: Boolean = true,
    val includeSpecialChars: Boolean = false,
    val length: Int = 12
)
