package ru.mleykhner.shkedapp.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class AttachmentDTO(
    val id: String,
    val fileName: String,
    val extension: String,
    val previewURL: String,
    val fileURL: String,
    val sizeBytes: Long
)
