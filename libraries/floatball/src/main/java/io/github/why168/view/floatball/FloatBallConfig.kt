package io.github.why168.view.floatball

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.view.Gravity


/**
 *
 * 悬浮球配置文件
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/24 下午7:51
 * @since JDK1.8
 */
data class FloatBallConfig(var size: Int = 0,
                           @DrawableRes
                           var icon: Int,
                           var gravity: Gravity = Gravity.LEFT_TOP,
                           var offsetY: Int = 0,
                           var hideHalfLater: Boolean = true) {

    //第一次显示的y坐标偏移量，左上角是原点。
    var mOffsetY = 0

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
