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
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.inflate
import com.rayfantasy.icode.extra.CircleTransform
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.Reply
import com.rayfantasy.icode.util.ms2RelativeDate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycler_reply_list.view.*

class ReplyListAdapter(val activity: Activity, var replyList: MutableList<Reply>, onLoadingMore: () -> Unit) :
        LoadMoreAdapter<ReplyListAdapter.NormalViewHolder>(activity, onLoadingMore) {
    private val picasso by lazy { Picasso.with(activity) }
    override val normalItemCount: Int
        get() = replyList.size

    override fun onBindNormalViewHolder(holder: NormalViewHolder, position: Int) {
        val replyList = replyList[position]
        holder.username.text = replyList.username
        holder.reply.text = replyList.content
        holder.time.text = ms2RelativeDate(activity, replyList.createat!!)

        picasso.load(PostUtil.
                getProfilePicUrl(replyList.username!!))
                .centerCrop().error(R.mipmap.ic_user_black)
                .resizeDimen(R.dimen.profile_pic_size, R.dimen.profile_pic_size)
                .transform(circleTransform)
                .into(holder.pic)
    }

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int)
            = NormalViewHolder(parent.inflate(R.layout.item_recycler_reply_list))


    class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pic = itemView.reply_user_pic
        val username = itemView.reply_username
        val time = itemView.reply_time
        val reply = itemView.reply_context
    }

    companion object {
        private val circleTransform = CircleTransform()
    }
}