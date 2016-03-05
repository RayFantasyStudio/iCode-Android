package com.rayfantasy.icode.ui.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View

/**
 * Created by qweas on 2016/2/29.
 */
class AccountAdapter(val ctx : Context,var views : MutableList<View>) : PagerAdapter() {

    override fun isViewFromObject(p0: View?, p1: Any?): Boolean = p0 == p1
    override fun getCount(): Int  = views.size
    override fun destroyItem(container: View?, position: Int, `object`: Any?) {
        super.destroyItem(container, position, `object`)
    }

    override fun instantiateItem(container: View?, position: Int): Any? {
        return super.instantiateItem(container, position)
    }
}