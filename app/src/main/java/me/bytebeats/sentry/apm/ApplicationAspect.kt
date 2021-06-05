package me.bytebeats.sentry.apm

import android.util.Log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import java.lang.Exception

/**
 * Created by bytebeats on 2021/6/5 : 17:46
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */


/**
 * 计算 Application 所有方法的耗时
 */
@Aspect
class ApplicationAspect {
    @Around("call (me.bytebeats.sentry.SentryApplication.**(..))")
    fun appMethodTiming(joinPoint: ProceedingJoinPoint) {
        val signature = joinPoint.signature
        val now = System.currentTimeMillis()
        try {
            joinPoint.proceed()
        } catch (e: Exception) {

        }
        val cost = System.currentTimeMillis() - now
        Log.i(TAG, "$signature cost ${cost}ms")
        if (cost > 300) {
            // TODO: 2021/6/5 性能监控工具上报
        }
    }

    companion object {
        const val TAG = "ApplicationAspect"
    }
}