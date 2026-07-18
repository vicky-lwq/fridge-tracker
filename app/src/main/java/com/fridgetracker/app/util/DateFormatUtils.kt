package com.fridgetracker.app.util

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/**
 * Colloquial Chinese date formatting (e.g. "7月20日（周一）") instead of ISO format,
 * per the UI design spec's differentiation goal.
 */
object DateFormatUtils {
    private val locale = Locale.CHINA

    /** e.g. "7月20日（周一）", used for the expiry date field. */
    fun formatExpiryDisplay(date: LocalDate): String {
        val weekday = date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
        return "${date.monthValue}月${date.dayOfMonth}日（${weekday}）"
    }

    /** e.g. "2026年7月16日", used for the read-only "录入于" line. */
    fun formatAddedDisplay(date: LocalDate): String =
        "${date.year}年${date.monthValue}月${date.dayOfMonth}日"
}
