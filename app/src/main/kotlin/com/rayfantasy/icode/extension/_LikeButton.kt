package com.rayfantasy.icode.extension

import com.like.LikeButton
import com.like.OnLikeListener
/**
 * Created by qweas on 2016/2/15 0015.
 */
class _OnLikeListener() : OnLikeListener{
    private var _liked: ((LikeButton) -> Unit)? = null
    private var _unLiked: ((LikeButton) -> Unit)? = null

    override fun liked(p0: LikeButton) {
        _liked?.invoke(p0)
    }

    override fun unLiked(p0: LikeButton) {
        _unLiked?.invoke(p0)
    }

    fun liked(listener: (LikeButton) -> Unit) {
        _liked = listener
    }

    fun unLiked(listener: (LikeButton) -> Unit) {
        _unLiked = listener
    }
}

fun LikeButton.onLike(init: _OnLikeListener.() -> Unit){
    setOnLikeListener(_OnLikeListener().apply(init))
}