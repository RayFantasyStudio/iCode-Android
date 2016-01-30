package com.rayfantasy.icode.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rayfantasy.icode.R
import kotlinx.android.synthetic.main.footer_recycler_view.view.*
import org.jetbrains.anko.onClick

abstract class LoadMoreAdapter<NV : RecyclerView.ViewHolder>(activity: Activity, private val onLoadingMore: () -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    abstract protected val normalItemCount: Int
    abstract fun onBindNormalViewHolder(holder: NV, position: Int)
    abstract fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): NV
    private var hintNoMore = R.string.footer_msg_no_more
    open val normalViewType = VIEW_TYPE_NORMAL
    private val footerViewHolder: FooterViewHolder
    var footerState = FOOTER_STATE_LOADING
        set(value) {
            field = footerState
            when (footerState) {
                FOOTER_STATE_HIDDEN -> footerViewHolder.root.visibility = View.GONE

                FOOTER_STATE_LOADING -> {
                    footerViewHolder.root.visibility = View.VISIBLE
                    footerViewHolder.progress.visibility = View.VISIBLE
                    footerViewHolder.msg.setText(R.string.footer_msg_loading)
                }

                FOOTER_STATE_NO_MORE -> {
                    footerViewHolder.root.visibility = View.VISIBLE
                    footerViewHolder.progress.visibility = View.GONE
                    footerViewHolder.msg.setText(hintNoMore)
                }

                FOOTER_STATE_FAILED -> {
                    footerViewHolder.root.visibility = View.VISIBLE
                    footerViewHolder.progress.visibility = View.GONE
                    footerViewHolder.msg.setText(R.string.footer_msg_failed)
                }
            }
        }

    init {
        @SuppressLint("InflateParams")
        val footerView = LayoutInflater.from(activity).inflate(R.layout.footer_recycler_view, null, false)
        footerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        footerViewHolder = FooterViewHolder(footerView)
        footerView.onClick {
            if (footerState == FOOTER_STATE_LOADING) return@onClick
            footerState = FOOTER_STATE_LOADING
            onLoadingMore()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is FooterViewHolder -> {
                footerState = FOOTER_STATE_LOADING
                onLoadingMore()
            }
            else -> onBindNormalViewHolder(holder as NV, position)

        }
    }

    override fun getItemCount() = normalItemCount + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        VIEW_TYPE_FOOTER -> footerViewHolder
        else -> onCreateNormalViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int)
            = if (position == normalItemCount) VIEW_TYPE_FOOTER else normalViewType

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root = itemView.footer_root
        val msg = itemView.footer_msg
        val progress = itemView.footer_progress
    }

    companion object {
        const val FOOTER_STATE_LOADING = 0
        const val FOOTER_STATE_NO_MORE = 1
        const val FOOTER_STATE_FAILED = 2
        const val FOOTER_STATE_HIDDEN = 3
        private const val VIEW_TYPE_NORMAL = 0
        private const val VIEW_TYPE_FOOTER = 1
    }
}