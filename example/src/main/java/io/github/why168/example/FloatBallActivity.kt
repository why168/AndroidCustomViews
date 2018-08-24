package io.github.why168.example

import android.animation.ValueAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Toast
import io.github.why168.R
import io.github.why168.common.dp2px
import io.github.why168.view.floatball.FloatBallConfig
import io.github.why168.view.floatball.FloatBallManager
import io.github.why168.view.floatball.FloatMenuCfg
import kotlinx.android.synthetic.main.activity_float_ball.*

/**
 * 悬浮框
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/7/25 下午5:36
 * @since JDK1.8
 */
class FloatBallActivity : AppCompatActivity() {
    private var mFloatBallManager: FloatBallManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_float_ball)

        initSinglePageFloatBall(true)

        clickFloatBall.setOnClickListener {
            mFloatBallManager?.showTipView(" 震惊！恋养猫还能这样搜索... ")


            val valueAnimator = ValueAnimator.ofInt(0, mFrameLayout.width)
            valueAnimator.duration = 1000
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener { animator ->
                val currentValue = animator.animatedValue as Int
//                println("currentValue = $currentValue")

                val layoutParams = mFrameLayout.layoutParams
                layoutParams.width = currentValue
                mFrameLayout.layoutParams = layoutParams
                mFrameLayout.postInvalidate()
            }

            valueAnimator.start()


        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //只有activity被添加到windowmanager上以后才可以调用show方法。
        mFloatBallManager?.show()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mFloatBallManager?.hide()
    }


    private fun exitFullScreen() {
        val attrs = window.attributes
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        window.attributes = attrs
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        isfull = false
    }

    private var isfull = false

    fun setFullScreen(view: View) {
        if (isfull == true) {
            exitFullScreen()
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            isfull = true
        }
    }

    /**
     * 是否显示菜单
     *
     * @param showMenu 显示菜单
     */
    private fun initSinglePageFloatBall(showMenu: Boolean) {
        val floatBallConfig = FloatBallConfig(
                size = dp2px(45F).toInt(),
                icon = R.drawable.dappp_menu_logo,
                gravity = FloatBallConfig.Gravity.RIGHT_CENTER)

        // 设置悬浮球不半隐藏
        floatBallConfig.mHideHalfLater = false

        if (showMenu) {
            //2 需要显示悬浮菜单
            //2.1 初始化悬浮菜单配置，有菜单item的大小和菜单item的个数
            val menuSize = dp2px(180F).toInt()
            val menuItemSize = dp2px(40F).toInt()
            val menuCfg = FloatMenuCfg(menuSize, menuItemSize)
            //3 生成floatballManager
            //必须传入Activity
            mFloatBallManager = FloatBallManager(this, floatBallConfig, menuCfg) {
                Toast.makeText(this, "标签", Toast.LENGTH_SHORT).show()
                true
            }
            addFloatMenuItem()
        } else {
            //必须传入Activity
            mFloatBallManager = FloatBallManager(this, floatBallConfig)
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun addFloatMenuItem() {
        val personItem = object : io.github.why168.view.floatball.MenuItem(ContextCompat.getDrawable(this, R.drawable.dappp_menu_strategy)!!) {
            override fun action() {
                toast("攻略")
                mFloatBallManager?.closeMenu()
            }
        }
        val walletItem = object : io.github.why168.view.floatball.MenuItem(ContextCompat.getDrawable(this, R.drawable.dappp_menu_translation)!!) {
            override fun action() {
                toast("翻译")
            }
        }
        val settingItem = object : io.github.why168.view.floatball.MenuItem(ContextCompat.getDrawable(this, R.drawable.dappp_menu_wallet)!!) {
            override fun action() {
                toast("钱包")
                mFloatBallManager?.closeMenu()
            }

        }

        mFloatBallManager
                ?.addMenuItem(personItem)
                ?.addMenuItem(walletItem)
                ?.addMenuItem(settingItem)
                ?.buildMenu()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
