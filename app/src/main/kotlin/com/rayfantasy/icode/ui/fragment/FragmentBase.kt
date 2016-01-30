package com.rayfantasy.icode.ui.fragment

import android.app.Fragment
import com.rayfantasy.icode.BaseApplication

abstract class FragmentBase : Fragment() {
    override fun onDestroy() {
        super.onDestroy()
        val refWatcher = BaseApplication.getRefWatcher(activity)
        refWatcher.watch(this)
    }
}