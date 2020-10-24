package io.goooler.demoapp.base.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

abstract class BaseService : Service() {

    /**
     * 必须不为 0
     */
    protected open val channelId: Int = 1

    protected open val contentTitle: String = ""

    protected open val channelName: String get() = getString(applicationInfo.labelRes)

    protected open val smallIcon: Int get() = applicationInfo.icon

    protected open val notification: Notification get() = createNormalNotification(contentTitle)

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId.toString(),
                channelName,
                NotificationManager.IMPORTANCE_MIN
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)
                ?.createNotificationChannel(channel)
            startForeground(channelId, notification)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    @Suppress("SameParameterValue")
    protected fun createNormalNotification(content: String?): Notification {
        val builder = NotificationCompat.Builder(this, channelId.toString())
        return if (content.isNullOrEmpty()) {
            builder.setNotificationSilent().build()
        } else {
            builder.setContentTitle(applicationInfo.name)
                .setContentTitle(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(smallIcon)
                .build()
        }
    }
}