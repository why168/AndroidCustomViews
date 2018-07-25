package io.github.why168.view.floatball

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.Point
import android.os.Build
import android.text.TextPaint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView

import java.util.ArrayList

/**
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/24 下午4:02
 * @since JDK1.8
 */
class FloatBallManager {
    var mScreenWidth: Int = 0
    var mScreenHeight: Int = 0
    private var mPermission: IFloatBallPermission? = null
    private var mFloatballClickListener: OnFloatBallClickListener? = null
    private var mWindowManager: WindowManager? = null
    private var mContext: Context? = null
    var floatBall: FloatBall? = null
        private set
    var floatMenu: FloatMenu? = null
        private set
    private var statusBarView: StatusBarView? = null
    var floatballX: Int = 0
    var floatballY: Int = 0
    private var isShowing = false
    private var menuItems: MutableList<MenuItem>? = ArrayList()
    private var mActivity: Activity? = null

    val menuItemSize: Int
        get() = if (menuItems != null) menuItems!!.size else 0

    val ballSize: Int
        get() = floatBall!!.size

    val statusBarHeight: Int
        get() = statusBarView!!.statusBarHeight

    constructor(application: Context, ballCfg: FloatBallCfg, menuCfg: FloatMenuCfg? = null) {
        mContext = application.applicationContext
        FloatBallUtil.inSingleActivity = false
        mContext?.apply {
            mWindowManager = getSystemService(android.content.Context.WINDOW_SERVICE) as android.view.WindowManager
            computeScreenSize()
            floatBall = FloatBall(this, this@FloatBallManager, ballCfg)
            floatMenu = FloatMenu(this, this@FloatBallManager, menuCfg)
            statusBarView = StatusBarView(this, this@FloatBallManager)
        }

//        mWindowManager = mContext!!.getSystemService(android.content.Context.WINDOW_SERVICE) as android.view.WindowManager
//        computeScreenSize()
//        floatBall = FloatBall(mContext!!, this, ballCfg)
//        floatMenu = FloatMenu(mContext!!, this, menuCfg)
//        statusBarView = StatusBarView(mContext!!, this)
    }

    constructor(activity: Activity, ballCfg: FloatBallCfg, menuCfg: FloatMenuCfg? = null) {
        mActivity = activity
        FloatBallUtil.inSingleActivity = true
        mActivity?.apply {
            mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            computeScreenSize()
            floatBall = FloatBall(this, this@FloatBallManager, ballCfg)
            floatMenu = FloatMenu(this, this@FloatBallManager, menuCfg)
            statusBarView = StatusBarView(this, this@FloatBallManager)
        }

//        mWindowManager = mActivity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        computeScreenSize()
//        floatBall = FloatBall(mActivity!!, this, ballCfg)
//        floatMenu = FloatMenu(mActivity!!, this, menuCfg)
//        statusBarView = StatusBarView(mActivity!!, this)
    }

    fun buildMenu() {
        inflateMenuItem()
    }

    fun showTipView(tip: String, mOnTipViewClickListener: OnTipViewClickListener?) {
        val tipView = LayoutInflater.from(mActivity).inflate(R.layout.view_tip, null) as FrameLayout
        val i = View.MeasureSpec.makeMeasureSpec((1 shl 30) - 1, View.MeasureSpec.AT_MOST)
        tipView.measure(i, i)

        val tv = tipView.findViewById<TextView>(R.id.tipTextView)
        tv.text = " 震惊！恋养猫还能这样搜索... "
        val paint = tv.paint
        paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
        paint.isAntiAlias = true//抗锯齿
        tv.setOnClickListener {
            mOnTipViewClickListener?.onTipViewClick()
        }


        val measuredHeight = tipView.measuredHeight

        val statusBarLayoutParams = FloatBallUtil.getLayoutParams(mActivity, false)
        statusBarLayoutParams.x = floatballX / 2 - ballSize / 2
        statusBarLayoutParams.y = (floatballY - ballSize / 2 + measuredHeight * 1.5).toInt()

        Log.i("TAG", "measuredHeight = $measuredHeight , floatballX = $floatballX，floatballY = $floatballY")


        //        mWindowManager.removeView(tipView);
        //        mWindowManager.removeViewImmediate(tipView);

        //        ValueAnimator valueAnimator = ValueAnimator.ofInt(100, tipView.getMeasuredWidth());
        //        valueAnimator.setDuration(2000);
        ////        valueAnimator.setInterpolator(new LinearInterpolator());
        //        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        //            @Override
        //            public void onAnimationUpdate(ValueAnimator animator) {
        //                int currentValue = (Integer) animator.getAnimatedValue();
        //                // 获得每次变化后的属性值
        //                System.out.println(currentValue);
        //                // 输出每次变化后的属性值进行查看
        //
        //                ViewGroup.LayoutParams layoutParams = tipView.getLayoutParams();
        //                layoutParams.width = currentValue;
        //                layoutParams.height = 60;
        //                tipView.setVisibility(View.VISIBLE);
        //                mWindowManager.updateViewLayout(tipView, layoutParams);
        //            }
        //        });

        //        tipView.setVisibility(View.INVISIBLE);
        mWindowManager!!.addView(tipView, statusBarLayoutParams)
        //        valueAnimator.start();
    }

    /**
     * 添加一个菜单条目
     *
     * @param item
     */
    fun addMenuItem(item: MenuItem): FloatBallManager {
        menuItems!!.add(item)
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
        floatMenu!!.removeAllItemViews()
        for (item in menuItems!!) {
            floatMenu!!.addItem(item)
        }
    }

    fun computeScreenSize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val point = Point()
            mWindowManager!!.defaultDisplay.getSize(point)
            mScreenWidth = point.x
            mScreenHeight = point.y
        } else {
            mScreenWidth = mWindowManager!!.defaultDisplay.width
            mScreenHeight = mWindowManager!!.defaultDisplay.height
        }
    }

    fun onStatusBarHeightChange() {
        floatBall!!.onLayoutChange()
    }

    fun show() {
        if (mActivity == null) {
            if (mPermission == null) {
                return
            }
            if (!mPermission!!.hasFloatBallPermission(mContext!!)) {
                mPermission!!.onRequestFloatBallPermission()
                return
            }
        }
        if (isShowing) return
        isShowing = true
        floatBall!!.visibility = View.VISIBLE

        statusBarView!!.attachToWindow(mWindowManager!!)

        floatBall!!.attachToWindow(mWindowManager!!)

        floatMenu!!.detachFromWindow(mWindowManager!!)
    }

    fun closeMenu() {
        floatMenu!!.closeMenu()
    }

    fun reset() {
        floatBall!!.visibility = View.VISIBLE
        floatBall!!.postSleepRunnable()
        floatMenu!!.detachFromWindow(mWindowManager!!)
    }

    fun onFloatBallClick() {
        if (menuItems != null && menuItems!!.size > 0) {
            floatMenu!!.attachToWindow(mWindowManager!!)
        } else {
            if (mFloatballClickListener != null) {
                mFloatballClickListener!!.onFloatBallClick()
            }
        }
    }

    fun hide() {
        if (!isShowing) return
        isShowing = false
        floatBall!!.detachFromWindow(mWindowManager!!)
        floatMenu!!.detachFromWindow(mWindowManager!!)
        statusBarView!!.detachFromWindow(mWindowManager!!)
    }

    fun onConfigurationChanged() {
        computeScreenSize()
        reset()
    }

    fun setPermission(iPermission: IFloatBallPermission) {
        this.mPermission = iPermission
    }

    fun setOnFloatBallClickListener(listener: OnFloatBallClickListener) {
        mFloatballClickListener = listener
    }

    interface OnTipViewClickListener {
        fun onTipViewClick()
    }


    interface OnFloatBallClickListener {
        fun onFloatBallClick()
    }

    interface IFloatBallPermission {
        /**
         * request the permission of floatball,just use [.requestFloatBallPermission],
         * or use your custom method.
         *
         * @return return true if requested the permission
         * @see .requestFloatBallPermission
         */
        fun onRequestFloatBallPermission(): Boolean

        /**
         * detect whether allow  using floatball here or not.
         *
         * @return
         */
        fun hasFloatBallPermission(context: Context): Boolean

        /**
         * request floatball permission
         */
        fun requestFloatBallPermission(activity: Activity)
    }
}
