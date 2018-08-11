package io.github.why168.view.floatball

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.support.v7.widget.AppCompatImageView
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import io.github.why168.common.setBackgroundDrawables

import io.github.why168.view.floatball.runner.ICarrier
import io.github.why168.view.floatball.runner.OnceRunnable
import io.github.why168.view.floatball.runner.ScrollRunner
import io.github.why168.view.floatball.utlis.MotionVelocityUtil


/**
 * 浮球
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/21 下午2:36
 * @since JDK1.8
 */
class FloatBall(context: Context,
                private val floatBallManager: FloatBallManager,
                private val mConfig: FloatBallCfg) : FrameLayout(context), ICarrier {

    var mLayoutParams: WindowManager.LayoutParams? = null
    private var windowManager: WindowManager? = null
    private var isFirst = true
    private var isAdded = false
    private var mTouchSlop: Int = 0 //  最小滑动的距离
    private var isClick: Boolean = false // 标记一个触摸是单击事件
    private var mDownX: Int = 0
    private var mDownY: Int = 0
    private var mLastX: Int = 0
    private var mLastY: Int = 0
    var size: Int = mConfig.mSize
    private var mRunner: ScrollRunner? = null
    private var mVelocityX: Int = 0
    private var mVelocityY: Int = 0
    private var mVelocity: MotionVelocityUtil? = null
    private var sleep = false
    private var mHideHalfLater = true
    private var mLayoutChanged = false
    private var mSleepX = -1

    private val mSleepRunnable = object : OnceRunnable() {
        override fun onRun() {
            if (mHideHalfLater && !sleep && isAdded) {
                sleep = true
                moveToEdge(false, sleep)
                mSleepX = mLayoutParams!!.x
            }
        }
    }

    init {
        init(context)
    }

    private fun init(context: Context) {
        val imageView = AppCompatImageView(context)

//        size = mConfig.mSize

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.background = mConfig.mIcon
        }

        imageView.setBackgroundDrawables(mConfig.mIcon)

        addView(imageView, ViewGroup.LayoutParams(size, size))
        initLayoutParams(context)

        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mRunner = ScrollRunner(this, context)
        mVelocity = MotionVelocityUtil(context)
    }

    private fun initLayoutParams(context: Context) {
        mLayoutParams = FloatBallUtil.getLayoutParams(context)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == View.VISIBLE) {
            onConfigurationChanged(null)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight
        val width = measuredWidth

        val curX = mLayoutParams!!.x
        if (sleep && curX != mSleepX && !mRunner!!.isRunning) {
            sleep = false
            postSleepRunnable()
        }

        if (mRunner!!.isRunning) {
            mLayoutChanged = false
        }

        if (height != 0 && isFirst || mLayoutChanged) {
            if (isFirst && height != 0) {
                location(width, height)
            } else {
                moveToEdge(false, sleep)
            }
            isFirst = false
            mLayoutChanged = false
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mLayoutChanged = true
        floatBallManager.onConfigurationChanged()
        moveToEdge(false, false)
        postSleepRunnable()
    }

    // 关闭拖动效果
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        mVelocity!!.acquireVelocityTracker(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> touchDown(x, y)
            MotionEvent.ACTION_MOVE -> touchMove(x, y)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> touchUp()
        }
        return super.onTouchEvent(event)
    }

    override fun onMove(lastX: Int, lastY: Int, curX: Int, curY: Int) {
        onMove(curX - lastX, curY - lastY)
    }

    override fun onDone() {
        postSleepRunnable()
    }

    fun attachToWindow(windowManager: WindowManager) {
        this.windowManager = windowManager
        if (!isAdded) {
            windowManager.addView(this, mLayoutParams)
            isAdded = true
        }
    }

    fun detachFromWindow(windowManager: WindowManager) {
        this.windowManager = null
        if (isAdded) {
            removeSleepRunnable()
            if (context is Activity) {
                windowManager.removeViewImmediate(this)
            } else {
                windowManager.removeView(this)
            }
            isAdded = false
            sleep = false
        }
    }

    private fun location(width: Int, height: Int) {
        val cfgGravity = mConfig.mGravity
        mHideHalfLater = mConfig.mHideHalfLater
        val gravity = cfgGravity.gravity
        val x: Int
        var y: Int
        val topLimit = 0
        val bottomLimit = floatBallManager.mScreenHeight - height
        val statusBarHeight = floatBallManager.statusBarHeight

        x = if (gravity and Gravity.LEFT != Gravity.LEFT) {
            floatBallManager.mScreenWidth - width
        } else {
            0
        }

        y = when {
            gravity and Gravity.TOP == Gravity.TOP -> topLimit
            gravity and Gravity.BOTTOM == Gravity.BOTTOM -> floatBallManager.mScreenHeight - height - statusBarHeight
            else -> floatBallManager.mScreenHeight / 2 - height / 2 - statusBarHeight
        }

        y = if (mConfig.mOffsetY != 0) y + mConfig.mOffsetY else y

        if (y < 0) y = topLimit
        if (y > bottomLimit)
            y = topLimit
        onLocation(x, y)
    }

    fun onLayoutChange() {
        mLayoutChanged = true
        requestLayout()
    }

    private fun touchDown(x: Int, y: Int) {
        mDownX = x
        mDownY = y
        mLastX = mDownX
        mLastY = mDownY
        isClick = true
        removeSleepRunnable()
    }

    /**
     * 移动的距离
     *
     * @param x
     * @param y
     */
    private fun touchMove(x: Int, y: Int) {
        val totalDeltaX = x - mDownX // 移动的X距离
        val totalDeltaY = y - mDownY // 移动的Y距离

        val deltaX = x - mLastX //
        val deltaY = y - mLastY

        // 判断是否在滑动，可否执行点击事件
        isClick = Math.abs(totalDeltaX) <= mTouchSlop && Math.abs(totalDeltaY) <= mTouchSlop

        mLastX = x
        mLastY = y

        if (!isClick) {
            onMove(deltaX, deltaY)
            Log.i("TAG", "X = $x , Y = $y")
        }
    }

    private fun touchUp() {
        mVelocity!!.computeCurrentVelocity()
        mVelocityX = mVelocity!!.xVelocity.toInt()
        mVelocityY = mVelocity!!.yVelocity.toInt()
        mVelocity!!.releaseVelocityTracker()
        if (sleep) {
            wakeUp()
        } else {
            if (isClick) {
                onClick()
            } else {
                moveToEdge(true, false)
            }
        }
        mVelocityX = 0
        mVelocityY = 0
    }

    private fun moveToX(smooth: Boolean, destX: Int) {
        val statusBarHeight = floatBallManager.statusBarHeight
        val screenHeight = floatBallManager.mScreenHeight - statusBarHeight
        val height = height
        var destY = 0

        if (mLayoutParams!!.y < 0) {
            destY = 0 - mLayoutParams!!.y
        } else if (mLayoutParams!!.y > screenHeight - height) {
            destY = screenHeight - height - mLayoutParams!!.y
        }

        if (smooth) {
            val dx = destX - mLayoutParams!!.x
            val duration = getScrollDuration(Math.abs(dx))
            mRunner!!.start(dx, destY, duration)
        } else {
            onMove(destX - mLayoutParams!!.x, destY)
            postSleepRunnable()
        }

    }

    private fun wakeUp() {
        val screenWidth = floatBallManager.mScreenWidth
        val width = width
        val halfWidth = width / 2
        val centerX = screenWidth / 2 - halfWidth
        val destX: Int
        destX = if (mLayoutParams!!.x < centerX) 0 else screenWidth - width
        sleep = false
        moveToX(true, destX)
    }

    private fun moveToEdge(smooth: Boolean, forceSleep: Boolean) {
        val screenWidth = floatBallManager.mScreenWidth
        val width = width

        val halfWidth = width / 2
        val centerX = screenWidth / 2 - halfWidth
        val destX: Int

        val minVelocity = mVelocity!!.minVelocity
        if (mLayoutParams!!.x < centerX) {
            sleep = forceSleep || Math.abs(mVelocityX) > minVelocity && mVelocityX < 0 || mLayoutParams!!.x < 0
            destX = if (sleep) -halfWidth else 0
        } else {
            sleep = forceSleep || Math.abs(mVelocityX) > minVelocity && mVelocityX > 0 || mLayoutParams!!.x > screenWidth - width
            destX = if (sleep) screenWidth - halfWidth else screenWidth - width
        }
        if (sleep) {
            mSleepX = destX
        }
        moveToX(smooth, destX)
    }

    private fun getScrollDuration(distance: Int): Int {
        return (250 * (1.0f * distance / 800)).toInt()
    }

    private fun onMove(deltaX: Int, deltaY: Int) {
        mLayoutParams!!.x += deltaX
        mLayoutParams!!.y += deltaY
        if (windowManager != null) {
            windowManager!!.updateViewLayout(this, mLayoutParams)
        }
    }

    fun onLocation(x: Int, y: Int) {
        mLayoutParams!!.x = x
        mLayoutParams!!.y = y
        if (windowManager != null) {
            windowManager!!.updateViewLayout(this, mLayoutParams)
        }
    }

    private fun moveTo(x: Int, y: Int) {
        mLayoutParams!!.x += x - mLayoutParams!!.x
        mLayoutParams!!.y += y - mLayoutParams!!.y
        if (windowManager != null) {
            windowManager!!.updateViewLayout(this, mLayoutParams)
        }
    }

    private fun onClick() {
        floatBallManager.floatBallX = mLayoutParams!!.x
        floatBallManager.floatBallY = mLayoutParams!!.y
        floatBallManager.onFloatBallClick()
    }

    private fun removeSleepRunnable() {
        mSleepRunnable.removeSelf(this)
    }

    fun postSleepRunnable() {
        if (mHideHalfLater && !sleep && isAdded) {
            mSleepRunnable.postDelaySelf(this, 3000)
        }
    }
}
