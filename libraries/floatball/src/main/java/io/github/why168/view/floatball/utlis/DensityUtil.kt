package io.github.why168.view.floatball.utlis

import android.content.Context

object DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = getScale(context)
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = getScale(context)
        return (pxValue / scale + 0.5f).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = getScale(context)
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = getScale(context)
        return (spValue * fontScale + 0.5f).toInt()
    }

    private fun getScale(context: Context): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return findScale(fontScale)
    }

    private fun findScale(scale: Float): Float {
        var temScale = scale
        when {
            scale <= 1 -> temScale = 1f
            scale <= 1.5 -> temScale = 1.5f
            scale <= 2 -> temScale = 2f
            scale <= 3 -> temScale = 3f
        }
        return temScale
    }
}
