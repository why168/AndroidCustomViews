package io.github.why168

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import io.github.why168.example.*
import io.github.why168.extension.startActivity

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

        gradientRound.setOnClickListener {
            startActivity(GradientRoundActivity::class.java)
        }
    }

}
