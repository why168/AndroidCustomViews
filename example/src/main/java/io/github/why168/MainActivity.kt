package io.github.why168

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import io.github.why168.R
import io.github.why168.example.FloatBallActivity
import io.github.why168.example.PinActivity
import io.github.why168.example.PrintActivity
import io.github.why168.example.ProgressActivity
import io.github.why168.extension.startActivity
import kotlinx.android.synthetic.main.activity_print.*
import kotlin.reflect.KClass

/**
 * Main界面
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/12 下午10:27
 * @since JDK1.8
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subtitleDisplay.setOnClickListener {
            startActivity(PrintActivity::class.java)
        }

        pinCodeDisplay.setOnClickListener {
            startActivity(PinActivity::class.java)
        }

        progressDisplay.setOnClickListener {
            startActivity(ProgressActivity::class.java)
        }

        floatBall.setOnClickListener {
            startActivity(FloatBallActivity::class.java)
        }
    }

}
