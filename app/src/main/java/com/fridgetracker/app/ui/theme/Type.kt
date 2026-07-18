package com.fridgetracker.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fridgetracker.app.R

// Bundled locally under res/font/ (no Downloadable Fonts API — this app is offline-only).
// Subsetted to GB2312 common Chinese characters + Latin + punctuation to keep APK size
// reasonable; see delivery notes for the small chance an unusual character isn't covered.
val NotoSansSC = FontFamily(
    Font(R.font.noto_sans_sc_regular, FontWeight.Normal),
    Font(R.font.noto_sans_sc_semibold, FontWeight.SemiBold)
)

private val base = Typography()

// Only labelSmall/Medium/Large, bodyMedium/Large, titleMedium/Large are used by this app's
// screens (see UI doc section 2.2); other slots keep M3 defaults but with NotoSansSC applied
// so any incidental use of them (e.g. Snackbar, DropdownMenu internals) still renders in the
// project's chosen typeface, per "正文/UI字体...覆盖所有中文文案、按钮、标签、输入框".
val Typography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = NotoSansSC),
    displayMedium = base.displayMedium.copy(fontFamily = NotoSansSC),
    displaySmall = base.displaySmall.copy(fontFamily = NotoSansSC),
    headlineLarge = base.headlineLarge.copy(fontFamily = NotoSansSC),
    headlineMedium = base.headlineMedium.copy(fontFamily = NotoSansSC),
    headlineSmall = base.headlineSmall.copy(fontFamily = NotoSansSC),
    titleLarge = TextStyle(
        fontFamily = NotoSansSC,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = NotoSansSC,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleSmall = base.titleSmall.copy(fontFamily = NotoSansSC),
    bodyLarge = TextStyle(
        fontFamily = NotoSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = NotoSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = base.bodySmall.copy(fontFamily = NotoSansSC),
    labelLarge = TextStyle(
        fontFamily = NotoSansSC,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = NotoSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = NotoSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp
    )
)
