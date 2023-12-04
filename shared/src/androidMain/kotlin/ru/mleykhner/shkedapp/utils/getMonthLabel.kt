package ru.mleykhner.shkedapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.Month
import java.text.DateFormatSymbols

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
actual fun getMonthLabel(month: Month): String {
    val format = DateFormatSymbols.getInstance()
    return format.months[month.ordinal]
}