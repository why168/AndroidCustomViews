package io.github.why168.view.floatball

import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager


object FloatBallUtil {
    var inSingleActivity: Boolean = false

    fun getLayoutParams(context: Context?, listenBackEvent: Boolean = false): WindowManager.LayoutParams {
        val layoutParams = WindowManager.LayoutParams()

        layoutParams.flags = (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)

        if (listenBackEvent) {
            layoutParams.flags = layoutParams.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv()
        }

        if (context == null || context !is Activity) {
            val sdkInt = Build.VERSION.SDK_INT
            if (sdkInt < Build.VERSION_CODES.KITKAT) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
            } else if (sdkInt < Build.VERSION_CODES.N_MR1) {
                if ("Xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)) {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
                } else {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST
                }
            } else if (sdkInt < Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
            } else {//8.0以后
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            }
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION
        }

        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.gravity = Gravity.TOP or Gravity.LEFT or Gravity.START
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        return layoutParams
    }

    fun getStatusBarLayoutParams(context: Context?): WindowManager.LayoutParams {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.width = 0
        layoutParams.height = 0
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        layoutParams.gravity = Gravity.LEFT or Gravity.START or Gravity.TOP

        if (context == null || context !is Activity) {
            val sdkInt = Build.VERSION.SDK_INT

            if (sdkInt < Build.VERSION_CODES.KITKAT) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
            } else if (sdkInt < Build.VERSION_CODES.N_MR1) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST
            } else if (sdkInt < Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
            } else  {//8.0以后
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                }
            }
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION
        }
        return layoutParams
    }
}
