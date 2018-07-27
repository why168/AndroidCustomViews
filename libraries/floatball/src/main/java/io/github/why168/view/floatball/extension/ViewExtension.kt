package io.github.why168.view.floatball.extension

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View

/**
 *
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/27 下午11:52
 * @since JDK1.8
 */


fun View.setBackgroundDrawables(drawable: Drawable) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        background = drawable
    } else {
        setBackgroundDrawable(drawable)
    }
}