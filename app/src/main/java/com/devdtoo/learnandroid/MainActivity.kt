package com.devdtoo.learnandroid

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.devdtoo.learnandroid.broadcast.BroadcastReceiverHandler
import com.devdtoo.learnandroid.databinding.ActivityMainBinding
import com.devdtoo.learnandroid.services.ServiceHandler

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val launchServiceIntent = Intent(this, ServiceHandler::class.java)
        ContextCompat.startForegroundService(this, launchServiceIntent)

    }




}