package io.github.why168.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View

/**
 *
 *
 * @param normalPinDrawableRes 默认图案
 * @param highlightPinDrawableRes 按下图案
 * @param view
 * @param pinWidth 宽度
 * @param pinHeight 高度
 * @param pinSize PIN长度
 * @param space 间距
 */
class PinPainter @JvmOverloads constructor(normalPinDrawableRes: Drawable,
                                           highlightPinDrawableRes: Drawable,
                                           private val view: PinEditText,
                                           private val pinWidth: Float,
                                           private val pinHeight: Float,
                                           private val pinSize: Int = 6,
                                           private val space: Float = 10f) {

    private val paint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        isDither = true
    }

    private val normalPinDrawables: Bitmap
    private val highlightPinDrawables: Bitmap

    init {
        require(pinWidth > 0) {
            "pinWidth must greater than 0"
        }

        require(pinHeight > 0) {
            "pinHeight must greater than 0"
        }

        require(space >= 0) {
            "space must greater than or equal to 0"
        }

        require(pinSize > 0) {
            "pinSize must greater than 0"
        }

        normalPinDrawables = normalPinDrawableRes.toBitmap(pinWidth.toInt(), pinHeight.toInt())
        highlightPinDrawables = highlightPinDrawableRes.toBitmap(pinWidth.toInt(), pinHeight.toInt())
    }

    fun getCalculatedMeasureSpecSize(): MeasureSpecSize {
        val totalWidthPinSize = pinWidth * pinSize
        val totalSpaceHorizontalSize = space * (pinSize - 1)

        val newWidth = view.paddingStartCompat + view.paddingEndCompat + totalWidthPinSize + totalSpaceHorizontalSize
        val newHeight = view.paddingTop + pinHeight + view.paddingBottom
        val newMeasureSpecWidth = View.MeasureSpec.makeMeasureSpec(newWidth.toInt(), View.MeasureSpec.EXACTLY)
        val newMeasureSpecHeight = View.MeasureSpec.makeMeasureSpec(newHeight.toInt(), View.MeasureSpec.EXACTLY)

        return MeasureSpecSize(newMeasureSpecWidth, newMeasureSpecHeight)
    }

    fun draw(canvas: Canvas) {
        val paddingTop: Float = view.paddingTop.toFloat()
        val paddingStart: Float = view.paddingStartCompat.toFloat()
        val canvasClipBondsTop: Int = canvas.clipBounds.top

        for (i in 0 until pinSize) {
            val top: Float = paddingTop + canvasClipBondsTop
            val left: Float = paddingStart + (space * i) + (pinWidth * i)
            val pinDrawable = if (hasTextInThisPosition(i)) {
                highlightPinDrawables
            } else {
                normalPinDrawables
            }
            canvas.drawBitmap(pinDrawable, left, top, paint)
        }
    }


    /**
     * 这个位置有文字没有
     *
     * @param position
     */
    private fun hasTextInThisPosition(position: Int) = view.text.getOrNull(position) != null

    /**
     * 制作Bitmap
     */
    private fun Drawable.toBitmap(width: Int = -1, height: Int = -1): Bitmap {
        if (this is BitmapDrawable) {
            return bitmap
        }

        val bitmapWidth = if (width < 0) intrinsicWidth else width
        val bitmapHeight = if (height < 0) intrinsicHeight else height

        val bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)

        return bitmap
    }


    // paddingLeft
    private val View.paddingStartCompat: Int
        get() = if (Build.VERSION.SDK_INT >= 17) paddingStart else paddingLeft

    // paddingRight
    private val View.paddingEndCompat: Int
        get() = if (Build.VERSION.SDK_INT >= 17) paddingEnd else paddingRight


    data class MeasureSpecSize(val widthMeasureSpec: Int, val heightMeasureSpec: Int)

}