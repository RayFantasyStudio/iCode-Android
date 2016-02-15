package com.rayfantasy.icode.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rayfantasy.icode.R

class FavoriteFragment : FragmentBase() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)

    }


    override fun onResume() {
        super.onResume()

    }
}
