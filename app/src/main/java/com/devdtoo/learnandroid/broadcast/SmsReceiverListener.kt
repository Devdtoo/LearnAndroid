package com.devdtoo.learnandroid.broadcast

import android.content.Intent

interface SmsReceiverListener {
    fun onSmsBroadcastSuccess(otpintent : Intent)
    fun onSmsBroadcastFailure()
}