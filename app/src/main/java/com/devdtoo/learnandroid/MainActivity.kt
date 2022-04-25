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
import androidx.databinding.DataBindingUtil
import com.devdtoo.learnandroid.broadcast.BroadcastReceiverHandler
import com.devdtoo.learnandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var receiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBroadcastReceiver()
        val intent = intent
        val action = intent.action
        val type = intent.type
        Log.d("MainActivity", "Action : $action")
        Log.d("MainActivity", "Type: $type")
        when {
            intent?.action == Intent.ACTION_SEND -> {
                if (intent.type?.startsWith("image/") == true) {
                    Log.d("MainActivity", "Check Passed")
                    handleSendImage(intent) // Handle single image being sent
                }
            }
            else -> {
                // Handle other intents, such as being started from the home screen
                Log.d("MainActivity", "Not Found")
            }
        }


    }


    private fun handleSendImage(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            // Update UI to reflect image being shared
            binding.imageView.setImageURI(it)
        }
    }

    fun initBroadcastReceiver() {
        receiver = BroadcastReceiverHandler()
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(receiver, it)
        }
    }
    override fun onStart() {
        super.onStart()
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }


}