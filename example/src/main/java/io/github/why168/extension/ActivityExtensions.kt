package io.github.why168.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity

/**
 *
 * Activity扩展
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/12 下午10:32
 * @since JDK1.8
 */
fun Activity.startActivity(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}
