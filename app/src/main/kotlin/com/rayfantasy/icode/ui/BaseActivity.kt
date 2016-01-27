package com.rayfantasy.icode.ui

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.rayfantasy.icode.BaseApplication
import com.rayfantasy.icode.R
import org.jetbrains.anko.find

abstract class BaseActivity : AppCompatActivity() {
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        val toolbar = find<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        val refWatcher = BaseApplication.getRefWatcher(this)
        refWatcher.watch(this)
    }
}