package com.devdtoo.learnandroid.services


import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.devdtoo.learnandroid.MainActivity
import com.devdtoo.learnandroid.R
import com.devdtoo.learnandroid.utils.Constants
import kotlinx.coroutines.*

@RequiresApi(Build.VERSION_CODES.O)
class MusicPlayerService : Service() {
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)
    private val iBinder = ServiceBinder()
    private val TAG = "MusicPlayerService"

    companion object {
        private val _musicState = MutableLiveData<Boolean>()
        val musicState get() = _musicState
    }


    private lateinit var player: MediaPlayer

    inner class ServiceBinder : Binder() {
        fun getService() = this@MusicPlayerService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate() Triggered")
        player = MediaPlayer.create(this, R.raw.music_sample)
        player.setOnCompletionListener {
            _musicState.postValue(true)
            stopForeground(true)
            Log.d(TAG, "stopForeground() by Music End Triggered")
            stopSelf()
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand() Triggered")
        coroutineScope.launch {
            Log.d(TAG, "CoroutineScope Started")
            when (intent?.action) {
                Constants.MUSIC_SERVICE_ACTION_PLAY -> {
                    play()
                }
                Constants.MUSIC_SERVICE_ACTION_PAUSE -> {
                    pause()
                }
                Constants.MUSIC_SERVICE_ACTION_STOP -> {
                    Log.d(TAG, "stopForeground() by btn Triggered")
                    stopForeground(true)
                    stopSelf()
                }
                Constants.MUSIC_SERVICE_ACTION_START -> {
                    Log.d(TAG, "startForeground() Triggered")
                    startForeground(Constants.START_FOREGROUND_ID, showNotification())
                }
            }
        }
        return START_NOT_STICKY
    }


    override fun onBind(p0: Intent?): IBinder? {
        Log.d(TAG, "onBind() Triggered")
        return iBinder
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "onRebind() Triggered")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind() Triggered")
        return true
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy() Triggered")
        player.release()
        coroutineScope.cancel()
        super.onDestroy()
    }

    //handling music
    fun isPlaying() = player.isPlaying
    fun play() = player.start()
    fun pause() = player.pause()

    //Notification

    private fun showNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            Constants.PENDING_INTENT_ACTIVITY_RC,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        //Setting up Intent Actions for notification button controls
        val playIntent = Intent(this, MusicPlayerService::class.java).apply {
            action = Constants.MUSIC_SERVICE_ACTION_PLAY
        }
        val pauseIntent = Intent(this, MusicPlayerService::class.java).apply {
            action = Constants.MUSIC_SERVICE_ACTION_PAUSE
        }
        val stopIntent = Intent(this, MusicPlayerService::class.java).apply {
            action = Constants.MUSIC_SERVICE_ACTION_STOP
        }

        val playPendingIntent = PendingIntent.getForegroundService(
            this,
            Constants.PENDING_INTENT_SERVICE_PLAY_RC,
            playIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val pausePendingIntent = PendingIntent.getForegroundService(
            this,
            Constants.PENDING_INTENT_SERVICE_PAUSE_RC,
            pauseIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val stopPendingIntent = PendingIntent.getForegroundService(
            this,
            Constants.PENDING_INTENT_SERVICE_STOP_RC,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        // Setting up Notification
        val chan = NotificationChannel(
            Constants.MUSIC_SERVICE_CHANNEL_ID,
            "My Foreground Service",
            NotificationManager.IMPORTANCE_LOW
        )

        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)

        val notificationBuilder = NotificationCompat.Builder(
            this, Constants.MUSIC_SERVICE_CHANNEL_ID
        )
        val notification: Notification = notificationBuilder
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Music Player")
            .setContentText("Song is playing")
            .setPriority(NotificationManager.IMPORTANCE_LOW)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setChannelId(Constants.MUSIC_SERVICE_CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_play,
                    "Play",
                    playPendingIntent
                )
            )
            .addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_pause,
                    "Pause",
                    pausePendingIntent
                )
            )
            .addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_menu_close_clear_cancel,
                    "Stop",
                    stopPendingIntent
                )
            )
            .build()

        return notification

    }
}