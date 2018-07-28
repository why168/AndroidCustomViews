package round.io.github.why168.gradient

import android.app.Activity
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.View

fun View.isVisible(bool: Boolean?, nonVisibleState: Int = View.GONE) {
    visibility = if (bool == true) View.VISIBLE else nonVisibleState
}

// 获取像素大小
fun View.getPxSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

// 获取颜色值
fun View.getColorById(@ColorRes id: Int) = ContextCompat.getColor(context, id)

// 获取字符串值
fun View.getStringRes(@StringRes id: Int): String = context.getString(id)

// 根据手机的分辨率从 dp(密度) 转成为 px(像素)
fun View.dp2px(dpValue: Float): Float = dpValue * context.resources.displayMetrics.density + 0.5f * if (dpValue >= 0) 1 else -1

// 根据手机的分辨率从 px(像素) 转成为 dp(密度)
fun View.px2dip(dpValue: Float): Float = dpValue / context.resources.displayMetrics.density + 0.5f * if (dpValue >= 0) 1 else -1

// 获取屏幕宽度
fun Activity.getScreenSizeWidth(): Int {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics);
    return displayMetrics.widthPixels
}

// 获取屏幕高度
fun Activity.getScreenSizeHeight(): Int {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics);
    return displayMetrics.heightPixels
}
