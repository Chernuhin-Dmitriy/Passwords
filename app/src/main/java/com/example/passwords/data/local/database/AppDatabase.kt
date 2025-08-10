package com.example.passwords.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.passwords.data.local.dao.FolderDao
import com.example.passwords.data.local.dao.PasswordDao
import com.example.passwords.data.local.entity.FolderEntity
import com.example.passwords.data.local.entity.PasswordEntity

@Database(
    entities = [PasswordEntity::class, FolderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
    abstract fun folderDao(): FolderDao
}