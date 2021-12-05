package io.github.why168.example

import android.animation.ValueAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Toast
import io.github.why168.R
import io.github.why168.common.dp2px
import io.github.why168.view.floatball.FloatBallConfig
import io.github.why168.view.floatball.FloatBallManager
import io.github.why168.view.floatball.FloatMenuConfig
import io.github.why168.view.floatball.menu.MenuItem
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

        initSinglePageFloatBall()

        clickFloatBall.setOnClickListener {
//            mFloatBallManager?.showTipView(" 震惊！恋养猫还能这样搜索... ")

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
    private fun initSinglePageFloatBall() {
        val floatBallConfig = FloatBallConfig(
                size = dp2px(45F).toInt(),
                icon = R.drawable.dappp_menu_logo,
                gravity = FloatBallConfig.Gravity.RIGHT_CENTER)

        // 设置悬浮球不半隐藏
        floatBallConfig.hideHalfLater = false

        //2 需要显示悬浮菜单
        //2.1 初始化悬浮菜单配置，有菜单item的大小和菜单item的个数
        val floatMenuConfig = FloatMenuConfig(dp2px(180F).toInt(), dp2px(40F).toInt())

        //3 生成FloatBallManager
        //必须传入Activity
        mFloatBallManager = FloatBallManager(this, floatBallConfig, floatMenuConfig) {
            Toast.makeText(this, "标签", Toast.LENGTH_SHORT).show()
            true
        }
        addFloatMenuItem()
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private var isTranslation: Boolean = false

    private var items: MutableList<MenuItem> = arrayListOf(
            object : MenuItem(R.drawable.dappp_menu_strategy) {
                override fun setDrawable(): Pair<Boolean, Int> = Pair<Boolean, Int>(false, R.drawable.dappp_menu_strategy)

                override fun action() {
                    toast("攻略")
                    mFloatBallManager?.closeMenu()
                }
            },
            object : MenuItem(R.drawable.dappp_menu_translation) {
                override fun setDrawable(): Pair<Boolean, Int> {

                    return if (isTranslation) {
                        isTranslation = false
                        Pair<Boolean, Int>(true, R.drawable.dappp_menu_translation)
                    } else {
                        isTranslation = true
                        Pair<Boolean, Int>(true, R.drawable.dappp_menu_translation_ok)
                    }
                }

                override fun action() {
                    toast("翻译")
                    mFloatBallManager?.closeMenu()
                    mFloatBallManager?.showTipView( "正在翻译，请稍等...")
                }
            },
            object : MenuItem(R.drawable.dappp_menu_wallet) {
                override fun setDrawable(): Pair<Boolean, Int> = Pair<Boolean, Int>(false, R.drawable.dappp_menu_wallet)
                override fun action() {
                    toast("钱包")
                    mFloatBallManager?.closeMenu()
                }
            }
    )


    private fun addFloatMenuItem() {
        mFloatBallManager?.setMenu(items)?.buildMenu()
    }

}
