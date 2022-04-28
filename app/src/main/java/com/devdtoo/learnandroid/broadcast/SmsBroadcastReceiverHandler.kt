package com.devdtoo.learnandroid.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.lang.ref.WeakReference


class SmsBroadcastReceiverHandler : BroadcastReceiver(){
    var listener : SmsReceiverListener? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("BroadcastReceiver", "Broadcast Triggered")
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
            val extras = intent.extras
            val status: Status? = extras?.get(SmsRetriever.EXTRA_STATUS) as Status?

            when (status?.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    Log.d("BroadcastReceiver", "SMS Received ")
                    val msgIntent : Intent? = extras?.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                    Log.d("BroadcastReceiver", "SMS DATA: ${msgIntent?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)} ")
                    msgIntent?.let {
                        listener?.onSmsBroadcastSuccess(it)
                    }

                }
                CommonStatusCodes.TIMEOUT -> {
                    listener?.onSmsBroadcastFailure()
                }
            }
        }


        /*Log.d("BroadcastReceiver", "Airplane Broadcast Triggered")
        val isAirplaneModeEnabled = intent?.getBooleanExtra("state", false) ?: return

        // checking whether airplane mode is enabled or not
        if (isAirplaneModeEnabled) {
            // showing the toast message if airplane mode is enabled
            Toast.makeText(context, "Airplane Mode Enabled", Toast.LENGTH_LONG).show()
        } else {
            // showing the toast message if airplane mode is disabled
            Toast.makeText(context, "Airplane Mode Disabled", Toast.LENGTH_LONG).show()
        }*/
    }
}