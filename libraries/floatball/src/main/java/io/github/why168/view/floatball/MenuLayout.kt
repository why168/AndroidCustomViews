package io.github.why168.view.floatball

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

import io.github.why168.view.floatball.runner.ICarrier
import io.github.why168.view.floatball.runner.ScrollRunner
import io.github.why168.view.floatball.utlis.DensityUtil


/**
 *
 * 子菜单项布局
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/25 下午7:47
 * @since JDK1.8
 */
class MenuLayout
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ViewGroup(context, attrs, defStyleAttr), ICarrier {

    companion object {
        private var MIN_RADIUS: Int = 0

        /**
         * 计算半径
         */
        private fun computeRadius(arcDegrees: Float, childCount: Int, childSize: Int, childPadding: Int, minRadius: Int): Int {
            if (childCount < 2) {
                return minRadius
            }
            //        final float perDegrees = arcDegrees / (childCount - 1);
            val perDegrees = if (arcDegrees == 360f) arcDegrees / childCount else arcDegrees / (childCount - 1)
            val perHalfDegrees = perDegrees / 2
            val perSize = childSize + childPadding
            val radius = (perSize / 2 / Math.sin(Math.toRadians(perHalfDegrees.toDouble()))).toInt()
            return Math.max(radius, minRadius)
        }

        /**
         * 计算子菜单项的范围
         */
        private fun computeChildFrame(centerX: Int, centerY: Int, radius: Int, degrees: Float, size: Int): Rect {
            //子菜单项中心点
            val childCenterX = centerX + radius * Math.cos(Math.toRadians(degrees.toDouble()))
            val childCenterY = centerY + radius * Math.sin(Math.toRadians(degrees.toDouble()))
            //子菜单项的左上角，右上角，左下角，右下角
            return Rect((childCenterX - size / 2).toInt(),
                    (childCenterY - size / 2).toInt(), (childCenterX + size / 2).toInt(), (childCenterY + size / 2).toInt())
        }
    }

    /**
     * 设定子菜单项大小
     */
    var childSize: Int = 0
    private val mChildPadding = 5
    private var mFromDegrees: Float = 0.toFloat()
    private var mToDegrees: Float = 0.toFloat()
    private var mRadius: Int = 0// 中心菜单圆点到子菜单中心的距离

    var isExpanded = false
        private set

    var isMoving = false
        private set

    private var position = FloatMenu.LEFT_TOP
    private var centerX = 0
    private var centerY = 0
    private var mRunner: ScrollRunner? = null

    private val radiusAndPadding: Int
        get() = mRadius + mChildPadding * 2

    private val layoutSize: Int
        get() {
            mRadius = computeRadius(Math.abs(mToDegrees - mFromDegrees), childCount,
                    childSize, mChildPadding, MIN_RADIUS)
            val layoutPadding = 10
            return mRadius * 2 + childSize + mChildPadding + layoutPadding * 2
        }

    private val isLeft: Boolean
        get() {
            val corner = (mFromDegrees / 90).toInt()
            return if (corner == 0 || corner == 3) true else false
        }


    init {
        MIN_RADIUS = DensityUtil.dip2px(context, 50F)
        mRunner = ScrollRunner(this, context)
        isChildrenDrawingOrderEnabled = true
    }

    fun computeCenterXY(position: Int) {
        val size = layoutSize
        when (position) {
            FloatMenu.LEFT_TOP//左上
            -> {
                centerX = size / 2 - radiusAndPadding
                centerY = size / 2 - radiusAndPadding
            }
            FloatMenu.LEFT_CENTER//左中
            -> {
                centerX = size / 2 - radiusAndPadding
                centerY = size / 2
            }
            FloatMenu.LEFT_BOTTOM//左下
            -> {
                centerX = size / 2 - radiusAndPadding
                centerY = size / 2 + radiusAndPadding
            }
            FloatMenu.CENTER_TOP//上中
            -> {
                centerX = size / 2
                centerY = size / 2 - radiusAndPadding
            }
            FloatMenu.CENTER_BOTTOM//下中
            -> {
                centerX = size / 2
                centerY = size / 2 + radiusAndPadding
            }
            FloatMenu.RIGHT_TOP//右上
            -> {
                centerX = size / 2 + radiusAndPadding
                centerY = size / 2 - radiusAndPadding
            }
            FloatMenu.RIGHT_CENTER//右中
            -> {
                centerX = size / 2 + radiusAndPadding
                centerY = size / 2
            }
            FloatMenu.RIGHT_BOTTOM//右下
            -> {
                centerX = size / 2 + radiusAndPadding
                centerY = size / 2 + radiusAndPadding
            }

            FloatMenu.CENTER -> {
                centerX = size / 2
                centerY = size / 2
            }
        }
    }

    /**
     * 子菜单项大小
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = layoutSize
        setMeasuredDimension(size, size)
        val count = childCount
        for (i in 0 until count) {
            getChildAt(i).measure(View.MeasureSpec.makeMeasureSpec(childSize, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(childSize, View.MeasureSpec.EXACTLY))
        }
    }

    /**
     * 子菜单项位置
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (isMoving) return
        computeCenterXY(position)
        val radius = 0
        layoutItem(radius)
    }

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        //当悬浮球在右侧时，使其菜单从上到下的顺序和在左边时一样。
        return if (!isLeft) {
            childCount - i - 1
        } else i
    }

    private fun layoutItem(radius: Int) {
        val childCount = childCount
        //        final float perDegrees =Math.abs (mToDegrees - mFromDegrees) / (childCount - 1);
        val perDegrees: Float
        var degrees = mFromDegrees
        val arcDegrees = Math.abs(mToDegrees - mFromDegrees)
        if (childCount == 1) {
            perDegrees = arcDegrees / (childCount + 1)
            degrees += perDegrees
        } else if (childCount == 2) {
            if (arcDegrees == 90f) {
                perDegrees = arcDegrees / (childCount - 1)
            } else {
                perDegrees = arcDegrees / (childCount + 1)
                degrees += perDegrees
            }
        } else {
            perDegrees = if (arcDegrees == 360f) arcDegrees / childCount else arcDegrees / (childCount - 1)
        }
        for (i in 0 until childCount) {
            val index = getChildDrawingOrder(childCount, i)
            val frame = computeChildFrame(centerX, centerY, radius, degrees, childSize)
            degrees += perDegrees
            getChildAt(index).layout(frame.left, frame.top, frame.right, frame.bottom)
        }
    }

    override fun requestLayout() {
        if (!isMoving) {
            super.requestLayout()
        }
    }

    /**
     * 切换中心按钮的展开缩小
     */
    fun switchState(position: Int, duration: Int) {
        this.position = position
        isExpanded = !isExpanded
        isMoving = true

        mRadius = computeRadius(Math.abs(mToDegrees - mFromDegrees),
                childCount,
                childSize,
                mChildPadding,
                MIN_RADIUS)

        val start = if (isExpanded) 0 else mRadius
        val radius = if (isExpanded) mRadius else -mRadius
        mRunner!!.start(start, 0, radius, 0, duration)
    }

    override fun onMove(lastX: Int, lastY: Int, curX: Int, curY: Int) {
        layoutItem(curX)
    }

    override fun onDone() {
        isMoving = false
        if (!isExpanded) {
            val floatMenu = parent as FloatMenu
            floatMenu.remove()
        }
    }

    /**
     * 设定弧度
     */
    fun setArc(fromDegrees: Float, toDegrees: Float, position: Int) {
        this.position = position
        if (mFromDegrees == fromDegrees && mToDegrees == toDegrees) {
            return
        }
        mFromDegrees = fromDegrees
        mToDegrees = toDegrees
        computeCenterXY(position)
        requestLayout()
    }

    fun setExpand(expand: Boolean) {
        isExpanded = expand
    }
}