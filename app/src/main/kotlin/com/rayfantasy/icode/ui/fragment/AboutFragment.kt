package com.rayfantasy.icode.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rayfantasy.icode.BuildConfig
import com.rayfantasy.icode.R
import kotlinx.android.synthetic.main.fragment_about.*


class AboutFragment : FragmentBase() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        /*AboutFragment_iv_version?.text = getVersion()*/
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_version.text = getString(R.string.version, BuildConfig.VERSION_NAME)
    }
}
