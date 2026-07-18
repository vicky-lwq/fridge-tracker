package com.fridgetracker.app.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/**
 * Schedules the once-a-day expiry check. PeriodicWorkRequest's minimum interval is
 * 15 minutes, so a 24h interval comfortably satisfies that; setInitialDelay aligns the
 * first run to the next occurrence of REMINDER_TIME.
 */
object NotificationScheduler {
    private const val UNIQUE_WORK_NAME = "expiry_check_daily"
    private val REMINDER_TIME: LocalTime = LocalTime.of(9, 0)

    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<ExpiryCheckWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(computeInitialDelay().toMinutes(), TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun computeInitialDelay(): Duration {
        val now = LocalDateTime.now()
        var next = now.toLocalDate().atTime(REMINDER_TIME)
        if (!next.isAfter(now)) {
            next = next.plusDays(1)
        }
        return Duration.between(now, next)
    }
}
