package io.github.why168.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import java.util.*
import android.annotation.SuppressLint
import android.text.SpannableString


/**
 *
 * 仿打字机的效果
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/9 下午12:23
 * @since JDK1.8
 */
class PrinterTextView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {
    /**
     * 默认打字字符
     */
    private var DEFAULT_INTERVAL_CHAR = "_"
    /**
     * 默认打字间隔时间
     */
    private var DEFAULT_TIME_DELAY = 80L
    /**
     * 计时器
     */
    private var mTimer: Timer? = null

    /**
     * 需要打字的文字
     * String
     */
    private var mPrintStr1: String = ""

    /**
     * 需要打字的文字
     * SpannableString
     */
    private var mPrintStr2: SpannableString? = null

    /**
     * 间隔时间
     */
    private var intervalTime: Long = DEFAULT_TIME_DELAY

    /**
     * 间隔文字
     */
    private var intervalChar = DEFAULT_INTERVAL_CHAR

    /**
     * 打字进度
     */
    private var printProgress = 0


    init {

    }

    /**
     * 设置要打字的文字
     *
     * @param str
     */
    fun setPrintText(str: String, onPrinterTimeTask: OnPrinterTimeTask? = null) {
        setPrintText(str, DEFAULT_TIME_DELAY, onPrinterTimeTask)
    }

    fun setPrintText(str: SpannableString, onPrinterTimeTask: OnPrinterTimeTask? = null) {
        setPrintText(str, DEFAULT_TIME_DELAY, onPrinterTimeTask)
    }

    /**
     * 设置需要打字的文字及打字间隔
     *
     * @param str  打字文字
     * @param time 打字间隔(ms)
     */
    fun setPrintText(str: String, time: Long, onPrinterTimeTask: OnPrinterTimeTask? = null) {
        setPrintText(str, time, DEFAULT_INTERVAL_CHAR, onPrinterTimeTask)
    }

    fun setPrintText(str: SpannableString, time: Long, onPrinterTimeTask: OnPrinterTimeTask? = null) {
        setPrintText(str, time, DEFAULT_INTERVAL_CHAR, onPrinterTimeTask)
    }

    /**
     * 设置需要打字的文字,打字间隔,间隔符号
     *
     * @param str          打字文字
     * @param time         打字间隔(ms)
     * @param intervalChar 间隔符号("_")
     */
    fun setPrintText(str: String, time: Long, intervalChar: String, onPrinterTimeTask: OnPrinterTimeTask? = null) {
        if (str.isEmpty() || 0L == time || intervalChar.isEmpty()) {
            return
        }
        this.mPrintStr1 = str
        this.intervalTime = time
        this.intervalChar = intervalChar
        this.onPrinterTimeTask = onPrinterTimeTask
    }

    fun setPrintText(str: SpannableString, time: Long, intervalChar: String, onPrinterTimeTask: OnPrinterTimeTask? = null) {
        if (str.isEmpty() || 0L == time || intervalChar.isEmpty()) {
            return
        }
        this.mPrintStr2 = str
        this.intervalTime = time
        this.intervalChar = intervalChar
        this.onPrinterTimeTask = onPrinterTimeTask
    }

    /**
     * 开始打字
     * String
     */
    fun startPrintByString() {
        // 判空处理
        if (mPrintStr1.isEmpty()) {
            if (!text.toString().isEmpty()) {
                this.mPrintStr1 = text.toString()
            } else {
                return
            }
        }

        // 重置相关信息
        text = ""
        stopPrint()
        printProgress = 0
        mTimer = Timer()
        mTimer?.schedule(PrinterTimeTaskByString(), intervalTime, intervalTime)
    }

    /**
     * 开始打字
     * SpannableString
     */
    fun startPrintBySpannableString() {
        // 判空处理
        if (mPrintStr2.isNullOrEmpty()) {
            if (!text.toString().isEmpty()) {
                this.mPrintStr1 = text.toString()
            } else {
                return
            }
        }

        // 重置相关信息
        text = ""
        stopPrint()
        printProgress = 0
        mTimer = Timer()
        mTimer?.schedule(PrinterTimeTaskBySpannableString(), intervalTime, intervalTime)
    }

    /**
     * 停止打字
     */
    fun stopPrint() {
        mTimer?.cancel()
        mTimer = null
    }

    /**
     * 打字计时器任务
     * String
     */
    internal inner class PrinterTimeTaskByString : TimerTask() {
        @SuppressLint("SetTextI18n")
        override fun run() {
            // 需要刷新页面,必须在UI线程,使用post方法
            post {
                // 如果未显示完,继续显示
                if (printProgress < mPrintStr1.length) {
                    printProgress++
                    // (printProgress & 1) == 1 等价于printProgress%2!=0
                    val showText = mPrintStr1.substring(0, printProgress)
//                    val defaultChar = if ((printProgress and 1) == 1) intervalChar else ""
                    val defaultChar = if (printProgress % 2 != 0) intervalChar else ""
                    text = showText + defaultChar
                } else {
                    // 如果完成打字,显示完整文字
                    text = mPrintStr1
                    stopPrint()
                }
            }
        }
    }

    /**
     * 打字计时器任务
     * SpannableString
     */
    internal inner class PrinterTimeTaskBySpannableString : TimerTask() {
        @SuppressLint("SetTextI18n")
        override fun run() {
            // 需要刷新页面,必须在UI线程,使用post方法
            post {
                // 如果未显示完,继续显示
                if (printProgress < mPrintStr2!!.length) {
                    printProgress++
                    // (printProgress & 1) == 1 等价于printProgress%2!=0
                    val showText = mPrintStr2!!.substring(0, printProgress)
//                    val defaultChar = if ((printProgress and 1) == 1) intervalChar else ""
                    val defaultChar = if (printProgress % 2 != 0) intervalChar else ""
                    text = showText + defaultChar
                } else {
                    // 如果完成打字,显示完整文字
                    text = mPrintStr2
                    stopPrint()
                    onPrinterTimeTask?.completed()
                }
            }
        }
    }

    var onPrinterTimeTask: OnPrinterTimeTask? = null

    interface OnPrinterTimeTask {
        fun completed()
    }

}