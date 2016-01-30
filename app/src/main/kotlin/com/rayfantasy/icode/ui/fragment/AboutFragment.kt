package com.rayfantasy.icode.ui.fragment

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rayfantasy.icode.R


class AboutFragment : FragmentBase() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        /*AboutFragment_iv_version?.text = getVersion()*/
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    fun getVersion(): String {
        var app_version: String
        var pm: PackageManager = context.packageManager
        var pi: PackageInfo = pm.getPackageInfo(context.packageManager.toString(), 0)
        app_version = pi.versionName
        return app_version
    }
}
