package com.rayfantasy.icode.editor

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.KeyEvent
import com.rayfantasy.icode.R


class ScrollViewText constructor(context: Context, attrs: AttributeSet? = null) : NestedScrollView(context, attrs) {

    private var rowHeight: Int = 0
    private var currRow: Int = 0
    private var totalRows: Int = 0
    private var horScrollText: HorScrollViewText? = null


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (horScrollText == null) {
            horScrollText = findViewById(R.id.horScrollViewText) as HorScrollViewText
        }
        rowHeight = horScrollText!!.rowHeight
        currRow = horScrollText!!.currRow
        totalRows = horScrollText!!.currRow
    }


    override fun executeKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_DEL) {
            if (totalRows * rowHeight <= height / 2) {
                scrollTo(scrollX, 0)
            }
        }
        return super.executeKeyEvent(event)
    }


    override fun computeScroll() {
        super.computeScroll()
        horScrollText!!.containerView?.scrollY(scrollY)

        horScrollText!!.containerView?.getViewHeight(height)
    }

}
