package me.bytebeats.sentry.apm

import android.os.Trace
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

/**
 * Created by bytebeats on 2021/6/5 : 18:01
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */

/**
 * 对所有方法进行 Systrace 插桩
 * 由此生成的 html 文件可以查看方法耗时和 CPU 使用情况.
 */

@Aspect
class SystraceAspect {

    @Before("execution(* **(..)")
    fun before(joinPoint: JoinPoint) {
        Trace.beginSection(joinPoint.signature.toString())
    }

    @After("execution(* **(..))")
    fun after() {
        Trace.endSection()
    }

    companion object {
        const val TAG = "SystraceAspect"
    }
}