package com.rayfantasy.icode.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
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
import com.rayfantasy.icode.ui.activity.UserActivity
import com.rayfantasy.icode.ui.fragment.SettingFragment
import kotlinx.android.synthetic.main.item_block_text.view.*
import kotlinx.android.synthetic.main.item_block_title.view.*
import org.evilbinary.highliter.HighlightEditText
import org.evilbinary.managers.Configure
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.longToast
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity

class BlockAdapter(var ctx: Context, val codeGood: CodeGood, var blocks: List<CodeGood.Block>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TITLE_VIEW = 998
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
                holder.user_icon.onClick {
                    ctx.startActivity<UserActivity>("username" to codeGood.username)
                }
                val favorite = Select().from(Favorite::class.java).where(Favorite_Table.goodId.`is`(codeGood.id)).querySingle()
                holder.like_btn.setLiked(favorite != null)
                holder.like_btn.onLike {
                    liked {
                        PostUtil.addFavorite(codeGood.id) {
                            onSuccess {
                                ctx.longToast("收藏成功")
                                Favorite(codeGood.id, System.currentTimeMillis()).save()
                            }
                            onFailed { throwable, rc -> ctx.longToast("收藏失败,$rc") }
                        }
                    }
                    unLiked {
                        PostUtil.delFavorite(codeGood.id) {
                            onSuccess {
                                ctx.longToast("取消收藏成功")
                                Delete()
                                        .from(Favorite::class.java)
                                        .where(Favorite_Table.goodId.`is`(codeGood.id))
                                        .execute()
                            }
                            onFailed { throwable, rc -> ctx.longToast("取消收藏失败,$rc") }
                        }
                    }
                }

                holder.likecount.text = "${codeGood.favorite}"

            }
        }

    }


    override fun getItemCount() = blocks.size + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        CodeGood.BlockType.CODE -> CodeViewHolder(parent.inflate(R.layout.item_block_code) as ViewGroup, highlightTheme)
        TITLE_VIEW -> TitleViewHolder(parent.inflate(R.layout.item_block_title))
        else -> TextViewHolder(parent.inflate(R.layout.item_block_text))

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