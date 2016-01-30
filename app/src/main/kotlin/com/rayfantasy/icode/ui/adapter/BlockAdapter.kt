package com.rayfantasy.icode.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.inflate
import com.rayfantasy.icode.postutil.Block
import com.rayfantasy.icode.postutil.BlockType
import kotlinx.android.synthetic.main.item_block_text.view.*
import org.evilbinary.highliter.HighlightEditText
import org.evilbinary.managers.Configure

class BlockAdapter(val blocks: List<Block>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val block = blocks[position]
        when (holder) {
            is CodeViewHolder -> {
                val configure = holder.highlight.configure
                configure.mLanguage = block.extra
                holder.highlight.loadFromConfigure(configure)
                holder.highlight.setSource(block.content)
            }
            is TextViewHolder -> holder.content.text = block.content
        }
    }

    override fun getItemCount() = blocks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        BlockType.CODE -> CodeViewHolder(parent.inflate(R.layout.item_block_code))
        else -> TextViewHolder(parent.inflate(R.layout.item_block_text))
    }

    override fun getItemViewType(position: Int)
            = blocks[position].blockType

    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content = itemView.tv_text
    }

    class CodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val highlight: HighlightEditText

        init {
            val configure = Configure(itemView.context)
            highlight = HighlightEditText(itemView.context, configure)
            highlight.editAble = false
            highlight.keyListener = null
            highlight.background = null
            (itemView as ViewGroup).addView(highlight)
        }
    }
}