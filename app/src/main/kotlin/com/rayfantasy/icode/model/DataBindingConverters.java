/*
 * Copyright 2016 Alex Zhang aka. ztc1997
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.rayfantasy.icode.model;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

public class DataBindingConverters {
    @BindingAdapter("android:textCursorDrawable")
    public static void setCursorDrawableColor(EditText editText, int color) {
        try {
            Field fCursorDrawableRes =
                    TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);

            Drawable[] drawables = new Drawable[2];
            Resources res = editText.getContext().getResources();
            drawables[0] = res.getDrawable(mCursorDrawableRes);
            drawables[1] = res.getDrawable(mCursorDrawableRes);
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        } catch (final Throwable ignored) {
        }
    }

    @BindingAdapter("app:supportBackgroundTintList")
    public static void setSupportBackgroundTintList(AppCompatEditText editText, int color) {
        editText.setSupportBackgroundTintList(ColorStateList.valueOf(color));
    }

    @BindingAdapter("android:textColorHint")
    public static void settTextColorHint(TextInputLayout til, int color) {
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        try {
            /*Field fDefaultTextColor =
                    TextInputLayout.class.getDeclaredField("mDefaultTextColor");
            fDefaultTextColor.setAccessible(true);
            fDefaultTextColor.set(til, colorStateList);*/
            Field fFocusedTextColor =
                    TextInputLayout.class.getDeclaredField("mFocusedTextColor");
            fFocusedTextColor.setAccessible(true);
            fFocusedTextColor.set(til, colorStateList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter("android:background")
    public static void setBackground(View view, int color) {
        view.setBackgroundDrawable(new ColorDrawable(color));
    }

    @BindingAdapter("app:backgroundTintList")
    public static void setBackgroundTintList(FloatingActionButton fab, int color) {
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        fab.setBackgroundTintList(colorStateList);
    }

    @BindingAdapter("app:itemSelectColor")
    public static void setItemSelectColor(NavigationView nv, int color) {
        Context context = nv.getContext();
        int normal = context.getResources().getColor(android.support.v7.appcompat.R.color.abc_secondary_text_material_light);
        int[] colors = new int[]{color, color, color, color, normal};
        int[][] states = new int[5][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_focused};
        states[3] = new int[]{android.R.attr.state_checked};
        states[4] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        nv.setItemTextColor(colorList);
        nv.setItemIconTintList(colorList);
    }
}
