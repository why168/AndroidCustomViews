package io.github.why168.view.floatball.runner

import android.view.View

abstract class OnceRunnable : Runnable {

    var isRunning: Boolean = false

    override fun run() {
        onRun()
        isRunning = false
    }

    abstract fun onRun()

    fun postSelf(carrier: View) {
        postDelaySelf(carrier, 0)
    }

    fun postDelaySelf(carrier: View, delay: Int) {
        if (!isRunning) {
            carrier.postDelayed(this, delay.toLong())
            isRunning = true
        }
    }

    fun removeSelf(carrier: View) {
        isRunning = false
        carrier.removeCallbacks(this)
    }
}
