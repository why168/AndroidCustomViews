package io.github.why168.view.floatball

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager


/**
 * 状态栏视图
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/24 下午4:26
 * @since JDK1.8
 */
@SuppressLint("ViewConstructor")
class StatusBarView(mContext: Context, private val mFloatBallManager: FloatBallManager) : View(mContext) {
    private val mLayoutParams: WindowManager.LayoutParams = FloatBallUtil.getStatusBarLayoutParams(mContext)
    private var isAdded: Boolean = false
    private val layoutChangeListener = OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom -> mFloatBallManager.onStatusBarHeightChange() }

    val statusBarHeight: Int
        get() {
            val windowParams = IntArray(2)
            val screenParams = IntArray(2)
            getLocationInWindow(windowParams)
            getLocationOnScreen(screenParams)
            return screenParams[1] - windowParams[1]
        }

    fun attachToWindow(wm: WindowManager) {
        if (!isAdded) {
            addOnLayoutChangeListener(layoutChangeListener)
            wm.addView(this, mLayoutParams)
            isAdded = true
        }
    }

    fun detachFromWindow(windowManager: WindowManager) {
        if (!isAdded) return
        isAdded = false
        removeOnLayoutChangeListener(layoutChangeListener)
        if (context is Activity) {
            windowManager.removeViewImmediate(this)
        } else {
            windowManager.removeView(this)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}