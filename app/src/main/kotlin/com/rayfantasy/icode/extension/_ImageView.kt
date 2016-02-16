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
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.rayfantasy.icode.postutil.PostUtil
import jp.wasabeef.glide.transformations.CropCircleTransformation

fun ImageView.loadPortrait(username: String, placeholderRes: Int? = null, circle: Boolean = true,
                           onResourceReady: ((GlideDrawable, GlideAnimation<in GlideDrawable>) -> Unit)? = null) {
    val placeholder = placeholderRes?.let { resources.getDrawable(placeholderRes) } ?:
            TextDrawable
                    .builder().buildRound(username[0].toString().toUpperCase(),
                    username.hashCode().alpha(0xff).shadowColor())
    val builder = Glide.with(context)
            .load(PostUtil.getProfilePicUrl(username))
            .placeholder(placeholder)
            .error(placeholder)
            .centerCrop()
    if (circle)
        builder.bitmapTransform(CropCircleTransformation(context))
    builder.into(object : GlideDrawableImageViewTarget(this) {
        override fun onResourceReady(resource: GlideDrawable, animation: GlideAnimation<in GlideDrawable>) {
            super.onResourceReady(resource, animation)
            onResourceReady?.invoke(resource, animation)
        }
    })
}