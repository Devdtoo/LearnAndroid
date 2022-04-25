package com.devdtoo.learnandroid.broadcast

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.devdtoo.learnandroid.MainActivity

class BroadcastReceiverHandler : BroadcastReceiver(){
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("BroadcastReceiver", "Broadcast Received")
    }

}