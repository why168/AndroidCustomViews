package io.github.why168.view.floatball.menu

import android.support.annotation.DrawableRes


/**
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/25 下午7:46
 * @since JDK1.8
 * @param mDrawable 菜单icon
 */
abstract class MenuItem(@DrawableRes var mDrawable: Int) {

    abstract fun action()

    abstract fun setDrawable(): Pair<Boolean, Int>
}
