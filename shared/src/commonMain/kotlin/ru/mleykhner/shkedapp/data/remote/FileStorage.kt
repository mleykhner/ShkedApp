package ru.mleykhner.shkedapp.data.remote

expect class FileStorage {
    suspend fun saveFile(bytes: ByteArray, fileExtension: String): String
}