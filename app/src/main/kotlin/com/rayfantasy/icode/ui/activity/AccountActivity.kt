package com.rayfantasy.icode.ui.activity

import android.app.Fragment
import android.database.DatabaseUtils
import android.databinding.DataBindingUtil
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import com.bumptech.glide.Glide

import com.rayfantasy.icode.R
import com.rayfantasy.icode.databinding.ActivityAccountBinding
import com.rayfantasy.icode.extension.alpha
import com.rayfantasy.icode.extension.loadPortrait
import com.rayfantasy.icode.extension.shadowColor
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.User
import com.rayfantasy.icode.ui.fragment.AccountCodeFragment
import com.rayfantasy.icode.ui.fragment.MainFragment
import com.rayfantasy.icode.ui.fragment.ReplyFragment
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.content_account.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.image
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity

class AccountActivity :ActivityBase() {
    val glide by lazy { Glide.with(this) }
    private val codeFragment by lazy { AccountCodeFragment() }
    private val replysFragment by lazy { ReplyFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityAccountBinding>(this, R.layout.activity_account).theme = ICodeTheme
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            startActivity<AccountSettingActivity>()
        }
        val username : String = PostUtil.user!!.username
        account_pic.loadPortrait(username)
        account_username.text = username
        val colorDrawable = ColorDrawable(username.hashCode().alpha(0xff).shadowColor())
        glide.load(PostUtil.getProfilePicUrl(username))
                .error(colorDrawable)
                .placeholder(colorDrawable)
                .bitmapTransform(CropTransformation(this, account_bg_pic.width, account_bg_pic.height),
                        BlurTransformation(this, 15, 4))
                .into(account_bg_pic)
        replaceFragment(codeFragment)
        account_btn_code.onClick {
            replaceFragment(codeFragment)

        }
        account_btn_reply.onClick {
            replaceFragment(replysFragment)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.account_fragment, fragment).commit()
    }
    private fun  getFragment(){

    }
}
