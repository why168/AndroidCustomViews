package io.github.why168.view.floatball.utlis


import android.content.Context
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration

/**
 *
 * 运动速度检测工具类
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/27 下午11:49
 * @since JDK1.8
 */
class MotionVelocityUtil(context: Context) {
    private var mVelocityTracker: VelocityTracker? = null
    private val mMaxVelocity: Int = ViewConfiguration.get(context).scaledMaximumFlingVelocity
    private val mMinVelocity: Int = ViewConfiguration.get(context).scaledMinimumFlingVelocity

    val minVelocity: Int
        get() = if (mMinVelocity < 1000) 1000 else mMinVelocity

    /**
     * Retrieve the last computed X velocity.  You must first call
     * [.computeCurrentVelocity] before calling this function.
     *
     * @return The previously computed X velocity.
     */
    val xVelocity: Float
        get() = mVelocityTracker!!.xVelocity

    /**
     * Retrieve the last computed Y velocity.  You must first call
     * [.computeCurrentVelocity] before calling this function.
     *
     * @return The previously computed Y velocity.
     */
    val yVelocity: Float
        get() = mVelocityTracker!!.yVelocity

    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see VelocityTracker.obtain
     * @see VelocityTracker.addMovement
     */
    fun acquireVelocityTracker(event: MotionEvent) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
    }

    fun computeCurrentVelocity() {
        mVelocityTracker!!.computeCurrentVelocity(1000, mMaxVelocity.toFloat())
    }

    /**
     * 释放VelocityTracker
     *
     * @see VelocityTracker.clear
     * @see VelocityTracker.recycle
     */
    fun releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker!!.clear()
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }
}
