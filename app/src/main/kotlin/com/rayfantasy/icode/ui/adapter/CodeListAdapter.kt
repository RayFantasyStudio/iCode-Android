/*
 * Copyright 2015 Alex Zhang aka. ztc1997
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

package com.rayfantasy.icode.ui.adapter

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.inflate
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.ui.activity.BlocksActivity
import com.rayfantasy.icode.util.ms2RelativeDate
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_recycler_code_list.view.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity

class CodeListAdapter(val activity: Activity, var codeGoods: MutableList<CodeGood>, onLoadingMore: () -> Unit) :
        LoadMoreAdapter<CodeListAdapter.NormalViewHolder>(activity, onLoadingMore) {
    override val normalItemCount: Int
        get() = codeGoods.size

    override fun onBindNormalViewHolder(holder: NormalViewHolder, position: Int) {
        val codeGood = codeGoods[position]

        holder.title.text = codeGood.title
        holder.subTitle.text = codeGood.subtitle
        holder.time.text = ms2RelativeDate(activity, codeGood.createAt!!)
        holder.username.text = codeGood.username
        if (codeGood.highlight ?: false) {
            holder.title.setTextColor(Color.RED)
            holder.username.setTextColor(Color.RED)
            holder.subTitle.setTextColor(Color.RED)
            holder.username.append("被管理员临时高亮!")
        } else {
            holder.title.setTextColor(Color.rgb(0, 136, 255))
            holder.username.setTextColor(Color.rgb(140, 140, 140))
            holder.subTitle.setTextColor(Color.rgb(140, 140, 140))
        }
        Glide.with(activity)
                .load(PostUtil.getProfilePicUrl(codeGood.username!!))
                .error(R.mipmap.ic_user_black)
                .bitmapTransform(CropCircleTransformation(activity))
                .into(holder.pic)
        holder.pic
        holder.bg.onClick {
            activity.startActivity<BlocksActivity>("codeGood" to codeGood)
        }
    }

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int)
            = NormalViewHolder(parent.inflate(R.layout.item_recycler_code_list))

    class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pic = itemView.pic
        val username = itemView.username
        val time = itemView.time
        val title = itemView.title
        val subTitle = itemView.sub_title
        val bg = itemView.element_bg
    }
}