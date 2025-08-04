package com.example.passwords.data.file

import android.content.ContentResolver
import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

@Singleton
class FileHandler @Inject constructor(
    private val contentResolver: ContentResolver
) {
    suspend fun readPasswordsFromFile(uri: Uri): List<String> {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    reader.readLines().filter { it.isNotBlank() }
                }
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun writePasswordsToFile(uri: Uri, passwords: List<String>): Boolean {
        return try {
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    passwords.forEach { password ->
                        writer.write("$password\n")
                    }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}