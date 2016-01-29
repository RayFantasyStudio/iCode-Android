package com.rayfantasy.icode.adapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.inflate
import com.rayfantasy.icode.extension.string
import com.rayfantasy.icode.postutil.Block
import com.rayfantasy.icode.postutil.BlockType
import com.rayfantasy.icode.postutil.PostUtil
import kotlinx.android.synthetic.main.footer_edit.view.*
import kotlinx.android.synthetic.main.header_edit.view.*
import kotlinx.android.synthetic.main.item_edit_code.view.*
import kotlinx.android.synthetic.main.item_edit_header.view.*
import kotlinx.android.synthetic.main.item_edit_text.view.*
import org.evilbinary.highliter.HighlightEditText
import org.evilbinary.managers.Configure
import org.evilbinary.utils.DirUtil
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.onClick
import java.io.File
import java.util.*

class EditBlockAdapter(val ctx: Context, blocks: List<Block>? = null) : DraggableItemAdapter<RecyclerView.ViewHolder>,
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private companion object {
        const val VIEW_TYPE_HEADER = -1
        const val VIEW_TYPE_FOOTER = -2
    }

    val languages by lazy {
        File(DirUtil.getFilesDir(ctx), "langDefs")
                .listFiles()
                .map { it.name.substringBeforeLast(".") }
                .sorted()
                .toTypedArray()
    }

    var blocks: MutableList<Block>
    private val headerViewHolder: HeaderViewHolder
    var title: String
        get() = headerViewHolder.title.string
        set(value) {
            headerViewHolder.title.string = value
        }
    var subTitle: String
        get() = headerViewHolder.subTitle.string
        set(value) {
            headerViewHolder.subTitle.string = value
        }
    val content: String
        get() = PostUtil.gson.toJson(blocks)
    val textWatchers = HashMap<BlockViewHolder, BlockTextWatcher>()

    init {
        if (blocks is MutableList<Block>) {
            this.blocks = blocks
        } else {
            this.blocks = if (blocks == null) ArrayList() else ArrayList(blocks)
        }
        setHasStableIds(true)
        val headerView = ctx.layoutInflater.inflate(R.layout.header_edit, null, false)
        headerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        headerViewHolder = HeaderViewHolder(headerView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is BlockViewHolder -> {
                val block = blocks[position - 1]
                var textWatcher = textWatchers[holder]
                if (textWatcher == null) {
                    textWatcher = BlockTextWatcher(block)
                    textWatchers[holder] = textWatcher
                } else {
                    holder.content.removeTextChangedListener(textWatcher)
                    textWatcher.block = block
                }

                holder.btnClose.onClick {
                    blocks.remove(block)
                    notifyItemRemoved(position)
                }
                when (holder) {
                    is TextViewHolder -> holder.content.string = block.content
                    is CodeViewHolder -> {
                        holder.blockType.text = block.extra + ctx.getString(holder.blockTypeStringRes)
                        val configure = holder.content.configure
                        configure.mLanguage = block.extra
                        holder.content.loadFromConfigure(configure)
                        holder.content.setSource(block.content)
                    }
                }
                holder.content.addTextChangedListener(textWatcher)
            }

            is FooterViewHolder -> {
                holder.addText.onClick {
                    blocks.add(Block(BlockType.TEXT, ""))
                    notifyItemInserted(position)
                }
                holder.addCode.onClick {
                    AlertDialog.Builder(ctx)
                            .setTitle(R.string.title_choose_language)
                            .setItems(languages) { dialogInterface: DialogInterface, i: Int ->
                                blocks.add(Block(BlockType.CODE, "", languages[i]))
                                notifyItemInserted(position)
                            }.show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        VIEW_TYPE_HEADER -> headerViewHolder
        VIEW_TYPE_FOOTER -> FooterViewHolder(parent.inflate(R.layout.footer_edit))
        BlockType.CODE -> CodeViewHolder(parent.inflate(R.layout.item_edit_code))
        else -> TextViewHolder(parent.inflate(R.layout.item_edit_text))
    }

    override fun getItemCount() = blocks.size + 2

    override fun onCheckCanStartDrag(holder: RecyclerView.ViewHolder, position: Int, x: Int, y: Int) = when (holder) {
        is BlockViewHolder -> {
            val handle = holder.handle
            (x in handle.x..handle.x + handle.width
                    && y in handle.y..handle.y + handle.height)
        }
        else -> false
    }

    override fun onMoveItem(fromPosition: Int, toPosition: Int) {
        blocks.add(toPosition - 1, blocks.removeAt(fromPosition - 1))
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onGetItemDraggableRange(holder: RecyclerView.ViewHolder?, position: Int)
            = ItemDraggableRange(1, itemCount - 2)

    override fun getItemViewType(position: Int)
            = if (position == 0) VIEW_TYPE_HEADER else
        if (position == itemCount - 1) VIEW_TYPE_FOOTER else blocks[position - 1].blockType

    override fun getItemId(position: Int) = position.toLong()

    abstract class BlockViewHolder(itemView: View) : AbstractDraggableItemViewHolder(itemView) {
        val blockType = itemView.block_type
        val btnClose = itemView.btn_close
        val handle = itemView.handle
        abstract val content: EditText
        abstract val blockTypeStringRes: Int

        init {
            blockType.setText(blockTypeStringRes)
        }
    }

    class CodeViewHolder(itemView: View) : BlockViewHolder(itemView) {
        override val blockTypeStringRes: Int
            get() = R.string.block_type_code
        override val content: HighlightEditText

        init {
            val configure = Configure(itemView.context)
            content = HighlightEditText(itemView.context, configure)
            content.background = null
            content.setHint(R.string.block_type_code)
            itemView.linearLayout.addView(content)
        }
    }

    class TextViewHolder(itemView: View) : BlockViewHolder(itemView) {
        override val blockTypeStringRes: Int
            get() = R.string.block_type_text
        override val content = itemView.tv_text
    }

    class HeaderViewHolder(itemView: View) : AbstractDraggableItemViewHolder(itemView) {
        val title = itemView.et_title
        val subTitle = itemView.et_sub_title
    }

    class FooterViewHolder(itemView: View) : AbstractDraggableItemViewHolder(itemView) {
        val addText = itemView.btn_add_text
        val addCode = itemView.btn_add_code
    }

    class BlockTextWatcher(var block: Block) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            block.content = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}
