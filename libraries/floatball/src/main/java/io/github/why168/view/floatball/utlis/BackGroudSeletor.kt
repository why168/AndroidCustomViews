package io.github.why168.view.floatball.utlis

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.NinePatch
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.graphics.drawable.StateListDrawable

import java.io.IOException
import java.io.InputStream

object BackGroudSeletor {
    internal var PRESSED_ENABLED_STATE_SET = intArrayOf(16842910, 16842919)// debug出的该变量在view中的值
    internal var ENABLED_STATE_SET = intArrayOf(16842910)
    internal var EMPTY_STATE_SET = intArrayOf()

    /**
     * 该方法主要是构建StateListDrawable对象，以StateListDrawable来设置图片状态，来表现View的各中状态：未选中，按下
     * ，选中效果
     *
     * @param imagename 选中和未选中使用的两张图片名称
     * @param context   上下文
     */
    fun createBgByImagedrawble(imagename: Array<String>,
                               context: Context): StateListDrawable {
        val bg = StateListDrawable()
        val normal = getdrawble(imagename[0], context)
        val pressed = getdrawble(imagename[1], context)
        bg.addState(PRESSED_ENABLED_STATE_SET, pressed)
        bg.addState(ENABLED_STATE_SET, normal)
        bg.addState(EMPTY_STATE_SET, normal)
        return bg
    }

    fun createBg(normal: Drawable, pressed: Drawable): StateListDrawable {
        val bg = StateListDrawable()
        bg.addState(PRESSED_ENABLED_STATE_SET, pressed)
        bg.addState(ENABLED_STATE_SET, normal)
        bg.addState(EMPTY_STATE_SET, normal)
        return bg
    }

    fun createColorStateList(normal: Int, pressed: Int): ColorStateList {
        val colors = intArrayOf(pressed, pressed, normal, pressed, pressed, normal)
        val states = arrayOfNulls<IntArray>(6)
        states[0] = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
        states[1] = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused)
        states[2] = intArrayOf(android.R.attr.state_enabled)
        states[3] = intArrayOf(android.R.attr.state_focused)
        states[4] = intArrayOf(android.R.attr.state_window_focused)
        states[5] = intArrayOf()
        return ColorStateList(states, colors)
    }

    /**
     * 该方法主要是构建StateListDrawable对象，以StateListDrawable来设置图片状态，来表现View的各中状态：未选中，按下
     * ，选中效果
     *
     * @param imagename 选中和未选中使用的两张图片名称
     * @param context   上下文
     */
    fun createBgByImage9png(imagename: Array<String>,
                            context: Context): StateListDrawable {
        val bg = StateListDrawable()
        val normal = get9png(imagename[0], context)
        val pressed = get9png(imagename[1], context)
        bg.addState(PRESSED_ENABLED_STATE_SET, pressed)
        bg.addState(ENABLED_STATE_SET, normal)
        bg.addState(EMPTY_STATE_SET, normal)
        return bg
    }

    fun getBitmap(imagename: String, context: Context): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            //			File file = new File("image/" + imagename  + ".png");
            val imagePath = "image/$imagename.png"
            //			if (!file.isFile()) {
            //				imagePath = "image/" + imagename + "480x800" + ".png";
            //			}
            bitmap = BitmapFactory.decodeStream(context.assets.open(imagePath))

        } catch (e: IOException) {
            bitmap?.recycle()
            e.printStackTrace()
        }

        return bitmap
    }

    /**
     * 该方法主要根据图片名称获取可用的 Drawable
     *
     * @param imagename 选中和未选中使用的两张图片名称
     * @param context   上下文
     * @return 可用的Drawable
     */
    fun getdrawble(imagename: String, context: Context): Drawable? {
        var drawable: Drawable? = null
        var bitmap: Bitmap? = null
        try {
            val imagePath = "image/$imagename.png"
            bitmap = BitmapFactory.decodeStream(context.assets.open(imagePath))
            drawable = BitmapDrawable(bitmap)
        } catch (e: IOException) {
            bitmap?.recycle()
            e.printStackTrace()
        }

        return drawable
    }


    /**
     * 获取asset下面的.9 png
     *
     * @param imagename 图片名
     * @param context   上下文对象
     */
    fun get9png(imagename: String, context: Context): NinePatchDrawable? {
        val toast_bitmap: Bitmap
        try {
            toast_bitmap = BitmapFactory.decodeStream(context.assets.open("image/$imagename.9.png"))
            val temp = toast_bitmap.ninePatchChunk
            val is_nine = NinePatch.isNinePatchChunk(temp)
            if (is_nine) {
                return NinePatchDrawable(context.resources, toast_bitmap, temp, Rect(), null)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}
