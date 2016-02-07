/*
 * Copyright 2015 Alex Zhang aka. ztc1997
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.rayfantasy.icode

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import com.raizlabs.android.dbflow.config.FlowManager
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.extension.v
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.tencent.bugly.crashreport.CrashReport
import org.evilbinary.managers.ConfigureManager
import kotlin.properties.Delegates

var CACHE_PATH by Delegates.notNull<String>()
    private set
var FILES_PATH by Delegates.notNull<String>()
    private set

class BaseApplication : Application() {

    private lateinit var refWatcher: RefWatcher

    override fun onCreate() {
        super.onCreate()

        instence = this

        //初始化PostUtil
        PostUtil.init(this)
        CACHE_PATH = cacheDir.path
        FILES_PATH = filesDir.path
        refWatcher = LeakCanary.install(this)

        val appInfo = this.packageManager
                .getApplicationInfo(packageName,
                        PackageManager.GET_META_DATA)
        val BUGLY_APP_ID = appInfo.metaData.getInt("BUGLY_APP_ID").toString()

        CrashReport.initCrashReport(this, BUGLY_APP_ID, false);
        v("BUGLY_APP_ID = $BUGLY_APP_ID")

        val configureManager = ConfigureManager(this)
        configureManager.exractDefaultConfigure()

        FlowManager.init(this)

        ICodeTheme.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        FlowManager.destroy()
    }

    companion object {
        lateinit var instence: BaseApplication

        @JvmStatic
        fun getInstance() = instence

        fun getRefWatcher(context: Context): RefWatcher {
            val application: BaseApplication = context.applicationContext as BaseApplication;
            return application.refWatcher;
        }

    }
}
