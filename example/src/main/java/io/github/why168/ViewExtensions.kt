package io.github.why168

import android.app.Activity
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView

/**
 *
 * View扩展
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/11 上午2:01
 * @since JDK1.8
 */

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

/*

// 图片加载一
fun ImageView.loadImage(any: Any) {
    GlideApp
            .with(this)
            .load(any)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .into(this)
}

// 图片加载二
fun ImageView.loadImage(any: Any, @DrawableRes id: Int) {
    GlideApp
            .with(this)
            .load(any)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .error(id)  // 错误
            .fallback(id) // 当于传递了Null,传递nul
            .placeholder(id) // 占位符
            .into(this)
}
*/
