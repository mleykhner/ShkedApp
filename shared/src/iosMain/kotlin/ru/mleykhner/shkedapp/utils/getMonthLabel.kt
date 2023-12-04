package ru.mleykhner.shkedapp.utils

import kotlinx.datetime.Month
import platform.Foundation.NSCalendar


actual fun getMonthLabel(month: Month): String {
    val calendar = NSCalendar.currentCalendar
    val symbols = calendar.shortMonthSymbols
    return symbols[month.ordinal] as? String ?: ""
}