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
import android.widget.Toast
import com.bumptech.glide.Glide
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.inflate
import com.rayfantasy.icode.extension.loadPortrait
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.Reply
import com.rayfantasy.icode.ui.activity.UserActivity
import com.rayfantasy.icode.util.ms2RelativeDate
import com.tencent.bugly.proguard.t
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_recycler_reply_list.view.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onLongClick
import org.jetbrains.anko.startActivity

class ReplyListAdapter(val activity: Activity, var replyList: MutableList<Reply>, onLoadingMore: () -> Unit) :
        LoadMoreAdapter<ReplyListAdapter.NormalViewHolder>(activity, onLoadingMore) {
    private val glide by lazy { Glide.with(activity) }
    private val circleTransformation by lazy { CropCircleTransformation(activity) }
    override val normalItemCount: Int
        get() = replyList.size

    override fun onBindNormalViewHolder(holder: NormalViewHolder, position: Int) {
        PostUtil.init(activity)
        val replyList = replyList[position]
        holder.username.text = replyList.username
        holder.reply.text = replyList.content
        holder.time.text = ms2RelativeDate(activity, replyList.createAt!!)
       /* if(PostUtil.user == null || PostUtil.user!!.username.equals(replyList.username)){

        }
        else{
            holder.del.text = "删除"

        }*/
        holder.pic.loadPortrait(replyList.username)
        holder.pic.onClick { activity.startActivity<UserActivity>("username" to replyList.username.toString()) }
    }
    fun deleteReply(id : Int){
        PostUtil.delReply(id,{Toast.makeText(activity,"删除回复成功",Toast.LENGTH_SHORT).show()
        },{ t , rc -> Toast.makeText(activity,"删除回复失败，rc = $rc",Toast.LENGTH_SHORT).show()}
        )
    }

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int)
            = NormalViewHolder(parent.inflate(R.layout.item_recycler_reply_list))


    class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pic = itemView.reply_user_pic
        val username = itemView.reply_username
        val time = itemView.reply_time
        val reply = itemView.reply_context
        val reply_bg = itemView.reply_bg
        val del = itemView.reply_delete
    }


}
