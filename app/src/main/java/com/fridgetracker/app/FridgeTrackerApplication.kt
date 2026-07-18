package com.fridgetracker.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.fridgetracker.app.data.FridgeDatabase
import com.fridgetracker.app.notification.ExpiryCheckWorker
import com.fridgetracker.app.notification.NotificationScheduler
import com.fridgetracker.app.repository.FoodRepository

class FridgeTrackerApplication : Application() {

    /** Manual service locator (no DI framework per project scope). */
    val repository: FoodRepository by lazy {
        FoodRepository(FridgeDatabase.getInstance(this).foodDao())
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        NotificationScheduler.schedule(this)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ExpiryCheckWorker.CHANNEL_ID,
                "过期提醒",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "每日汇总即将过期和已过期的食物"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
