package com.devdtoo.learnandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.work.*
import com.devdtoo.learnandroid.databinding.ActivityMainBinding
import com.devdtoo.learnandroid.workmanager.MyWorker
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        with(binding) {
            periodicBtn.setOnClickListener {
                getPeriodicWorker()
            }
            oneTimeBtn.setOnClickListener {
                getOneTimeWorker()
            }
            clearBtn.setOnClickListener {

            }
        }


    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() Triggered")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() Triggered")
    }

    private fun observe() {

    }

    private fun getOneTimeWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(true)
            .build()

        val myWorkReq = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(myWorkReq)
    }

    private fun getPeriodicWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val myWorkReq = PeriodicWorkRequest.Builder(
            MyWorker::class.java, 15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .addTag("MyUniqueId")
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "MyUniqueId",
                ExistingPeriodicWorkPolicy.KEEP,
                myWorkReq
            )
    }

}