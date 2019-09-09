

package com.example.javademogithubpractice.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.javademogithubpractice.R;


public class ViewUtils {

    public static void virtualClick(final View view){
        virtualClick(view, 300);
    }

    public static void virtualClick(final View view, int pressTime){
        long downTime = System.currentTimeMillis();
        int width = view.getWidth();
        int height = view.getHeight();
        float x = view.getX() + width / 2;
        float y = view.getY() + height / 2;

        MotionEvent downEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
        long upTime = downTime + pressTime;
        final MotionEvent upEvent = MotionEvent.obtain(upTime, upTime, MotionEvent.ACTION_UP, x, y, 0);

        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);

        downEvent.recycle();
        upEvent.recycle();
    }

    @ColorInt
    public static int getPrimaryColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorPrimary);
    }

    public static void setTextView(@NonNull TextView textView, String text) {
        if (!StringUtils.isBlank(text)) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @ColorInt
    private static int getColorAttr(@NonNull Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(new int[]{attr});
        final int color = typedArray.getColor(0, Color.LTGRAY);
        typedArray.recycle();
        return color;
    }


    private static Bitmap getBitmapFromResource(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmapFromResource(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmapFromResource((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }


    public static MenuItem getSelectedMenu(@NonNull MenuItem menuItem,@NonNull MenuItem item) {
        if (menuItem.getSubMenu() == null || menuItem.getSubMenu().size() == 0) {
            return null;
        }
        item.setChecked(true);
        MenuItem selected = null;
        for (int i = 0; i < menuItem.getSubMenu().size(); i++) {
            MenuItem item1= menuItem.getSubMenu().getItem(i);
            if (item.equals(item1)) {
                selected = item1;
                break;
            }
            else {
                item1.setChecked(false);
            }
        }
        return selected;
    }





}
