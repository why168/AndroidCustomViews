package io.github.why168.view.floatball

import android.graphics.drawable.Drawable


/**
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/25 下午7:46
 * @since JDK1.8
 * @param mDrawable 菜单icon
 */
abstract class MenuItem(var mDrawable: Drawable) {

    /**
     * 点击次菜单执行的操作
     */
    abstract fun action()
}
