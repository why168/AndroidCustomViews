package io.github.why168.view.floatball.runner


import android.content.Context
import android.view.animation.LinearInterpolator
import android.widget.Scroller

class ScrollRunner(private val mCarrier: ICarrier, context: Context) : Runnable {

    private val mScroller: Scroller = Scroller(context, LinearInterpolator())
    private var mDuration = 250
    private var lastX: Int = 0
    private var lastY: Int = 0

    val isRunning: Boolean = !mScroller.isFinished

    fun start(dx: Int, dy: Int, duration: Int = mDuration) {
        start(0, 0, dx, dy, duration)
    }

    fun start(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int = mDuration) {
        this.mDuration = duration
        mScroller.startScroll(startX, startY, dx, dy, duration)
        mCarrier.removeCallbacks(this)
        mCarrier.post(this)
        lastX = startX
        lastY = startY
    }

    override fun run() {
        if (mScroller.computeScrollOffset()) {
            val currentX = mScroller.currX
            val currentY = mScroller.currY
            mCarrier.onMove(lastX, lastY, currentX, currentY)
            mCarrier.post(this)
            lastX = currentX
            lastY = currentY
        } else {
            mCarrier.removeCallbacks(this)
            mCarrier.onDone()
        }
    }

}
