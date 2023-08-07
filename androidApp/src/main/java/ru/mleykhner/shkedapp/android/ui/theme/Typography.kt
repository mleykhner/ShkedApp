package ru.mleykhner.shkedapp.android.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.mleykhner.shared_resources.SharedRes

val unboundedFontFamily = FontFamily(
    Font(SharedRes.fonts.Unbounded.extraLight.fontResourceId, FontWeight.ExtraLight),
    Font(SharedRes.fonts.Unbounded.light.fontResourceId, FontWeight.Light),
    Font(SharedRes.fonts.Unbounded.regular.fontResourceId, FontWeight.Normal),
    Font(SharedRes.fonts.Unbounded.medium.fontResourceId, FontWeight.Medium),
    Font(SharedRes.fonts.Unbounded.extraLight.fontResourceId, FontWeight.ExtraLight),
    Font(SharedRes.fonts.Unbounded.semiBold.fontResourceId, FontWeight.SemiBold),
    Font(SharedRes.fonts.Unbounded.bold.fontResourceId, FontWeight.Bold),
    Font(SharedRes.fonts.Unbounded.extraBold.fontResourceId, FontWeight.ExtraBold),
    Font(SharedRes.fonts.Unbounded.black.fontResourceId, FontWeight.Black)
    )

val lableMediumStyle = TextStyle(
    fontFamily = unboundedFontFamily,
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium
)

val titleMediumStyle = TextStyle(
    fontFamily = unboundedFontFamily,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold
)

val weekdaysStyle = TextStyle(
    fontFamily = unboundedFontFamily,
    fontSize = 12.sp,
    fontWeight = FontWeight.SemiBold
)