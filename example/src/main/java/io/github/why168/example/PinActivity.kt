package io.github.why168.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import io.github.why168.R
import kotlinx.android.synthetic.main.activity_pin.*

/**
 *
 * Pin码
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/12 下午10:24
 * @since JDK1.8
 */
class PinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        mPinEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s.toString().length
                if (length == 6) {
                    mPinEditText.errorAnimation()
                }
            }

        })
    }
}
