package ru.mleykhner.shkedapp.data.local

expect class FileStorage {
    suspend fun saveFile(bytes: ByteArray, fileExtension: String): String
}