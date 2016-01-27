package com.rayfantasy.icode.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.inflate
import com.rayfantasy.icode.postutil.Block
import com.rayfantasy.icode.postutil.BlockType
import kotlinx.android.synthetic.main.item_block_code.view.*
import kotlinx.android.synthetic.main.item_block_text.view.*

class BlockAdapter(val blocks: List<Block>) : RecyclerView.Adapter<BlockAdapter.BlockViewHolder>() {

    override fun onBindViewHolder(holder: BlockViewHolder?, position: Int) {
        val block = blocks[position]
        when (block.blockType) {
            BlockType.TEXT -> holder?.itemView?.tv_text?.text = block.content
            else -> holder?.itemView?.tv_code?.text = block.content
        }
    }

    override fun getItemCount() = blocks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder? = when (BlockType.values()[viewType]) {
        BlockType.TEXT -> BlockViewHolder(parent.inflate(R.layout.item_block_text))
        else -> BlockViewHolder(parent.inflate(R.layout.item_block_code))
    }

    override fun getItemViewType(position: Int)
            = blocks[position].blockType.ordinal

    class BlockViewHolder(itemView: View?) :
            RecyclerView.ViewHolder(itemView)
}