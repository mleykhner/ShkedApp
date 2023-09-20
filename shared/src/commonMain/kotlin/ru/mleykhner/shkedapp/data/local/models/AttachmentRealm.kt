package ru.mleykhner.shkedapp.data.local.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey

class AttachmentRealm: RealmObject {
    @PrimaryKey
    var id: String = RealmUUID.random().toString()
    @Index
    var fileName: String = ""
    var extension: String = ""
    var previewURL: String = ""
    var fileURL: String = ""
    var filePath: String? = null
    var sizeBytes: Long = 0
}