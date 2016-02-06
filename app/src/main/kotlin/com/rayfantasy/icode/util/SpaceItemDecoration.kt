package com.rayfantasy.icode.util

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by qweas on 2016/2/6 0006.
 */

public class SpaceItemDecoration() : RecyclerView.ItemDecoration(){
    private   var space : Int = 0
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect!!.top = space
    }
}