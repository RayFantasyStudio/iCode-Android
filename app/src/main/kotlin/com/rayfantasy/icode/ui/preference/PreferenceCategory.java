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

package com.rayfantasy.icode.ui.preference;

import android.content.Context;
import android.databinding.Observable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.rayfantasy.icode.model.ICodeTheme;

public class PreferenceCategory extends android.preference.PreferenceCategory {
    private Observable.OnPropertyChangedCallback changedCallback;

    public PreferenceCategory(Context context) {
        super(context);
    }

    public PreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PreferenceCategory(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onBindView(@NonNull View view) {
        super.onBindView(view);
        final TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setTextColor(ICodeTheme.INSTANCE.getColorAccent().get());
        changedCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                titleView.setTextColor(ICodeTheme.INSTANCE.getColorAccent().get());
            }
        };
        ICodeTheme.INSTANCE.getColorAccent().addOnPropertyChangedCallback(changedCallback);
        titleView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {


            @Override
            public void onViewAttachedToWindow(View v) {
                ICodeTheme.INSTANCE.getColorAccent().addOnPropertyChangedCallback(changedCallback);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                ICodeTheme.INSTANCE.getColorAccent().removeOnPropertyChangedCallback(changedCallback);
            }
        });
    }
}
