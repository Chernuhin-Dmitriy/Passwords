package com.example.passwords.domain.entity

data class Folder(
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)