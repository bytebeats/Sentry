package me.bytebeats.sentry.apm.cmd

import android.util.Log
import java.io.*
import java.net.InetAddress
import java.net.Socket
import java.net.URL

/**
 * Created by bytebeats on 2021/7/26 : 16:07
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
class CommandUtil {
    fun execute(cmds: Array<String>?): List<String>? {
        if (cmds == null) return null
        val execResult = mutableListOf<String>()
        debug("execute command start: $cmds")
        var process: Process? = null
        var successReader: BufferedReader? = null
        var errorReader: BufferedReader? = null
        var errorMessage: StringBuilder? = null
        var dos: DataOutputStream? = null
        var status = 0
        try {
            process = Runtime.getRuntime().exec(CMD_SH)
            dos = DataOutputStream(process.outputStream)
            for (i in cmds.indices) {
                if (cmds[i].isEmpty()) continue
                dos.write(cmds[i].toByteArray())
                dos.writeBytes(CMD_LINE_END)
                dos.flush()
            }
            dos.writeBytes(CMD_EXIT)
            dos.flush()

            status = process.waitFor()

            errorMessage = StringBuilder()
            successReader = BufferedReader(InputStreamReader(process.inputStream, Charsets.UTF_8))
            errorReader = BufferedReader(InputStreamReader(process.errorStream, Charsets.UTF_8))
            var lineMessage: String? = successReader.readLine()
            while (lineMessage != null) {
                execResult.add(lineMessage)
                debug(" command line item: $lineMessage")
                lineMessage = successReader.readLine()
            }
            lineMessage = errorReader.readLine()
            while (lineMessage != null) {
                errorMessage.append(lineMessage)
                lineMessage = errorReader.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                dos?.close()
                successReader?.close()
                errorReader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            process?.destroy()
        }
        debug("execute command end, error: $errorMessage, and status: $status")
        return execResult
    }

    /**
     * in ascending order, the first domain took the least time
     * For selecting the least time-taking server address for the same request and response
     */
    fun sortDomainByDNS(
        domains: List<String>,
        sudo: Boolean = false
    ): List<String> {
        if (domains.isEmpty()) return emptyList()
        val ds = mutableMapOf<String, Long>()
        for (domain in domains) {
            val start = System.currentTimeMillis()
            val cmd = if (sudo) "sudo -c $CMD_PING $domain" else "$CMD_PING $domain"
            val process = Runtime.getRuntime().exec(cmd)
            val status = process.waitFor()
            val end = System.currentTimeMillis()
            if (status == 0) {
                ds[domain] = end - start
                process.destroy()
            } else {
                process.destroy()
                throw IllegalStateException("ping $domain TIMEOUT!")
            }
        }
        return ds.entries.sortedBy { it.value }.map { it.key }
    }

    fun ping(domains: List<String>): List<PingResult> {
        return domains.map { ping(it) }
    }

    private fun ping(domain: String): PingResult {
        val pingResult = PingResult()
        try {
            val url = URL(domain)
            val start = System.currentTimeMillis()
            val hostAddress = InetAddress.getByName(url.host).hostAddress
            val dnsResolved = System.currentTimeMillis()
            val socket = Socket(hostAddress, url.port)
            socket.close()
            val connectionResolved = System.currentTimeMillis()
            pingResult.host = url.host
            pingResult.ip = hostAddress
            pingResult.dnsTime = dnsResolved - start
            pingResult.connectionTime = connectionResolved - dnsResolved
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pingResult
    }

    private fun debug(message: String) {
        Log.d(TAG, message)
    }

    companion object {
        private const val TAG = "CommandUtil"
        private const val CMD_PING = "ping -c 1 -w 3"
        private const val CMD_SH = "sh"
        private const val CMD_LINE_END = "\n"
        private const val CMD_EXIT = "exit\n"
    }
}