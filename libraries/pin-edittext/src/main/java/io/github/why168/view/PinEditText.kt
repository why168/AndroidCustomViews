package io.github.why168.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.TextView
import com.dappp.play.pin.R

/**
 *
 * 自定义PIN码输入框
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/3 下午4:02
 * @since JDK1.8
 */
class PinEditText @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.support.v7.appcompat.R.attr.editTextStyle) : AppCompatEditText(context, attrs, defStyleAttr) {

    companion object {
        private const val XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"

        private const val DEFAULT_PIN_WIDTH = 25
        private const val DEFAULT_PIN_HEIGHT = 25
        private const val DEFAULT_PIN_TOTAL = 6
        private const val DEFAULT_PIN_SPACE = 12
        private const val DEFAULT_PIN_MAX_LINES = 1

    }

    private var pinPainter: PinPainter
    private var onClickListener: View.OnClickListener? = null
    private var onEditorActionListener: TextView.OnEditorActionListener? = null


    init {
        isCursorVisible = false
        isLongClickable = false
        customSelectionActionModeCallback = ActionModeCallbackInterceptor()
        maxLines = DEFAULT_PIN_MAX_LINES
        setBackgroundColor(Color.TRANSPARENT)

        initClickListener()
        initOnEditorActionListener()

        var normalStateDrawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.pin_default_normal_state)
        var highlightStateDrawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.pin_default_highlight_state)
        var pinWidth = context.dpToPx(DEFAULT_PIN_WIDTH)
        var pinHeight = context.dpToPx(DEFAULT_PIN_HEIGHT)
        var pinTotal = DEFAULT_PIN_TOTAL
        var pinSpace = context.dpToPx(DEFAULT_PIN_SPACE)

        context.obtainStyledAttributes(attrs, R.styleable.PinEditText, defStyleAttr, 0).apply {
            pinWidth = getDimension(R.styleable.PinEditText_pinWidth, pinWidth)
            pinHeight = getDimension(R.styleable.PinEditText_pinHeight, pinHeight)
            pinSpace = getDimension(R.styleable.PinEditText_pinSpace, pinSpace)

            getDrawable(R.styleable.PinEditText_pinNormalStateDrawable)?.let {
                normalStateDrawable = it
            }
            getDrawable(R.styleable.PinEditText_pinHighlightStateDrawable)?.let {
                highlightStateDrawable = it
            }
            recycle()
        }

        pinTotal = getTextViewMaxLength(attrs!!, pinTotal)

        require(normalStateDrawable != null) {
            "normalStateDrawable must not be null"
        }

        require(highlightStateDrawable != null) {
            "highlightStateDrawable must not be null"
        }

        pinPainter = PinPainter(
                normalStateDrawable!!,
                highlightStateDrawable!!,
                this,
                pinWidth,
                pinHeight,
                pinTotal,
                pinSpace)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            setSelection(text.length)
        }
    }

    override fun setOnClickListener(onClickListener: View.OnClickListener?) {
        this.onClickListener = onClickListener
    }

    override fun setOnEditorActionListener(onEditorActionListener: TextView.OnEditorActionListener) {
        this.onEditorActionListener = onEditorActionListener
    }

    fun canPaste(): Boolean = false

    override fun isSuggestionsEnabled(): Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val (newWidthMeasureSpec, newHeightMeasureSpec) = pinPainter.getCalculatedMeasureSpecSize()

        setMeasuredDimension(newWidthMeasureSpec, newHeightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        println("onDraw")
        pinPainter.draw(canvas)
    }


    private fun initClickListener() {
        super.setOnClickListener { view ->
            setSelection(text.length)
            onClickListener?.onClick(view)
        }
    }

    private fun initOnEditorActionListener() {
        super.setOnEditorActionListener { view, actionId, event ->
            onEditorActionListener?.onEditorAction(view, actionId, event) ?: false
        }
    }

    private fun getTextViewMaxLength(attrs: AttributeSet, defaultMaxLength: Int): Int {
        return attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", defaultMaxLength)
    }

    private fun Context.dpToPx(dp: Int) = dp * resources.displayMetrics.density

    private class ActionModeCallbackInterceptor : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean = false
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean = false
        override fun onDestroyActionMode(mode: ActionMode) {}
    }


    /**
     * 错误动画
     */
    fun errorAnimation(onAnimation: OnAnimation? = null) {
        this.onAnimation = onAnimation
        clearAnimation()

        val translateAnimation = TranslateAnimation(0F, 15F, 0F, 0F)
        translateAnimation.duration = 600
        translateAnimation.interpolator = CycleInterpolator(3F)
        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                onAnimation?.repeat()
            }

            override fun onAnimationEnd(animation: Animation?) {
                setText("")
                onAnimation?.end()
            }

            override fun onAnimationStart(animation: Animation?) {
                onAnimation?.start()
            }

        })
        startAnimation(translateAnimation)
    }

    private var onAnimation: OnAnimation? = null

    interface OnAnimation {
        fun start()
        fun end()
        fun repeat()
    }
}
