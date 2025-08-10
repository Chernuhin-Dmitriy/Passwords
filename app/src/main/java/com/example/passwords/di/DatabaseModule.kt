package com.example.passwords.di

import android.content.Context
import androidx.room.Room
import com.example.passwords.data.local.database.AppDatabase
import com.example.passwords.data.local.dao.FolderDao
import com.example.passwords.data.local.dao.PasswordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "password_database"
        ).build()
    }

    @Provides
    fun providePasswordDao(database: AppDatabase): PasswordDao {
        return database.passwordDao()
    }

    @Provides
    fun provideFolderDao(database: AppDatabase): FolderDao {
        return database.folderDao()
    }
}