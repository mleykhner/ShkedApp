package ru.mleykhner.shkedapp.utils

import android.content.Context
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format
import ru.mleykhner.shared_resources.SharedRes
import ru.mleykhner.shkedapp.data.LessonType

actual class Strings(
    private val context: Context
) {
    actual fun get(id: StringResource, args: List<Any>): String {
        return if(args.isEmpty()) {
            StringDesc.Resource(id).toString(context)
        } else {
            id.format(args.toTypedArray()).toString(context)
        }
    }
}

fun LessonType.toLocalizedString(context: Context): String {
    val id = when (this) {
        LessonType.LECTURE -> SharedRes.strings.lecture
        LessonType.PRACTICAL -> SharedRes.strings.practical
        LessonType.LABORATORY -> SharedRes.strings.laboratory
        LessonType.TEST -> SharedRes.strings.test
        LessonType.EXAM -> SharedRes.strings.exam
    }
    return Strings(context).get(id, emptyList())
}