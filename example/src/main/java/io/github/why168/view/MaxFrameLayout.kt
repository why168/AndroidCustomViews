package io.github.why168.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

/**
 *
 * 最大高度Layout
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/21 下午5:46
 * @since JDK1.8
 */
class MaxFrameLayout
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    public var mMaxWidth: Int = -1
    public var mMaxHeight: Int = -1

    init {
        context.obtainStyledAttributes(attrs, R.styleable.MaxFrameLayout, defStyleAttr, 0).apply {
            mMaxWidth = getDimension(R.styleable.MaxFrameLayout_attrMaxWidth, -1f).toInt()
            mMaxHeight = getDimension(R.styleable.MaxFrameLayout_attrMaxHeight, -1f).toInt()
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var maxWidthMeasureSpec: Int = widthMeasureSpec
        var maxHeightMeasureSpec: Int = heightMeasureSpec

        if (mMaxWidth != -1) {
            val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
            var widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
            widthSize = if (widthSize <= mMaxWidth) widthSize else mMaxWidth
            maxWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(widthSize, widthMode)
        }

        if (mMaxHeight != -1) {
            val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
            var heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
            heightSize = if (heightSize <= mMaxHeight) heightSize else mMaxHeight
            maxHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode)
        }

        super.onMeasure(maxWidthMeasureSpec, maxHeightMeasureSpec)
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}