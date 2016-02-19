package com.rayfantasy.icode.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.Select
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.inflate
import com.rayfantasy.icode.extension.loadPortrait
import com.rayfantasy.icode.extension.onLike
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.bean.Favorite
import com.rayfantasy.icode.postutil.bean.Favorite_Table
import com.rayfantasy.icode.ui.fragment.SettingFragment
import kotlinx.android.synthetic.main.item_block_favorite.view.*
import kotlinx.android.synthetic.main.item_block_text.view.*
import kotlinx.android.synthetic.main.item_block_title.view.*
import org.evilbinary.highliter.HighlightEditText
import org.evilbinary.managers.Configure
import org.jetbrains.anko.defaultSharedPreferences

class BlockAdapter(var ctx: Context, val codeGood: CodeGood, var blocks: List<CodeGood.Block>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TITLE_VIEW = 998
    private val FAVORITE_VIEW = 999
    private val highlightTheme = ctx.defaultSharedPreferences.getString(SettingFragment.PREF_HIGHLIGHT, SettingFragment.DEFAULT_HIGHLIGHT)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is CodeViewHolder -> {
                val block = blocks[position - 1]
                val configure = holder.highlight.configure
                configure.mLanguage = block.extra
                holder.highlight.loadFromConfigure(configure)
                holder.highlight.setSource(block.content)
            }
            is TextViewHolder -> {
                val block = blocks[position - 1]
                holder.content.text = block.content
            }
            is TitleViewHolder -> {
                holder.username.text = codeGood.username
                holder.subtitle.text = codeGood.subtitle
                holder.user_icon.loadPortrait(codeGood.username)
                val favorite = Select().from(Favorite::class.java).where(Favorite_Table.goodId.`is`(codeGood.id)).querySingle()
                holder.like_btn.setLiked(favorite != null)
                holder.like_btn.onLike {
                    liked {
                        PostUtil.addFavorite(codeGood.id, { Toast.makeText(ctx, "收藏成功", Toast.LENGTH_LONG).show(); Favorite(codeGood.id, System.currentTimeMillis()).save() }, { t, rc -> Toast.makeText(ctx, "收藏失败,$rc", Toast.LENGTH_LONG).show() })
                    }
                    unLiked {
                        PostUtil.delFavorite(codeGood.id, {
                            Toast.makeText(ctx, "取消收藏成功", Toast.LENGTH_LONG).show()
                            Delete()
                                    .from(Favorite::class.java)
                                    .where(Favorite_Table.goodId.`is`(codeGood.id))
                                    .execute()
                        }, { t, rc -> Toast.makeText(ctx, "取消收藏失败,$rc", Toast.LENGTH_LONG).show() })
                    }
                }

                holder.likecount.text = "${codeGood.favorite}"

            }
        }

    }


    override fun getItemCount() = blocks.size +1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        CodeGood.BlockType.CODE -> CodeViewHolder(parent.inflate(R.layout.item_block_code) as ViewGroup, highlightTheme)
        TITLE_VIEW -> TitleViewHolder(parent.inflate(R.layout.item_block_title))
        CodeGood.BlockType.TEXT -> TextViewHolder(parent.inflate(R.layout.item_block_text))
        else -> TextViewHolder(parent.inflate(R.layout.item_block_favorite))

    }

    override fun getItemViewType(position: Int) = if (position == 0) TITLE_VIEW else {
        blocks[position - 1].blockType
    }

    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content = itemView.tv_text
    }

    class CodeViewHolder(itemView: ViewGroup, highlightTheme: String) : RecyclerView.ViewHolder(itemView) {
        val highlight: HighlightEditText

        init {
            val configure = Configure(itemView.context)
            configure.mTheme = highlightTheme
            highlight = HighlightEditText(itemView.context, configure)
            highlight.editAble = false
            highlight.keyListener = null
            highlight.background = null
            itemView.addView(highlight)
        }
    }

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.block_username
        val subtitle = itemView.block_sub_title
        val user_icon = itemView.block_userIcon
        val like_btn = itemView.block_like_title
        val likecount = itemView.block_likecount_title
    }
}