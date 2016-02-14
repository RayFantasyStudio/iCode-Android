package com.rayfantasy.icode.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.inflate
import com.rayfantasy.icode.extension.loadPortrait
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.ui.fragment.SettingFragment
import kotlinx.android.synthetic.main.item_block_favorite.view.*
import kotlinx.android.synthetic.main.item_block_text.view.*
import kotlinx.android.synthetic.main.item_block_title.view.*
import org.evilbinary.highliter.HighlightEditText
import org.evilbinary.managers.Configure
import org.jetbrains.anko.defaultSharedPreferences

class BlockAdapter(ctx: Context, val  codeGood: CodeGood, var blocks: List<CodeGood.Block>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

            }
            is FavoriteViewHolder ->{}

        }

    }

    override fun getItemCount() = blocks.size + 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        CodeGood.BlockType.CODE -> CodeViewHolder(parent.inflate(R.layout.item_block_code) as ViewGroup, highlightTheme)
        TITLE_VIEW -> TitleViewHolder(parent.inflate(R.layout.item_block_title))
        FAVORITE_VIEW -> FavoriteViewHolder(parent.inflate(R.layout.item_block_favorite))
        CodeGood.BlockType.TEXT -> TextViewHolder(parent.inflate(R.layout.item_block_text))
        else -> FavoriteViewHolder(parent.inflate(R.layout.item_block_favorite))

    }

    override fun getItemViewType(position: Int) = if (position == 0) TITLE_VIEW else if (position == blocks.size+1) FAVORITE_VIEW else {
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
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favorite = itemView.favo_btn
    }
}