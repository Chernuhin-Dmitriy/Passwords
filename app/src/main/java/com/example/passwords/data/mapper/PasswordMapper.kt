package com.example.passwords.data.mapper

import com.example.passwords.data.local.entity.PasswordEntity
import com.example.passwords.domain.entity.Password

fun PasswordEntity.toDomain(): Password {
    return Password(
        id = id,
        value = value,
        entropy = entropy,
        characterSet = characterSet,
        folderId = folderId,
        createdAt = createdAt
    )
}

fun Password.toEntity(): PasswordEntity {
    return PasswordEntity(
        id = id,
        value = value,
        entropy = entropy,
        characterSet = characterSet,
        folderId = folderId,
        createdAt = createdAt
    )
}