package io.github.why168.view.floatball

import android.app.Activity
import android.graphics.Paint
import android.graphics.Point
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatTextView
import android.view.*
import android.widget.FrameLayout
import io.github.why168.view.floatball.menu.MenuItem

import java.util.ArrayList
import android.widget.Toast


/**
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/24 下午4:02
 * @since JDK1.8
 */
class FloatBallManager(activity: Activity,
                       ballCfg: FloatBallConfig,
                       menuCfg: FloatMenuConfig,
                       tipCallListener: (() -> Boolean)? = null) {

    private var mWindowManager: WindowManager
    private var floatMenu: FloatMenu
    private var statusBarView: StatusBarView
    private var isShowing = false
    private var menuItems: MutableList<MenuItem> = ArrayList()
    private var mActivity: Activity = activity
    private var floatBall: FloatBall
    private var viewTipLayout: FrameLayout // 标签布局
    private var tipTextView: AppCompatTextView // 标签提示

    var floatBallX: Int = 0
    var floatBallY: Int = 0
    var mScreenWidth: Int = 0
    var mScreenHeight: Int = 0

    var ballSize: Int = 0
        get() = floatBall.size

    var statusBarHeight: Int = 0
        get() = statusBarView.statusBarHeight

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


        tipTextView.setOnClickListener { it ->
            tipCallListener?.invoke()?.let {
                mWindowManager.removeViewImmediate(viewTipLayout)
            }
        }
    }

    fun showTipView(tip: String, underline: Boolean = true) {
        // 判断是否在Window上先移除View
        if (ViewCompat.isAttachedToWindow(viewTipLayout)) {
            mWindowManager.removeViewImmediate(viewTipLayout)
        }

        if (underline)
            tipTextView.paint.apply {
                flags = Paint.UNDERLINE_TEXT_FLAG //下划线
                isAntiAlias = true//抗锯齿
            }

        tipTextView.text = tip

        val measuredHeight = viewTipLayout.measuredHeight

        /*
        val rect1 = Rect()
        floatBall.imageView.getWindowVisibleDisplayFrame(rect1)
        println("rect1 ${rect1.toString()}")

        val rect2 = intArrayOf(0, 0)
        floatBall.imageView.getLocationInWindow(rect2)
        println("rect2 x = ${rect2[0]} y = ${rect2[1]}")

        val rect3 = Rect()
        floatBall.imageView.getLocalVisibleRect(rect3)
        println("rect3 $rect3")
        */

        val rect4 = intArrayOf(0, 0)
        floatBall.imageView.getLocationOnScreen(rect4)
//        println("rect4 x = ${rect4[0]} y = ${rect4[1]}")

        /*
         val floatBallX = floatBall.mLayoutParams.x
         val floatBallY = floatBall.mLayoutParams.y
         */

        val floatBallX = rect4[0]
        val floatBallY = rect4[1]

        // 获取屏幕的宽度
        /*
        val point = Point()
        mWindowManager.defaultDisplay.getSize(point)
        Log.i("TAG", point.toString())
        */
        val statusBarLayoutParams = FloatBallUtil.getLayoutParams(mActivity, false)




        if ((mScreenWidth / 2) < floatBallX) {
            // 右边的显示状态
            tipTextView.setBackgroundResource(R.drawable.icon_right_tip)


            val makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 shl 30) - 1, View.MeasureSpec.AT_MOST)
            viewTipLayout.measure(makeMeasureSpec, makeMeasureSpec)

            statusBarLayoutParams.x = floatBallX - viewTipLayout.measuredWidth - 10
            statusBarLayoutParams.y = floatBallY - measuredHeight / 2
            Toast.makeText(mActivity, "右边的显示状态", Toast.LENGTH_SHORT).show()
        } else {
            // 左边的显示状态
            tipTextView.setBackgroundResource(R.drawable.icon_left_tip)
            statusBarLayoutParams.x = floatBallX + floatBall.width + 10
            statusBarLayoutParams.y = floatBallY - measuredHeight / 2
            Toast.makeText(mActivity, "左边的显示状态", Toast.LENGTH_SHORT).show()
        }

//        Log.i("TAG", "measuredHeight = $measuredHeight , floatBallX = $floatBallX，floatBallY = $floatBallY")


        //TODO bug 执行动画卡顿
/*
        val valueAnimator = ValueAnimator.ofInt(0, viewTipLayout.measuredWidth)
        valueAnimator.duration = 1000
//        valueAnimator.interpolator = TimeInterpolator(0)
        valueAnimator.addUpdateListener { animator ->
            val currentValue = animator.animatedValue as Int
            // 获得每次变化后的属性值
//            println(currentValue)
            // 输出每次变化后的属性值进行查看

            */
/*
            val layoutParams = viewTipLayout.layoutParams
            layoutParams.width = currentValue
            layoutParams.height = measuredHeight
            *//*


            statusBarLayoutParams.width = currentValue
            statusBarLayoutParams.height = measuredHeight

//            viewTipLayout.layoutParams = statusBarLayoutParams
//            viewTipLayout.postInvalidate()

            mWindowManager.updateViewLayout(viewTipLayout, statusBarLayoutParams)

//            println("height = ${layoutParams.height}  width = ${layoutParams.width}  x = ${layoutParams.x}  y = ${layoutParams.y}")
        }
*/

        mWindowManager.addView(viewTipLayout, statusBarLayoutParams)


//        valueAnimator.start()
    }

    fun closeTipView(delayMillis: Long = 0) {
        viewTipLayout.postDelayed({
            if (ViewCompat.isAttachedToWindow(viewTipLayout)) {
                mWindowManager.removeViewImmediate(viewTipLayout)
            }
        }, delayMillis)

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
