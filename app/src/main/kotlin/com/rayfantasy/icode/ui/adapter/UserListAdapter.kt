package com.rayfantasy.icode.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.Select
import com.rayfantasy.icode.R
import com.rayfantasy.icode.databinding.ItemCodeListBinding
import com.rayfantasy.icode.extension.*
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.bean.Favorite
import com.rayfantasy.icode.postutil.bean.Favorite_Table
import com.rayfantasy.icode.ui.activity.startBlockActivity
import com.rayfantasy.icode.util.ms2RelativeDate
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import kotlinx.android.synthetic.main.footer_recycler_view.view.*
import kotlinx.android.synthetic.main.item_code_list.view.*
import kotlinx.android.synthetic.main.item_recycler_user.view.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

/**
 * Created by qweas on 2016/1/22 0022.
 */
class UserListAdapter(val activity: Activity, var username: String, var codeGoods: MutableList<CodeGood>, private val onLoadingMore: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val glide by lazy { Glide.with(activity) }
    private val circleTransformation by lazy { CropCircleTransformation(activity) }
    private var footerState: Int = 0
    private var hintNoMore = R.string.footer_msg_no_more
    private val footerViewHolder: FooterViewHolder

    companion object {
        const val VIEW_TYPE_HEADER = -1
        const val VIEW_TYPE_NORMAL = -2
        val FOOTER_STATE_LOADING = 0
        val FOOTER_STATE_NO_MORE = 1
        val FOOTER_STATE_FAILED = 2
        val FOOTER_STATE_HIDDEN = 3
        private val VIEW_TYPE_FOOTER = 1
    }

    override fun getItemCount() = codeGoods.size + 1

    init {
        @SuppressLint("InflateParams")
        val footerView = LayoutInflater.from(activity).inflate(R.layout.footer_recycler_view, null, false)
        footerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        footerViewHolder = FooterViewHolder(footerView)
        footerView.onClick {
            if (footerState == FOOTER_STATE_LOADING) return@onClick
            setFooterState(FOOTER_STATE_LOADING)
            onLoadingMore()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

        when (holder) {
            is UserViewHolder -> {
                holder.username.text = username
                holder.usericon.loadPortrait(username)

                val colorDrawable = ColorDrawable(username.hashCode().alpha(0xff).shadowColor())
                glide.load(PostUtil.getProfilePicUrl(username))
                        .error(colorDrawable)
                        .placeholder(colorDrawable)
                        .bitmapTransform(CropTransformation(activity, holder.user_bg.width, holder.user_bg.height),
                                BlurTransformation(activity, 15, 4))
                        .into(holder.user_bg)
            }
            is CodeViewHolder -> {
                val codeGood = codeGoods[position - 1]

                holder.title.text = codeGood.title
                holder.subTitle.text = codeGood.subtitle
                holder.time.text = ms2RelativeDate(activity, codeGood.createAt!!)
                holder.username.text = codeGood.username
                holder.binding.highlight = codeGood.highlight ?: false
                holder.pic.loadPortrait(username)
                val favorite = Select().from(Favorite::class.java).where(Favorite_Table.goodId.`is`(codeGood.id)).querySingle()
                holder.like.setLiked(favorite != null)
                holder.like.onLike {
                    liked {
                        PostUtil.addFavorite(codeGood.id) {
                            onSuccess {
                                activity.toast("成功")
                                Favorite(codeGood.id, System.currentTimeMillis()).save()
                                holder.like_count.text = "被收藏${codeGood.favorite + 1 }次"
                            }
                            onFailed { throwable, rc -> activity.toast("失败,rc = $rc") }
                        }
                    }
                    unLiked {
                        PostUtil.delFavorite(codeGood.id) {
                            onSuccess {
                                activity.toast("成功")
                                holder.like_count.text = "被收藏${codeGood.favorite - 1}次"
                                Delete()
                                        .from(Favorite::class.java)
                                        .where(Favorite_Table.goodId.`is`(codeGood.id))
                                        .execute()
                            }
                            onFailed { throwable, rc -> activity.toast("失败,rc = $rc") }
                        }
                    }
                }
                holder.reply_count.text = "共${codeGood.reply}条回复"
                holder.like_count.text = "被收藏${codeGood.favorite}次"
                holder.bg.onClick { holder.bg.startBlockActivity(codeGood, false) }
            }
            is FooterViewHolder -> {
                setFooterState(FOOTER_STATE_LOADING)
                onLoadingMore()
            }
        }
    }

    override fun getItemViewType(position: Int)
            = if (position == 0) VIEW_TYPE_HEADER else if (position == getItemCount() + 2) VIEW_TYPE_FOOTER else VIEW_TYPE_NORMAL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        VIEW_TYPE_HEADER -> UserViewHolder(parent.inflate(R.layout.item_recycler_user))
        VIEW_TYPE_NORMAL -> CodeViewHolder(parent.inflate(R.layout.item_code_list))
        VIEW_TYPE_FOOTER -> FooterViewHolder(parent.inflate(R.layout.footer_recycler_view))
        else -> null
    }

    fun setFooterState(footerState: Int) {
        this.footerState = footerState
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

    fun setHintNoMore(hintNoMore: Int) {
        this.hintNoMore = hintNoMore
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username = itemView.user_item_username
        var usericon = itemView.user_item_avatar
        var user_bg = itemView.user_item_bg
    }

    class CodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root = itemView.footer_root
        val msg = itemView.footer_msg
        val progress = itemView.footer_progress
    }


}