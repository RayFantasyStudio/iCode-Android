package com.rayfantasy.icode.ui.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.snackBar
import com.rayfantasy.icode.postutil.PostUtil
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_account_setting.*
import kotlinx.android.synthetic.main.content_account_setting.*
import org.jetbrains.anko.onClick

class AccountSettingActivity : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
       account_setting_fab.onClick {

           if (et_newPwd.text != null && et_oldPwd.text != null && et_newPwd_check.text != null){
               var oldPwd : String = et_oldPwd.text.toString()
               var newPwd : String = et_newPwd.text.toString()
               var newPwd_check : String = et_newPwd_check.text.toString()
               if(!checkArgs(oldPwd,newPwd,newPwd_check)) return@onClick
               resPwd(oldPwd,newPwd)
           }
           else account_setting_fab.snackBar("请检查你的输入",Snackbar.LENGTH_LONG)
       }
    }
    fun resPwd(oldPwd: String,newPwd: String){
        PostUtil.resetPwd(oldPwd,newPwd,
                {account_setting_fab.snackBar("修改密码成功",Snackbar.LENGTH_LONG)},
                {t,rc ->account_setting_fab.snackBar("修改密码失败，错误代码:$rc",Snackbar.LENGTH_LONG)}
        )
    }
    fun checkArgs(oldPwd: String, newPwd: String, newPwd_check: String): Boolean {
        if (et_oldPwd.text.length <= 6) {
            (et_oldPwd.parent as TextInputLayout).error = getString(R.string.validation_password_length)
            return false
        }
        if (newPwd.length < 6) {
            (et_newPwd.parent as TextInputLayout).error = getString(R.string.validation_password_length)
            return false
        }
        if (!(newPwd.equals(newPwd_check))){
            account_setting_fab.snackBar("确认密码不匹配，请检查后重新输入",Snackbar.LENGTH_LONG)
            return false
        }
        return true
    }

}
