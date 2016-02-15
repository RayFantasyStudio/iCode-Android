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
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.Select
import com.rayfantasy.icode.R
import com.rayfantasy.icode.databinding.ItemCodeListBinding
import com.rayfantasy.icode.databinding.ItemRecyclerCodeListBinding
import com.rayfantasy.icode.extension.inflate
import com.rayfantasy.icode.extension.loadPortrait
import com.rayfantasy.icode.extension.onLike
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.bean.Favorite
import com.rayfantasy.icode.postutil.bean.Favorite_Table
import com.rayfantasy.icode.ui.activity.UserActivity
import com.rayfantasy.icode.ui.activity.startBlockActivity
import com.rayfantasy.icode.util.ms2RelativeDate
import kotlinx.android.synthetic.main.item_code_list.view.*
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
        holder.pic.loadPortrait(codeGood.username)
        holder.pic.onClick {
            activity.startActivity<UserActivity>("username" to codeGood.username.toString())
        }
        holder.binding.highlight = codeGood.highlight ?: false
        holder.bg.onClick {
            holder.bg.startBlockActivity(codeGood)
        }
        val favorite = Select().from(Favorite::class.java).where(Favorite_Table.goodId.`is`(codeGood.id)).querySingle()
        holder.like.setLiked(favorite != null)
        holder.like.onLike {
            liked { PostUtil.addFavorite(codeGood.id,
                    {
                        Toast.makeText(activity,"成功",Toast.LENGTH_SHORT)
                        Favorite(codeGood.id, System.currentTimeMillis()).save()
                        holder.like_count.text = "被收藏${codeGood.favorite +1}次"

                    },
                    {t,rc -> Toast.makeText(activity,"失败",Toast.LENGTH_SHORT) }) }
            unLiked { PostUtil.delFavorite(codeGood.id,{
                Toast.makeText(activity,"成功",Toast.LENGTH_SHORT)
                Delete()
                        .from(Favorite::class.java)
                        .where(Favorite_Table.goodId.`is`(codeGood.id))
                        .execute()
                holder.like_count.text = "被收藏${codeGood.favorite -1}次"
            },
                    {t,rc -> Toast.makeText(activity,"失败",Toast.LENGTH_SHORT) })
            }
        }
        holder.reply_count.text = "共${codeGood.reply}条回复"
        holder.like_count.text = "被收藏${codeGood.favorite}次"
    }

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int)
            = NormalViewHolder(parent.inflate(R.layout.item_code_list))

    class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pic = itemView.code_usericon
        val username = itemView.code_username
        val time = itemView.code_time
        val title = itemView.code_title
        val subTitle = itemView.code_subtitle
        val bg = itemView.code_card
        val like = itemView.code_like
        val like_count = itemView.code_favoCount
        val reply_count = itemView.code_replyCount
        val binding: ItemCodeListBinding

        init {
            binding = ItemCodeListBinding.bind(itemView)
            binding.theme = ICodeTheme
        }
    }
}