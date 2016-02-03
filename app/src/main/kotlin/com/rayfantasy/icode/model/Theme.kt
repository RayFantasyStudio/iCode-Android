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

package com.rayfantasy.icode.model

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

class Theme(colorPrimary: Int) {
    val colorPrimary = object : ObservableInt(colorPrimary) {
        override fun set(value: Int) {
            super.set(value)
            colorPrimaryDrawable.set(ColorDrawable(value))
        }
    }
    val colorPrimaryDrawable = ObservableField<Drawable>(ColorDrawable(colorPrimary))
}