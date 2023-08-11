package ru.mleykhner.shkedapp.utils

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format
import ru.mleykhner.shared_resources.SharedRes
import ru.mleykhner.shkedapp.data.LessonType

actual class Strings {
    actual fun get(id: StringResource, args: List<Any>): String {
        return if(args.isEmpty()) {
            StringDesc.Resource(id).localized()
        } else {
            id.format(args.toTypedArray()).localized()
        }
    }
}

fun LessonType.toLocalizedString(): String {
    val id = when (this) {
        LessonType.LECTURE -> SharedRes.strings.lecture
        LessonType.PRACTICAL -> SharedRes.strings.practical
        LessonType.LABORATORY -> SharedRes.strings.laboratory
        LessonType.TEST -> SharedRes.strings.test
        LessonType.EXAM -> SharedRes.strings.exam
    }
    return Strings().get(id, emptyList())
}

