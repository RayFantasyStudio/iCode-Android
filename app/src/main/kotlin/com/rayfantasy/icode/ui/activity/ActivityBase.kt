package com.rayfantasy.icode.ui.activity

import android.databinding.Observable
import android.os.Build
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.rayfantasy.icode.BaseApplication
import com.rayfantasy.icode.R
import com.rayfantasy.icode.model.ICodeTheme
import org.jetbrains.anko.configuration
import org.jetbrains.anko.find

abstract class ActivityBase : ActivityConverter() {
    open val bindingStatus = false
    private val callback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                window.statusBarColor = ICodeTheme.colorPrimaryDark.get()
            }
        }
    }

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

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> false
    }

    override fun onResume() {
        super.onResume()
        if (bindingStatus)
            configuration(fromSdk = Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ICodeTheme.colorPrimaryDark.get()
                ICodeTheme.colorPrimaryDark.addOnPropertyChangedCallback(callback)
            }
    }

    override fun onPause() {
        super.onPause()
        if (bindingStatus)
            configuration(fromSdk = Build.VERSION_CODES.LOLLIPOP) {
                ICodeTheme.colorPrimaryDark.removeOnPropertyChangedCallback(callback)
            }
    }
}