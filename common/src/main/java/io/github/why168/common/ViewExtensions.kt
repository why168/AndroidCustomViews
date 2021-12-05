package io.github.why168.common

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
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

fun Context.getStringRes(@StringRes id: Int): String = getString(id)

fun View.dp2px(dpValue: Float): Float = dpValue * context.resources.displayMetrics.density + 0.5f * if (dpValue >= 0) 1 else -1
fun Context.dp2px(dpValue: Float): Float = dpValue * resources.displayMetrics.density + 0.5f * if (dpValue >= 0) 1 else -1

fun View.px2dip(dpValue: Float): Float = dpValue / context.resources.displayMetrics.density + 0.5f * if (dpValue >= 0) 1 else -1
fun Context.px2dip(dpValue: Float): Float = dpValue / resources.displayMetrics.density + 0.5f * if (dpValue >= 0) 1 else -1

fun View.sp2px(sxValue: Float): Float = sxValue * context.resources.displayMetrics.density + 0.5f * if (sxValue >= 0) 1 else -1
fun Context.sp2px(sxValue: Float): Float = sxValue * resources.displayMetrics.density + 0.5f * if (sxValue >= 0) 1 else -1

fun View.px2sp(pxValue: Float): Float = pxValue / context.resources.displayMetrics.density + 0.5f * if (pxValue >= 0) 1 else -1
fun Context.px2sp(pxValue: Float): Float = pxValue / resources.displayMetrics.density + 0.5f * if (pxValue >= 0) 1 else -1


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

fun View.setBackgroundDrawables(drawable: Drawable) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        background = drawable
    } else {
        setBackgroundDrawable(drawable)
    }
}