package com.rayfantasy.icode.ui.activity

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
import com.rayfantasy.icode.extension.alpha
import com.rayfantasy.icode.extension.loadPortrait
import com.rayfantasy.icode.extension.shadowColor
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.User
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import kotlinx.android.synthetic.main.activity_account.*
import org.jetbrains.anko.image

class AccountActivity :ActivityBase() {
    val glide by lazy { Glide.with(this) }
    val lf : LayoutInflater = LayoutInflater.from(this)
    lateinit  var views : MutableList<View>
    var titles : List<String> = listOf("代码","回复")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
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

        views.add(0,lf.inflate(R.layout.pager_code,null))
        views.add(1,lf.inflate(R.layout.pager_reply,null))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

}
