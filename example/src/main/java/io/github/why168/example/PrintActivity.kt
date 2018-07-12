package io.github.why168.example

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import io.github.why168.R
import kotlinx.android.synthetic.main.activity_print.*

class PrintActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print)


        printButton.setOnClickListener {
            val text = "草原上有对狮子母子。小狮子问母狮子：“妈，幸福在哪里?”母狮子说：“幸福就在你的尾巴上。”\n" +
                    "　　于是小狮子不断追着尾巴跑，但始终咬不到。母狮子笑道：“傻瓜!幸福不是这样得到的!只要你昂首向前走，幸福就会一直跟随着你!”。"

            mPrinterTextView1.setPrintText(text)
            mPrinterTextView1.startPrintByString()

            val spanString = SpannableString(text)
            val span1 = BackgroundColorSpan(Color.YELLOW)
            spanString.setSpan(span1, 10, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            val span2 = ForegroundColorSpan(Color.BLUE)
            spanString.setSpan(span2, 20, 27, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            mPrinterTextView2.setPrintText(spanString)
            mPrinterTextView2.startPrintBySpannableString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPrinterTextView1.stopPrint()
        mPrinterTextView2.stopPrint()
    }
}
