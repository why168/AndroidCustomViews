/*
 * Copyright (C) 2012 Capricorn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.why168.view.floatball

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView

@SuppressLint("ViewConstructor")
class FloatMenu(context: Context, private val floatBallManager: FloatBallManager, private val mConfig: FloatMenuCfg?) : FrameLayout(context) {
    private var mMenuLayout: MenuLayout? = null

    private var mIconView: ImageView? = null
    private var mPosition: Int = 0
    private var mItemSize: Int = 0
    private var size: Int = 0
    private val mDuration = 250
    private var mLayoutParams: WindowManager.LayoutParams? = null
    private var isAdded = false
    private var mBallSize: Int = 0
    private val mListenBackEvent = true

    val isMoving: Boolean
        get() = mMenuLayout!!.isMoving


    init {
        if (mConfig != null) {
            mItemSize = mConfig.mItemSize
            size = mConfig.mSize
            init(context)
            mMenuLayout!!.childSize = mItemSize
        }
    }


    private fun initLayoutParams(context: Context) {
        mLayoutParams = FloatBallUtil.getLayoutParams(context, mListenBackEvent)
    }

    fun removeViewTreeObserver(listener: ViewTreeObserver.OnGlobalLayoutListener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            viewTreeObserver.removeGlobalOnLayoutListener(listener)
        } else {
            viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        when (action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> if (mMenuLayout!!.isExpanded) {
                toggle(mDuration)
            }
        }
        return super.onTouchEvent(event)
    }

    fun attachToWindow(windowManager: WindowManager) {
        if (!isAdded) {
            mBallSize = floatBallManager.ballSize
            mLayoutParams!!.x = floatBallManager.floatBallX
            mLayoutParams!!.y = floatBallManager.floatBallY - size / 2
            mPosition = computeMenuLayout(mLayoutParams!!)
            refreshPathMenu(mPosition)
            toggle(mDuration)
            windowManager.addView(this, mLayoutParams)
            isAdded = true
        }
    }

    fun detachFromWindow(windowManager: WindowManager) {
        if (isAdded) {
            toggle(0)
            mMenuLayout!!.visibility = View.GONE
            if (context is Activity) {
                windowManager.removeViewImmediate(this)
            } else {
                windowManager.removeView(this)
            }
            isAdded = false
        }
    }

    private fun addMenuLayout(context: Context) {
        mMenuLayout = MenuLayout(context)
        val layoutParams = ViewGroup.LayoutParams(size, size)
        addView(mMenuLayout, layoutParams)
        mMenuLayout!!.visibility = View.INVISIBLE
    }

    private fun addControllLayout(context: Context) {
        mIconView = ImageView(context)
        val sublayoutParams = FrameLayout.LayoutParams(mBallSize, mBallSize)
        addView(mIconView, sublayoutParams)
    }

    private fun init(context: Context) {
        initLayoutParams(context)
        mLayoutParams!!.height = size
        mLayoutParams!!.width = size
        addMenuLayout(context)
        addControllLayout(context)

        mIconView!!.setOnClickListener { closeMenu() }

        if (mListenBackEvent) {
            setOnKeyListener(OnKeyListener { v, keyCode, event ->
                val action = event.action
                if (action == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        this@FloatMenu.floatBallManager.closeMenu()
                        return@OnKeyListener true
                    }
                }
                false
            })
            isFocusableInTouchMode = true
        }
    }

    fun closeMenu() {
        if (mMenuLayout!!.isExpanded) {
            toggle(mDuration)
        }
    }

    fun remove() {
        floatBallManager.reset()
        mMenuLayout!!.setExpand(false)
    }

    // 切换
    private fun toggle(duration: Int) {
        //持续时间==0 表示关闭菜单，因此如果已关闭，则不做任何操作。

        if (!mMenuLayout!!.isExpanded && duration <= 0) return

        mMenuLayout?.visibility = View.VISIBLE
        if (width == 0) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mMenuLayout?.switchState(mPosition, duration)
                    removeViewTreeObserver(this)
                }
            })
        } else {
            mMenuLayout?.switchState(mPosition, duration)
        }
    }

    fun addItem(menuItem: MenuItem) {
        if (mConfig == null) return
        val imageView = ImageView(context)
        imageView.setBackgroundDrawable(menuItem.mDrawable)
        mMenuLayout!!.addView(imageView)
        imageView.setOnClickListener {
            if (!mMenuLayout!!.isMoving)
                menuItem.action()
        }
    }

    fun removeAllItemViews() {
        mMenuLayout!!.removeAllViews()
    }

    /**
     * 根据按钮位置改变子菜单方向
     */
    fun refreshPathMenu(position: Int) {
        val menuLp = mMenuLayout!!.layoutParams as FrameLayout.LayoutParams
        val iconLp = mIconView!!.layoutParams as FrameLayout.LayoutParams

        when (position) {
            LEFT_TOP//左上
            -> {
                iconLp.gravity = Gravity.LEFT or Gravity.START or Gravity.TOP
                menuLp.gravity = Gravity.LEFT or Gravity.START or Gravity.TOP
                mMenuLayout?.setArc(0f, 90f, position)
            }
            LEFT_CENTER//左中
            -> {
                iconLp.gravity = Gravity.LEFT or Gravity.START or Gravity.CENTER_VERTICAL
                menuLp.gravity = Gravity.LEFT or Gravity.START or Gravity.CENTER_VERTICAL
                mMenuLayout?.setArc(270f, (270 + 180).toFloat(), position)
            }
            LEFT_BOTTOM//左下
            -> {
                iconLp.gravity = Gravity.LEFT or Gravity.START or Gravity.BOTTOM
                menuLp.gravity = Gravity.LEFT or Gravity.START or Gravity.BOTTOM
                mMenuLayout?.setArc(270f, 360f, position)
            }
            RIGHT_TOP//右上
            -> {
                iconLp.gravity = Gravity.RIGHT or Gravity.END or Gravity.TOP
                menuLp.gravity = Gravity.RIGHT or Gravity.END or Gravity.TOP
                mMenuLayout?.setArc(90f, 180f, position)
            }
            RIGHT_CENTER//右中
            -> {
                iconLp.gravity = Gravity.RIGHT or Gravity.END or Gravity.CENTER_VERTICAL
                menuLp.gravity = Gravity.RIGHT or Gravity.END or Gravity.CENTER_VERTICAL
                mMenuLayout?.setArc(90f, 270f, position)
            }
            RIGHT_BOTTOM//右下
            -> {
                iconLp.gravity = Gravity.BOTTOM or Gravity.RIGHT or Gravity.END
                menuLp.gravity = Gravity.BOTTOM or Gravity.RIGHT or Gravity.END
                mMenuLayout?.setArc(180f, 270f, position)
            }

            CENTER_TOP//上中
            -> {
                iconLp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                menuLp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                mMenuLayout?.setArc(0f, 180f, position)
            }
            CENTER_BOTTOM//下中
            -> {
                iconLp.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                menuLp.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                mMenuLayout?.setArc(180f, 360f, position)
            }
            CENTER -> {
                iconLp.gravity = Gravity.CENTER
                menuLp.gravity = Gravity.CENTER
                mMenuLayout?.setArc(0f, 360f, position)
            }
        }
        mIconView!!.layoutParams = iconLp
        mMenuLayout?.layoutParams = menuLp
    }

    /**
     * 计算菜单中各个view的位置
     *
     * @return
     */
    fun computeMenuLayout(layoutParams: WindowManager.LayoutParams): Int {
        var position = FloatMenu.RIGHT_CENTER
        val halfBallSize = mBallSize / 2
        val screenWidth = floatBallManager.mScreenWidth
        val screenHeight = floatBallManager.mScreenHeight
        val floatballCenterY = floatBallManager.floatBallY + halfBallSize

        var wmX = floatBallManager.floatBallX
        var wmY = floatballCenterY

        if (wmX <= screenWidth / 3)
        //左边  竖区域
        {
            wmX = 0
            if (wmY <= size / 2) {
                position = FloatMenu.LEFT_TOP//左上
                wmY = floatballCenterY - halfBallSize
            } else if (wmY > screenHeight - size / 2) {
                position = FloatMenu.LEFT_BOTTOM//左下
                wmY = floatballCenterY - size + halfBallSize
            } else {
                position = FloatMenu.LEFT_CENTER//左中
                wmY = floatballCenterY - size / 2
            }
        } else if (wmX >= screenWidth * 2 / 3)
        //右边竖区域
        {
            wmX = screenWidth - size
            if (wmY <= size / 2) {
                position = FloatMenu.RIGHT_TOP//右上
                wmY = floatballCenterY - halfBallSize
            } else if (wmY > screenHeight - size / 2) {
                position = FloatMenu.RIGHT_BOTTOM//右下
                wmY = floatballCenterY - size + halfBallSize
            } else {
                position = FloatMenu.RIGHT_CENTER//右中
                wmY = floatballCenterY - size / 2
            }
        }
        layoutParams.x = wmX
        layoutParams.y = wmY
        return position
    }

    companion object {

        val LEFT_TOP = 1
        val CENTER_TOP = 2
        val RIGHT_TOP = 3
        val LEFT_CENTER = 4
        val CENTER = 5
        val RIGHT_CENTER = 6
        val LEFT_BOTTOM = 7
        val CENTER_BOTTOM = 8
        val RIGHT_BOTTOM = 9
    }
}
