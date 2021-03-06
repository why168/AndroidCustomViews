package io.github.why168.view.floatball.runner


interface ICarrier {

    fun onMove(lastX: Int, lastY: Int, curX: Int, curY: Int)

    fun onDone()

    fun post(runnable: Runnable): Boolean

    fun removeCallbacks(action: Runnable): Boolean
}
