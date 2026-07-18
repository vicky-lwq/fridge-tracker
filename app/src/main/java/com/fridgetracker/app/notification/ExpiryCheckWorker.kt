package com.fridgetracker.app.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fridgetracker.app.MainActivity
import com.fridgetracker.app.R
import com.fridgetracker.app.data.FridgeDatabase
import com.fridgetracker.app.repository.FoodRepository

/**
 * Daily check for items with remaining days <= 3 (including already expired).
 * Sends one merged notification; sends nothing when today's count is 0.
 */
class ExpiryCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val repository = FoodRepository(FridgeDatabase.getInstance(applicationContext).foodDao())
        val itemsNeedingAttention = repository.getItemsNeedingAttention()

        if (itemsNeedingAttention.isEmpty()) {
            return Result.success()
        }

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Can't request a runtime permission from a background worker; skip silently.
            // Permission is requested from MainActivity on first app launch instead.
            return Result.success()
        }

        showNotification(itemsNeedingAttention.map { it.name })
        return Result.success()
    }

    private fun showNotification(names: List<String>) {
        val count = names.size
        val title = "冰箱里有${count}件食物需要注意"
        val content = if (count <= 3) {
            names.joinToString("、") + "即将过期或已过期"
        } else {
            names.take(3).joinToString("、") + "等${count}件即将过期或已过期"
        }

        val contentIntent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "expiry_reminder"
        private const val NOTIFICATION_ID = 1001
    }
}
