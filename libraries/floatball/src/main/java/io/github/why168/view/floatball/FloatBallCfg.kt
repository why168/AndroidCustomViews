package io.github.why168.view.floatball

import android.graphics.drawable.Drawable

class FloatBallCfg {
    var mIcon: Drawable
    var mSize: Int = 0
    /**
     * 标记悬浮球所处于屏幕中的位置
     *
     * @see Gravity.LEFT_TOP
     *
     * @see Gravity.LEFT_CENTER
     *
     * @see Gravity.LEFT_BOTTOM
     *
     * @see Gravity.RIGHT_TOP
     *
     * @see Gravity.RIGHT_CENTER
     *
     * @see Gravity.RIGHT_BOTTOM
     */
    var mGravity: Gravity
    //第一次显示的y坐标偏移量，左上角是原点。
    var mOffsetY = 0
    var mHideHalfLater = true

    constructor(size: Int, icon: Drawable, gravity: Gravity = Gravity.LEFT_TOP, offsetY: Int = 0) {
        mSize = size
        mIcon = icon
        mGravity = gravity
        mOffsetY = offsetY
    }

    constructor(size: Int, icon: Drawable, gravity: Gravity, hideHalfLater: Boolean) {
        mSize = size
        mIcon = icon
        mGravity = gravity
        mHideHalfLater = hideHalfLater
    }

    constructor(size: Int, icon: Drawable, gravity: Gravity, offsetY: Int, hideHalfLater: Boolean) {
        mSize = size
        mIcon = icon
        mGravity = gravity
        mOffsetY = offsetY
        mHideHalfLater = hideHalfLater
    }

    fun setGravity(gravity: Gravity) {
        mGravity = gravity
    }

    fun setHideHalfLater(hideHalfLater: Boolean) {
        mHideHalfLater = hideHalfLater
    }

    enum class Gravity(gravity: Int) {
        LEFT_TOP(android.view.Gravity.LEFT or android.view.Gravity.TOP),
        LEFT_CENTER(android.view.Gravity.LEFT or android.view.Gravity.CENTER),
        LEFT_BOTTOM(android.view.Gravity.LEFT or android.view.Gravity.BOTTOM),
        RIGHT_TOP(android.view.Gravity.RIGHT or android.view.Gravity.TOP),
        RIGHT_CENTER(android.view.Gravity.RIGHT or android.view.Gravity.CENTER),
        RIGHT_BOTTOM(android.view.Gravity.RIGHT or android.view.Gravity.BOTTOM);

        var gravity: Int = 0
            internal set

        init {
            this.gravity = gravity
        }
    }
}
