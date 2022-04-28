package com.devdtoo.learnandroid

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.devdtoo.learnandroid.broadcast.SmsBroadcastReceiverHandler
import com.devdtoo.learnandroid.broadcast.SmsReceiverListener
import com.devdtoo.learnandroid.databinding.ActivityMainBinding
import com.devdtoo.learnandroid.services.ServiceHandler
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.regex.Pattern

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var smsBroadcastReceiver: SmsBroadcastReceiverHandler? = null
//    lateinit var startForProfileImageResult: ActivityResultLauncher<Intent>
    private val REQ_USER_CONSENT = 200



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        startSmartUserConsent()


        /*val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data
                Log.d("MainActivityLog", "ResultCode : $resultCode")
                Log.d("MainActivityLog", "Data : $data")
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)?.let {
                            getOtpFromMessage(it)
                        }

                    }
                }
            }*/

    }

    fun getOtpFromMessage(message: String) {
        Log.d("MainActivity Broadcast", "Pattern triggered")
        val otpPattern = Pattern.compile("(|^)\\d{6}")
        val matcher = otpPattern.matcher(message)
        if (matcher.find()) {
            Log.d("MainActivity Broadcast", "OTP parsed:  ${matcher.group(0)}")
            binding.textView.setText(matcher.group(0))
        }
    }

    fun startSmartUserConsent() {
        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
    }

    fun registerSmsBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiverHandler()
        smsBroadcastReceiver!!.listener = object : SmsReceiverListener{
            override fun onSmsBroadcastSuccess(otpintent: Intent) {
                Log.d("MainActivity Broadcast", "onSuccess Triggered ")
                startActivityForResult(intent, REQ_USER_CONSENT)
            }

            override fun onSmsBroadcastFailure() {
                Log.d("MainActivity Broadcast", "onFailure Triggered ")
                Toast.makeText(this@MainActivity, "SMS Listener Failed", Toast.LENGTH_SHORT).show()
            }
        }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)

        /*smsBroadcastReceiver = SmsBroadcastReceiverHandler()
        smsBroadcastReceiver.listener = this
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter, SmsRetriever.SEND_PERMISSION, null)*/

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity Broadcast", "onActivityResult Triggered ")
        if (requestCode == REQ_USER_CONSENT) {
            Log.d("MainActivity Broadcast", "onActivityResult requestCode passed ")
            if (resultCode == RESULT_OK && data != null) {
                Log.d("MainActivity Broadcast", "onActivityResult resultCode passed")
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                Log.d("MainActivity Broadcast", "onActivityResult message: $message")
                getOtpFromMessage(message!!)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        registerSmsBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }



}