package ru.mleykhner.shkedapp.data.local

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey

class AttachmentRealm: RealmObject {
    @PrimaryKey
    val id: String = RealmUUID.random().toString()
    @Index
    val fileName: String = ""
    val extension: String = ""
    val previewURL: String = ""
    val fileURL: String = ""
    val filePath: String? = null
    val sizeBytes: Long = 0
}