package com.devdtoo.learnandroid.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*

class LocalBoundService : Service(){

    private val binder = LocalBinder()
    // Random number generator
    private val mGenerator = Random()
    val randomNumber: Int
        get() = mGenerator.nextInt(100)


    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): LocalBoundService = this@LocalBoundService
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d("LocalBoundService", "onBind triggered")
        return binder
    }

}