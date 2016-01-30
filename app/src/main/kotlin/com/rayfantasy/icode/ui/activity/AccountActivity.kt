package com.rayfantasy.icode.ui.activity

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.PostUtil
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.content_account.*
import org.jetbrains.anko.onClick

class AccountActivity : ActivityBase() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setLogo(R.mipmap.ic_launcher)
        /*        username.setText(postUtil?.user!!.username)
                account_fab.setOnClickListener { startActivity<AccountCodeActivity>() }
                account_btn_login_out.setOnClickListener {
                    postUtil?.logoutUser()
                    nv_username.text = "未登录"
                    finish()*/
        account_btn_login_out.onClick {
            PostUtil.logoutUser()
            finish()
        }
        account_tv_username.text = PostUtil.user?.username.toString()
        Glide
                .with(this)
                .load(PostUtil.getProfilePicUrl(PostUtil.user!!.username!!))
                .into(account_iv_usericon)
        account_fab.onClick {
            val picUri: String
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.setType("image/jpeg")
            startActivity(intent)
            picUri = intent.data.toString()


        }
        account_fab.onClick {
            startActivity(Intent(AccountActivity@this, AccountCodeActivity::class.java))
        }

    }

}

