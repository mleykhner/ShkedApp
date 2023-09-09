package ru.mleykhner.shkedapp.data.remote

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

actual class FileStorage(
    private val context: Context
) {
    actual suspend fun saveFile(bytes: ByteArray, fileExtension: String): String {
        return withContext(Dispatchers.IO) {
            val fileName = UUID.randomUUID().toString() + fileExtension
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(bytes)
            }
            fileName
        }
    }
}