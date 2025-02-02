package com.deepseek.android

import android.app.Application
import android.util.Log

class DeepSeekApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Configure large heap for model loading
        val runtime = Runtime.getRuntime()
        val maxMemory = runtime.maxMemory()
        Log.i(TAG, "Max memory: ${maxMemory / 1024 / 1024}MB")
    }

    companion object {
        private const val TAG = "DeepSeekApplication"
    }
}
