package com.rayfantasy.icode.ui.activity

import android.os.Bundle
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.snackBar
import kotlinx.android.synthetic.main.activity_account_setting.*
import org.jetbrains.anko.onClick

class AccountSettingActivity : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)

        fab.onClick { it?.snackBar("Replace with your own action") }
    }

}
