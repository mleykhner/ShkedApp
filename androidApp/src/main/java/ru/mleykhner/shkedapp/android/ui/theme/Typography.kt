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

val golosFontFamily = FontFamily(
    Font(SharedRes.fonts.Golos.regular.fontResourceId, FontWeight.Normal),
    Font(SharedRes.fonts.Golos.medium.fontResourceId, FontWeight.Medium),
    Font(SharedRes.fonts.Golos.semiBold.fontResourceId, FontWeight.SemiBold),
    Font(SharedRes.fonts.Golos.bold.fontResourceId, FontWeight.Bold),
    Font(SharedRes.fonts.Golos.black.fontResourceId, FontWeight.Black)
)

val labelMediumStyle = TextStyle(
    fontFamily = unboundedFontFamily,
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium
)

val titleMediumStyle = TextStyle(
    fontFamily = unboundedFontFamily,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold
)

val titleSmallStyle = TextStyle(
    fontFamily = unboundedFontFamily,
    fontSize = 10.sp,
    fontWeight = FontWeight.Bold
)

val weekdaysStyle = TextStyle(
    fontFamily = unboundedFontFamily,
    fontSize = 12.sp,
    fontWeight = FontWeight.SemiBold
)

val labelSmallStyle = TextStyle(
    fontFamily = golosFontFamily,
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium
)

val bodyMediumStyle = TextStyle(
    fontFamily = golosFontFamily,
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal
)

val bodyLargeStyle = TextStyle(
    fontFamily = golosFontFamily,
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal
)