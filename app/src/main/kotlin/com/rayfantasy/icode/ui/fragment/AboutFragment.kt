package com.rayfantasy.icode.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rayfantasy.icode.BuildConfig
import com.rayfantasy.icode.R
import kotlinx.android.synthetic.main.fragment_about.*


class AboutFragment : PreferenceFragment() {
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPreferencesFromResource(R.xml.about)
        val OSWeb = findPreference("OSWeb")
        OSWeb.onPreferenceClickListener = OnPreferenceClickListener {
            var uri = Uri.parse("https://github.com/RayFantasyStudio/iCode-Android")
            var intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
            true
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }
}
