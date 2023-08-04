package ru.mleykhner.shkedapp.data.models

data class AttachmentViewData(
    val id: String,
    val fileName: String,
    val extension: String,
    val previewURL: String,
    val fileURL: String,
    val filePath: String?,
    val sizeBytes: Long
)
