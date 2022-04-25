package com.devdtoo.learnandroid.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.devdtoo.learnandroid.MainActivity
import com.devdtoo.learnandroid.R
import com.devdtoo.learnandroid.notification.MyNotificationChannel

class ServiceHandler: Service() {
    companion object {
        const val SERVICE_ID = 1
    }
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, MyNotificationChannel.CHANNEL_ID)
            .setContentTitle("Service Alive")
            .setContentText("LearnAndroid's Service is alive as long as this notification is not closed")
            .setSmallIcon(R.drawable.ic_service)
            .setContentIntent(pendingIntent).build()
        startForeground(SERVICE_ID, notification)
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}