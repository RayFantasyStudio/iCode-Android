/*
 * Copyright 2016 Alex Zhang aka. ztc1997
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

package com.rayfantasy.icode.ui

import android.content.Intent
import android.os.Build
import com.rayfantasy.icode.BuildConfig
import kotlinx.android.synthetic.main.app_bar_main.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricGradleTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.LOLLIPOP), constants = BuildConfig::class)
class MainActivityTest {
    @Test
    fun testMainActivity() {
        val mainActivity: MainActivity = Robolectric.setupActivity(MainActivity::class.java)
        mainActivity.main_fab.performClick()
        val expectedIntent = Intent(mainActivity, WriteCodeActivity::class.java)
        val shadowActivity = Shadows.shadowOf(mainActivity)
        val actualIntent = shadowActivity.nextStartedActivityForResult
        Assert.assertEquals(expectedIntent, actualIntent)
    }
}