package com.rayfantasy.icode.ui.activity

import android.app.ActivityManager
import android.databinding.Observable
import android.graphics.BitmapFactory
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
    open val bindTaskDescription = false
    private val callback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            private val icon by lazy {
                BitmapFactory.decodeResource(resources, R.mipmap.ic_task_desc)
            }

            private val appName by lazy {
                getString(R.string.app_name)
            }

            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (bindingStatus) {
                    val colorPrimaryDark = ICodeTheme.colorPrimaryDark.get()
                    window.statusBarColor = colorPrimaryDark
                }
                if (bindTaskDescription) {
                    val colorPrimary = ICodeTheme.colorPrimary.get()
                    val tDesc = ActivityManager.TaskDescription(appName, icon, colorPrimary)
                    setTaskDescription(tDesc)
                }
            }
        }
    }
    protected val toolbar: Toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
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
        configuration(fromSdk = Build.VERSION_CODES.LOLLIPOP) {
            if (bindingStatus || bindTaskDescription) {
                callback.onPropertyChanged(null, 0)
                ICodeTheme.colorPrimaryDark.addOnPropertyChangedCallback(callback)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        configuration(fromSdk = Build.VERSION_CODES.LOLLIPOP) {
            if (bindingStatus || bindTaskDescription)
                ICodeTheme.colorPrimaryDark.removeOnPropertyChangedCallback(callback)
        }
    }
}