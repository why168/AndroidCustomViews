package io.github.why168.view.floatball.utlis

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View

object Util {

    fun setBackground(view: View, drawable: Drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = drawable
        } else {
            view.setBackgroundDrawable(drawable)
        }
    }
}
