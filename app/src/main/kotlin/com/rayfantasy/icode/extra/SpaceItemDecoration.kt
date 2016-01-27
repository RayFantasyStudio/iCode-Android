package com.rayfantasy.icode.extra

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Allen on 2015/12/20 0020.
 */
class SpaceItemDecoration : RecyclerView.ItemDecoration() {

    var space: Int = 5
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent!!.getChildPosition(view) != 0)
            outRect!!.top = space
    }


}
