package com.example.passwords.domain.entity

data class Password(
    val id: Long = 0,
    val value: String,
    val entropy: Double? = null,
    val characterSet: String? = null,
    val folderId: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)