package io.github.why168.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import io.github.why168.R
import kotlinx.android.synthetic.main.activity_progress.*

/**
 * 进度条Activity
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/12 下午10:09
 * @since JDK1.8
 */
class ProgressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (seekBar.progress == 0) {
                    seekBar.progress = 1
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }
}
