package me.bytebeats.sentry

import android.app.Application
import android.content.Context
import android.content.res.Configuration

/**
 * Created by bytebeats on 2021/6/5 : 17:21
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
class SentryApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}