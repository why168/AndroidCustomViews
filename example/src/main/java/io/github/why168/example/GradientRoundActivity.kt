package io.github.why168.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import io.github.why168.R
import kotlinx.android.synthetic.main.activity_gradient_round.*

class GradientRoundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gradient_round)

        initView()
    }

    private fun initView() {

        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                /*
                if (progress == 0 || progress == 1) {
                    mRoundView.setBigRadius(0F)
                    mRoundView.setSmallRadius(1F)
                } else {
                    mRoundView.setBigRadius(progress * 0.1F)
                    mRoundView.setSmallRadius(progress * 0.1F)
                }
                */
                val value = progress * 0.1F
                mRoundView.setBigRadius(value)
                mRoundView.setSmallRadius(value)
                Log.e("TAG", "progress = $value")

                if (progress == 10) {

                } else {

                }

//                mRoundView.requestLayout()
                mRoundView.invalidate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

    }
}
