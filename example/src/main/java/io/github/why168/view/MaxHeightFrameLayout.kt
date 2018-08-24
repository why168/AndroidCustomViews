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
class MaxHeightFrameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var mMaxHeight: Int = 100
//    private var mMaxHeight: Int = dp2px(380f).toInt()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        heightSize = if (heightSize <= mMaxHeight) heightSize else mMaxHeight

        val maxHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode)

//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec)

    }
}