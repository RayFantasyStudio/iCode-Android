package com.rayfantasy.icode.ui.activity

import android.os.Bundle
import com.github.paolorotolo.appintro.AppIntro2
import com.rayfantasy.icode.BaseApplication
import com.rayfantasy.icode.R
import com.rayfantasy.icode.view.SampleSlide
import org.jetbrains.anko.startActivity

/**
 * Created by Allen on 2015/11/14 0014.
 */
class IntroductionView : AppIntro2() {


    override fun init(savedInstanceState: Bundle) {
        addSlide(SampleSlide.newInstance(R.layout.introduction_view_first))
        addSlide(SampleSlide.newInstance(R.layout.introduction_view_second))
        addSlide(SampleSlide.newInstance(R.layout.introduction_view_third))
        setFadeAnimation()
    }

    override fun onDonePressed() {
        /*        MyApplication.getInstance().editint(PREF_KEY_LAST_VERSION, BuildConfig.VERSION_CODE);*/
        startActivity<MainActivity>()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        val refWatcher = BaseApplication.getRefWatcher(this)
        refWatcher.watch(this)
    }

    companion object {
        const val PREF_KEY_LAST_VERSION = "pref_key_last_version"
    }

}