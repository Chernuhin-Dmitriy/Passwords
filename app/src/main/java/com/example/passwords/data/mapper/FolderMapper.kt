package com.example.passwords.data.mapper

import com.example.passwords.data.local.entity.FolderEntity
import com.example.passwords.domain.entity.Folder

fun FolderEntity.toDomain(): Folder {
    return Folder(
        id = id,
        name = name,
        createdAt = createdAt
    )
}

fun Folder.toEntity(): FolderEntity {
    return FolderEntity(
        id = id,
        name = name,
        createdAt = createdAt
    )
}