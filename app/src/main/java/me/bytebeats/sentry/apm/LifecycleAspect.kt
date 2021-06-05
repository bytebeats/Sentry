package me.bytebeats.sentry.apm

import android.nfc.Tag
import android.util.Log
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

/**
 * Created by bytebeats on 2021/6/5 : 17:53
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */

/**
 * Activity/Fragment生命周期方法统计
 */

@Aspect
class LifecycleAspect {
    @Before("execution(* android.app.Activity.on***(..)")
    fun callActivityOn(joinPoint: JoinPoint) {
        val signature = joinPoint.signature
        val target = joinPoint.target

        Log.i(TAG, "$target $signature called")
    }

    @Before("execution(* android.app.Fragment.on***(..)")
    fun callFragmentOn(joinPoint: JoinPoint) {
        val signature = joinPoint.signature
        val target = joinPoint.target

        Log.i(TAG, "$target $signature called")
    }

    companion object {
        const val TAG = "LifecycleAspect"
    }
}