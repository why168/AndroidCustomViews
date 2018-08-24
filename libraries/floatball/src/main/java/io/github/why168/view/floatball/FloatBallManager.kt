package io.github.why168.view.floatball

import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Paint
import android.graphics.Point
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.*
import android.widget.FrameLayout

import java.util.ArrayList

/**
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/24 下午4:02
 * @since JDK1.8
 */
class FloatBallManager(activity: Activity,
                       ballCfg: FloatBallConfig,
                       menuCfg: FloatMenuCfg? = null,
                       tipCallListener: (() -> Boolean)? = null) {

    private var mWindowManager: WindowManager
    private var floatMenu: FloatMenu
    private var statusBarView: StatusBarView
    private var isShowing = false
    private var menuItems: MutableList<MenuItem> = ArrayList()
    private var mActivity: Activity = activity
    private var floatBall: FloatBall

    var floatBallX: Int = 0
    var floatBallY: Int = 0
    var mScreenWidth: Int = 0
    var mScreenHeight: Int = 0

    var ballSize: Int = 0
        get() = floatBall.size

    var statusBarHeight: Int = 0
        get() = statusBarView.statusBarHeight


    private var viewTipLayout: FrameLayout // 标签布局
    var tipTextView: AppCompatTextView // 标签提示


    init {
        mWindowManager = mActivity.windowManager
        computeScreenSize()
        floatBall = FloatBall(mActivity, this@FloatBallManager, ballCfg)
        floatMenu = FloatMenu(mActivity, this@FloatBallManager, menuCfg)
        statusBarView = StatusBarView(mActivity, this@FloatBallManager)

        // 初始化标签View
        viewTipLayout = LayoutInflater.from(mActivity).inflate(R.layout.view_tip, null) as FrameLayout
        val makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 shl 30) - 1, View.MeasureSpec.AT_MOST)
        viewTipLayout.measure(makeMeasureSpec, makeMeasureSpec)

        tipTextView = viewTipLayout.findViewById(R.id.tipTextView)
        tipTextView.paint.apply {
            flags = Paint.UNDERLINE_TEXT_FLAG //下划线
            isAntiAlias = true//抗锯齿
        }

        tipTextView.setOnClickListener { it ->
            tipCallListener?.invoke()?.let {
                mWindowManager.removeView(viewTipLayout)
            }
        }
    }

    fun showTipView(tip: String) {
        // 判断是否在Window上先移除View
        if (ViewCompat.isAttachedToWindow(viewTipLayout)) {
            mWindowManager.removeView(viewTipLayout)
        }


        tipTextView.text = tip

        val measuredHeight = viewTipLayout.measuredHeight

        val floatBallX = floatBall.mLayoutParams.x
        val floatBallY = floatBall.mLayoutParams.y

        val statusBarLayoutParams = FloatBallUtil.getLayoutParams(mActivity, false)
        statusBarLayoutParams.x = floatBallX / 2 - floatBall.size / 2
        statusBarLayoutParams.y = (floatBallY -  floatBall.size  / 2 + measuredHeight * 1.5).toInt()

        Log.i("TAG", "measuredHeight = $measuredHeight , floatBallX = $floatBallX，floatBallY = $floatBallY")

        val valueAnimator = ValueAnimator.ofInt(0, viewTipLayout.measuredWidth)
        valueAnimator.duration = 1000
//        valueAnimator.interpolator = TimeInterpolator(0)
        valueAnimator.addUpdateListener { animator ->
            val currentValue = animator.animatedValue as Int
            // 获得每次变化后的属性值
//            println(currentValue)
            // 输出每次变化后的属性值进行查看

            val layoutParams = viewTipLayout.layoutParams as WindowManager.LayoutParams
            layoutParams.width = currentValue
            layoutParams.height = measuredHeight


            viewTipLayout.layoutParams = layoutParams
//            viewTipLayout.postInvalidate()
            mWindowManager.updateViewLayout(viewTipLayout, layoutParams)

            println("height = ${layoutParams.height}  width = ${layoutParams.width}  x = ${layoutParams.x}  y = ${layoutParams.y}")
        }

        mWindowManager.addView(viewTipLayout, statusBarLayoutParams)
        valueAnimator.start()
    }


    /**
     * 添加一个菜单条目
     *
     * @param item
     */
    fun addMenuItem(item: MenuItem): FloatBallManager {
        menuItems.add(item)
        return this
    }

    /**
     * 设置菜单
     *
     * @param items
     */
    fun setMenu(items: MutableList<MenuItem>): FloatBallManager {
        menuItems = items
        return this
    }

    // 增加菜单项
    private fun inflateMenuItem() {
        floatMenu.removeAllItemViews()
        for (item in menuItems) {
            floatMenu.addItem(item)
        }
    }

    private fun computeScreenSize() {
        val point = Point()
        mWindowManager.defaultDisplay.getSize(point)
        mScreenWidth = point.x
        mScreenHeight = point.y
    }

    fun onStatusBarHeightChange() {
        floatBall.onLayoutChange()
    }

    fun buildMenu() {
        inflateMenuItem()
    }

    fun show() {
        if (isShowing) return

        isShowing = true
        floatBall.visibility = View.VISIBLE

        statusBarView.attachToWindow(mWindowManager)
        floatBall.attachToWindow(mWindowManager)
        floatMenu.detachFromWindow(mWindowManager)
    }

    fun closeMenu() {
        floatMenu.closeMenu()
    }

    fun reset() {
        floatBall.visibility = View.VISIBLE
        floatBall.postSleepRunnable()
        floatMenu.detachFromWindow(mWindowManager)
    }

    fun onFloatBallClick() {
        if (menuItems.size > 0) {
            floatMenu.attachToWindow(mWindowManager)
        }
    }

    fun hide() {
        if (!isShowing) return
        isShowing = false
        floatBall.detachFromWindow(mWindowManager)
        floatMenu.detachFromWindow(mWindowManager)
        statusBarView.detachFromWindow(mWindowManager)
    }

    fun onConfigurationChanged() {
        computeScreenSize()
        reset()
    }
}
