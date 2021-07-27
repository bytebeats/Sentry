package me.bytebeats.sentry

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.text.TextUtils
import me.bytebeats.sentry.network.NetworkChangeListenHelper


/**
 * Created by bytebeats on 2021/6/5 : 17:21
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
class SentryApplication : Application() {

    private var mNetworkChangeListenHelper: NetworkChangeListenHelper? = null

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        if (isAppMainProcess()) {
            registerNetworkListener()
        }
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

    /**
     * 监听网络变化
     */
    private fun registerNetworkListener() {
        if (mNetworkChangeListenHelper?.hasRegisterNetworkCallback() == true) {
            return
        }
        mNetworkChangeListenHelper = NetworkChangeListenHelper()
        mNetworkChangeListenHelper?.registerNetworkCallback(this, object :
            NetworkChangeListenHelper.NetworkChangeListener {
            override fun onNetworkChange(isNetworkAvailable: Boolean) {
                if (!isNetworkAvailable) {
                    return
                }
            }
        })
    }

    /**
     * Application.onCreate 在多进程时会被调用多次.
     * 为了使一些初始化操作只能在主进程中进行, 特使用该方法来区分进程
     * @return
     */
    protected fun isAppMainProcess(): Boolean {
        return try {
            val pid: Int = android.os.Process.myPid()
            val process = getProcessNameByPid(pid)
            !TextUtils.isEmpty(process) && TextUtils.equals(packageName, process)
        } catch (e: Exception) {
            true
        }
    }

    private fun getProcessNameByPid(pid: Int): String {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (manager == null || manager.runningAppProcesses == null) {
            return ""
        }
        for (runningAppProcess in manager.runningAppProcesses) {
            if (runningAppProcess.pid == pid) {
                return runningAppProcess.processName
            }
        }
        return ""
    }
}