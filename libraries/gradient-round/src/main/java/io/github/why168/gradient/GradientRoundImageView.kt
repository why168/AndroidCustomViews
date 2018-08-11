package io.github.why168.gradient

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import io.github.why168.common.dp2px

/**
 *
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/26 下午12:24
 * @since JDK1.8
 */
class GradientRoundImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPatin: Paint = Paint().apply {
        isAntiAlias = true // 抗锯齿
        flags = Paint.ANTI_ALIAS_FLAG // 帮助消除锯齿
    }


    private var defaultBigRadiusMax = dp2px(185F) // 740
    private var defaultBigRadiusMin = dp2px(135F) // 540

    private var defaultSmallRadiusMax = dp2px(155F) // 620
    private var defaultSmallRadiusMin = dp2px(95F) // 380

    private var bigRadius = defaultBigRadiusMax
    private var smallRadius = defaultSmallRadiusMin

    private var proportion: Float = 0F

    /**
     * 设置大圆的比例0-1
     */
    public fun setBigRadius(proportion: Float) {
        this.proportion = proportion
        this.bigRadius = defaultBigRadiusMax - (defaultBigRadiusMax - defaultBigRadiusMin) * proportion
    }

    /**
     * 设置小圆的比例0-1
     */
    public fun setSmallRadius(proportion: Float) {
        this.proportion = proportion
        this.smallRadius = defaultSmallRadiusMax - (defaultSmallRadiusMax - defaultSmallRadiusMin) * (1 - proportion)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //半径 = 宽/2-圆环的宽度

        val colorStart1 = ContextCompat.getColor(context, R.color.colorGradient11)
        val colorEnd1 = ContextCompat.getColor(context, R.color.colorGradient12)


        val colorStart2 = ContextCompat.getColor(context, R.color.colorGradient21)
        val colorEnd2 = ContextCompat.getColor(context, R.color.colorGradient22)


        // 渐变过渡色 设置  proportion 设值0-1
        val evaluate1 = ArgbEvaluator().evaluate(proportion, colorStart1, colorStart2) as Int
        val evaluate2 = ArgbEvaluator().evaluate(proportion, colorEnd1, colorEnd2) as Int

        val w = resources.displayMetrics.widthPixels

        // 小圆
        mPatin.shader = LinearGradient(w - dp2px(63F) * 2F, 0F,
                w.toFloat(), 0F,
                intArrayOf(evaluate1, evaluate2),
                null,
                Shader.TileMode.CLAMP)
        canvas?.drawCircle(w - dp2px(63F), 0F, smallRadius, mPatin)

        // 大圆
        mPatin.shader = LinearGradient(-dp2px(80F) * 2F, 0F,
                dp2px(180F), 0F,
                intArrayOf(evaluate1, evaluate2),
                null,
                Shader.TileMode.CLAMP)
        canvas?.drawCircle(dp2px(80F), 0F, bigRadius, mPatin)
    }
}