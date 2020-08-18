package dev.wiisportsresorts.duelsplugin.util

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future

class Timeout internal constructor(delayMs: Int, var callback: () -> Unit) {
    private var future: Future<Void> = CompletableFuture()

    fun cancel() {
        future.cancel(true)
    }

    private fun run() {
        callback()
    }

    companion object {
        fun setTimeout(delayMs: Int, callback: () -> Unit): Timeout {
            return Timeout(delayMs, callback)
        }
    }

    init {
        Executors.newCachedThreadPool().submit<Any?> {
            Thread.sleep(delayMs.toLong())
            run()
            null
        }
    }
}