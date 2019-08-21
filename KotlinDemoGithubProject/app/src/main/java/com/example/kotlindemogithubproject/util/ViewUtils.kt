package com.example.kotlindemogithubproject.util

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.view.*
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.example.kotlindemogithubproject.R


object ViewUtils {

    @JvmOverloads
    fun virtualClick(view: View, pressTime: Int = 300) {
        val downTime = System.currentTimeMillis()
        val width = view.width
        val height = view.height
        val x = view.x + width / 2
        val y = view.y + height / 2

        val downEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0)
        val upTime = downTime + pressTime
        val upEvent = MotionEvent.obtain(upTime, upTime, MotionEvent.ACTION_UP, x, y, 0)

        view.onTouchEvent(downEvent)
        view.onTouchEvent(upEvent)

        downEvent.recycle()
        upEvent.recycle()
    }

    fun setLongClickCopy(@NonNull textView: TextView) {
        textView.setOnLongClickListener { v ->
            val text = v as TextView
            AppUtils.copyToClipboard(text.context, text.text.toString())
            true
        }
    }

    fun setTextView(@NonNull textView: TextView, text: String) {
        if (!StringUtils.isBlank(text)) {
            textView.text = text
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
    }

    fun getSelectedMenu(@NonNull menuItem: MenuItem): MenuItem? {
        if (menuItem.subMenu == null || menuItem.subMenu.size() == 0) {
            return null
        }
        var selected: MenuItem? = null
        for (i in 0 until menuItem.subMenu.size()) {
            val item = menuItem.subMenu.getItem(i)
            if (item.isChecked) {
                selected = item
                break
            }
        }
        return selected
    }

    fun selectMenuItem(@NonNull menu: Menu, @IdRes itemId: Int, findSub: Boolean) {
        var find = false
        for (i in 0 until menu.size()) {
            if (!findSub) {
                menu.getItem(i).isChecked = menu.getItem(i).itemId == itemId
            } else {
                if (menu.getItem(i).itemId == itemId) {
                    find = true
                }
            }
        }

        if (!findSub) {
            return
        } else if (find) {
            selectMenuItem(menu, itemId, false)
        } else {
            for (i in 0 until menu.size()) {
                val subMenu = menu.getItem(i).subMenu
                if (subMenu != null)
                    selectMenuItem(subMenu, itemId, true)
            }
        }
    }

    fun getRefreshLayoutColors(context: Context): IntArray {
        return intArrayOf(getAccentColor(context), getPrimaryColor(context), getPrimaryDarkColor(context))
    }

    @ColorInt
    fun getPrimaryColor(@NonNull context: Context): Int {
        return getColorAttr(context, R.attr.colorPrimary)
    }

    @ColorInt
    fun getPrimaryDarkColor(@NonNull context: Context): Int {
        return getColorAttr(context, R.attr.colorPrimaryDark)
    }

    @ColorInt
    fun getAccentColor(@NonNull context: Context): Int {
        return getColorAttr(context, R.attr.colorAccent)
    }

    @ColorInt
    fun getPrimaryTextColor(@NonNull context: Context): Int {
        return getColorAttr(context, android.R.attr.textColorPrimary)
    }

    @ColorInt
    fun getSecondaryTextColor(@NonNull context: Context): Int {
        return getColorAttr(context, android.R.attr.textColorSecondary)
    }

    @ColorInt
    fun getWindowBackground(@NonNull context: Context): Int {
        return getColorAttr(context, android.R.attr.windowBackground)
    }


    @ColorInt
    private fun getColorAttr(@NonNull context: Context, attr: Int): Int {
        val theme = context.theme
        val typedArray = theme.obtainStyledAttributes(intArrayOf(attr))
        val color = typedArray.getColor(0, Color.LTGRAY)
        typedArray.recycle()
        return color
    }


    private fun getBitmapFromResource(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    /**
     * Get bitmap from resource
     */
    fun getBitmapFromResource(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        return if (drawable is BitmapDrawable) {
            BitmapFactory.decodeResource(context.resources, drawableId)
        } else if (drawable is VectorDrawable) {
            getBitmapFromResource(drawable as VectorDrawable)
        } else {
            throw IllegalArgumentException("unsupported drawable type")
        }
    }

    fun getRGBColor(colorValue: Int, withAlpha: Boolean): String {
        val r = colorValue shr 16 and 0xff
        val g = colorValue shr 8 and 0xff
        val b = colorValue and 0xff
        val a = colorValue shr 24 and 0xff
        var red = Integer.toHexString(r)
        var green = Integer.toHexString(g)
        var blue = Integer.toHexString(b)
        var alpha = Integer.toHexString(a)
        red = fixColor(red)
        green = fixColor(green)
        blue = fixColor(blue)
        alpha = fixColor(alpha)
        return if (withAlpha) alpha + red + green + blue else red + green + blue
    }

    private fun fixColor(@NonNull colorStr: String): String {
        return if (colorStr.length == 1) "0$colorStr" else colorStr
    }

    fun isLightColor(color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return if (darkness < 0.5) {
            true // It's a light color
        } else {
            false // It's a dark color
        }
    }


}
