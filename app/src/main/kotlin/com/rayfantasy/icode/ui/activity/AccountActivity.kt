package com.rayfantasy.icode.ui.activity

import android.app.Fragment
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.rayfantasy.icode.R
import com.rayfantasy.icode.databinding.ActivityAccountBinding
import com.rayfantasy.icode.extension.alpha
import com.rayfantasy.icode.extension.loadPortrait
import com.rayfantasy.icode.extension.shadowColor
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.ui.fragment.AccountCodeFragment
import com.rayfantasy.icode.ui.fragment.ReplyFragment
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.content_account.*
import kotlinx.android.synthetic.main.nv_layout.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult

class AccountActivity :ActivityBase() {
    val glide by lazy { Glide.with(this) }
    private val codeFragment by lazy { AccountCodeFragment() }
    private val repliesFragment by lazy { ReplyFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityAccountBinding>(this, R.layout.activity_account).theme = ICodeTheme
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fab.setOnClickListener {
           startActivityForResult(Intent(this,AccountSettingActivity::class.java),0)
        }
        val username : String = PostUtil.user!!.username
        account_pic.loadPortrait(username)
        toolbar_layout.title = username

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
            replaceFragment(repliesFragment)

        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.account_menu_out -> {
                alert(getString(R.string.exit_user_msg), getString(R.string.app_name)) {
                    positiveButton(getString(R.string.ok_btn)) {
                        PostUtil.logoutUser()
                        nv_user_icon.setImageDrawable(resources.getDrawable(R.mipmap.ic_nv_user))
                        nv_username.text = getText(R.string.not_Login)

                        finish()

                    }
                    negativeButton(getString(R.string.no_btn)) { }
                }.show()

                return true
            }
            android.R.id.home -> {
                super.onBackPressed()
                return  true
            }
        }
        return false
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.account_fragment, fragment).commit()
    }
    private fun  getFragment(){

    }
}
