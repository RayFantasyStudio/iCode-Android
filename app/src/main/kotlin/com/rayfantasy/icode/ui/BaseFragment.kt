package com.rayfantasy.icode.ui

import android.app.Fragment
import com.rayfantasy.icode.BaseApplication

abstract class BaseFragment : Fragment() {
    override fun onDestroy() {
        super.onDestroy()
        val refWatcher = BaseApplication.getRefWatcher(activity)
        refWatcher.watch(this)
    }
}