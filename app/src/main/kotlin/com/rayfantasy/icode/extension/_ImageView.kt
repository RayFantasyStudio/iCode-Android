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

package com.rayfantasy.icode.extension

import android.widget.ImageView
import com.amulyakhare.textdrawable.TextDrawable
import com.bumptech.glide.Glide
import com.rayfantasy.icode.postutil.PostUtil
import jp.wasabeef.glide.transformations.CropCircleTransformation
import org.jetbrains.anko.image

fun ImageView.loadPortrait(username: String, errorRes: Int? = null) {
    val errorIcon = errorRes?.let { resources.getDrawable(errorRes) } ?:
            TextDrawable
                    .builder().buildRound(username[0].toString().toUpperCase(),
                    username.hashCode().alpha(0xff).shadowColor())
    image = errorIcon
    Glide.with(context)
            .load(PostUtil.getProfilePicUrl(username))
            .error(errorIcon)
            .bitmapTransform(CropCircleTransformation(context))
            .into(this)
}