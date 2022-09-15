package com.devdtoo.learnandroid.workmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.devdtoo.learnandroid.MainActivity
import com.devdtoo.learnandroid.R

class MyWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    companion object {
        const val CHANNEL_ID = "WORKER_CHANNEL"
        const val NOTIFICATION_ID = 1
        const val PENDING_INTENT_RC = 2
        const val CHANNEL_NAME = "Notification"
        const val CHANNEL_DESC = "Test Notification"
        const val TAG = "LogWork"
    }

    override suspend fun doWork(): Result {
        showNotification()
        Log.d(TAG, "Success function Called")
        return Result.success()
    }

    private fun showNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            PENDING_INTENT_RC,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setContentTitle("WorkManager")
            setContentText("Work in progress")
            setSmallIcon(R.mipmap.ic_launcher)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
            setPriority(Notification.PRIORITY_MAX)

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelImportance = NotificationManager.IMPORTANCE_HIGH
            // Setting up Notification

            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, channelImportance).apply {
                description = CHANNEL_DESC
            }

            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(NOTIFICATION_ID, notification.build())
            }
        }


    }


}