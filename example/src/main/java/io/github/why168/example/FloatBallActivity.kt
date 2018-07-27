package io.github.why168.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import io.github.why168.R
import io.github.why168.view.floatball.FloatBallCfg
import io.github.why168.view.floatball.FloatBallManager
import io.github.why168.view.floatball.FloatMenuCfg
import io.github.why168.view.floatball.utlis.DensityUtil
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

        //5 如果没有添加菜单，可以设置悬浮球点击事件
        if (mFloatBallManager?.menuItemSize == 0) {
            mFloatBallManager?.setOnFloatBallClickListener(object : FloatBallManager.OnFloatBallClickListener {
                override fun onFloatBallClick() {
                    toast("点击了悬浮球")
                }
            })
        }

        clickFloatBall.setOnClickListener {
            mFloatBallManager?.showTipView(" 震惊！恋养猫还能这样搜索... ",
                    object : FloatBallManager.OnTipViewClickListener {
                        override fun onTipViewClick() {
                            toast("提示")
                        }
                    })
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
        //1 初始化悬浮球配置，定义好悬浮球大小和icon的drawable
        val ballSize = DensityUtil.dip2px(this, 45F)
        val ballIcon = ContextCompat.getDrawable(this, R.drawable.dappp_menu_logo)!!
        val ballCfg = FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.RIGHT_CENTER)
        // 设置悬浮球不半隐藏
        ballCfg.setHideHalfLater(false)
        if (showMenu) {
            //2 需要显示悬浮菜单
            //2.1 初始化悬浮菜单配置，有菜单item的大小和菜单item的个数
            val menuSize = DensityUtil.dip2px(this, 180F)
            val menuItemSize = DensityUtil.dip2px(this, 40F)
            val menuCfg = FloatMenuCfg(menuSize, menuItemSize)
            //3 生成floatballManager
            //必须传入Activity
            mFloatBallManager = FloatBallManager(this, ballCfg, menuCfg)
            addFloatMenuItem()
        } else {
            //必须传入Activity
            mFloatBallManager = FloatBallManager(this, ballCfg)
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
