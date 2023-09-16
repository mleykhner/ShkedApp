package ru.mleykhner.shkedapp.utils

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun startNapier() {
    Napier.base(DebugAntilog())
}