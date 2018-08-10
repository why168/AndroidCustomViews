package io.github.why168.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import io.github.why168.dp2px


/**
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/10 下午7:03
 * @since JDK1.8
 */
class ShaderProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var maxCount = 100f
    private var progress = 0f
    private var borderStroke: Float = 0F // 外描边的宽度
    private var progressStroke: Float = 0F // 进度条进度矩形与控件边界的距离,≥borderStroke
    private val gradientColorsBg = intArrayOf(Color.parseColor("#E8BE44"), Color.parseColor("#FE9445"))
    private var mRectF: RectF = RectF()
    private val path = Path()
    private val targetRect = Rect()
    private val rectF = RectF()
    private var mPaint: Paint = Paint().apply {
        isAntiAlias = true // 抗锯齿
        flags = Paint.ANTI_ALIAS_FLAG // 帮助消除锯齿
    }


    init {
        borderStroke = dp2px(1F)
        progressStroke = dp2px(1F)
    }


    /**
     * 测量得到进度条的宽高
     */
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec)
//        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
//        val heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec)
//        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)
//        if (widthSpecMode == View.MeasureSpec.EXACTLY || widthSpecMode == View.MeasureSpec.AT_MOST) {
//            width = widthSpecSize
//        } else {
//            width = 0
//        }
//        if (heightSpecMode == View.MeasureSpec.AT_MOST || heightSpecMode == View.MeasureSpec.UNSPECIFIED) {
//            height = 20
//        } else {
//            height = heightSpecSize
//        }
//        setMeasuredDimension(width, height)
//    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val section = progress / maxCount

//        val round = height / 3//弧度为高度的一半
        val round = dp2px(20F)

        mRectF.set(0f, 0f, width.toFloat(), height.toFloat())//第一层矩形(描边层)
        mPaint.color = Color.parseColor("#E6E6E6")//第一层矩形颜色(进度条描边的颜色)

        canvas.drawRoundRect(mRectF, round, round, mPaint)//画第一层圆角矩形

        mPaint.color = Color.parseColor("#FFFFFF")//第二层矩形颜色(背景层颜色)
        mRectF.set(borderStroke, borderStroke, width - borderStroke, height - borderStroke)//第二层矩形(背景层)
        canvas.drawRoundRect(mRectF, round, round, mPaint)//画背景层圆角矩形(盖在描边层之上)

        //进度为 0不画进度
        if (progress == 0f) {
            return
        } else {

            //第三层矩形(进度层)
            mRectF.set(progressStroke, progressStroke,
                    (width - progressStroke) * section,
                    height - progressStroke)


            mPaint.reset()
            mPaint.apply {
                isAntiAlias = true // 抗锯齿
                flags = Paint.ANTI_ALIAS_FLAG // 帮助消除锯齿
                shader = LinearGradient(progressStroke, progressStroke,
                        (width - progressStroke) * section,
                        height - progressStroke,
                        gradientColorsBg, null, Shader.TileMode.MIRROR) // 第三层矩形颜色(进度渐变色)
            }


            // 裁剪
            rectF.set(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat())
            path.addRoundRect(rectF, 600F, 600F, Path.Direction.CW)
            canvas.clipPath(path)
            // 画第四层(进度层)圆角矩形(盖在背景层之上)
            canvas.drawRoundRect(mRectF, round, round, mPaint)
        }


        // 画字体
        targetRect.set(0, 0, width, height)//第一层矩形(描边层)
        mPaint.reset()
        mPaint.apply {
            isAntiAlias = true // 抗锯齿
            flags = Paint.ANTI_ALIAS_FLAG // 帮助消除锯齿
            strokeWidth = 3F
            textSize = 35F
            color = Color.BLACK
        }

        val fontMetrics = mPaint.fontMetricsInt
        val baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2
        mPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("$progress%", targetRect.centerX().toFloat(), baseline.toFloat(), mPaint)

        mPaint.reset()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
    }

    /***
     * 设置当前进度
     *
     * @param currentCount
     */
    fun setProgress(currentCount: Float) {
        this.progress = if (currentCount > maxCount) maxCount else currentCount
        postInvalidate()
    }
}
