package me.bytebeats.sentry.apm.cmd

/**
 * Created by bytebeats on 2021/7/26 : 16:57
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
class PingResult {
    var net: String = "NO_CONNECTION"
    var host: String = ""
    var ip: String = ""

    var dnsTime: Long = 0
    var connectionTime: Long = 0
}
