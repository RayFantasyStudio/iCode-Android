package com.rayfantasy.icode.ui.activity


import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.rayfantasy.icode.R
import com.rayfantasy.icode.databinding.ActivityMainBinding
import com.rayfantasy.icode.databinding.NvLayoutBinding
import com.rayfantasy.icode.extension.alert
import com.rayfantasy.icode.extension.alpha
import com.rayfantasy.icode.extension.shadowColor
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.ACTION_USER_STATE_CHANGED
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.User
import com.rayfantasy.icode.ui.fragment.AboutFragment
import com.rayfantasy.icode.ui.fragment.FavoriteFragment
import com.rayfantasy.icode.ui.fragment.MainFragment
import com.rayfantasy.icode.ui.fragment.SettingFragment
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nv_layout.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity

class MainActivity : ActivityBase(), NavigationView.OnNavigationItemSelectedListener {

    private val aboutFragment by lazy { AboutFragment() }
    private val glide by lazy { Glide.with(this) }
    private val circleTransformation    by lazy { CropCircleTransformation(this) }
    private val favoriteFragment by lazy { FavoriteFragment() }
    private val mainFragment by lazy { MainFragment() }
    private val settingFragment by lazy { SettingFragment() }
    private lateinit var broadcastManager: LocalBroadcastManager
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            refreshUserState()
        }
    }
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.theme = ICodeTheme
        replaceFragment(mainFragment)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        refreshUserState()


        val headerView = nav_view.getHeaderView(0)
        NvLayoutBinding.bind(headerView).theme = ICodeTheme
        headerView.nv_user_icon.onClick {
            getAccount()
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        broadcastManager = LocalBroadcastManager.getInstance(this)
        broadcastManager.registerReceiver(receiver, IntentFilter(ACTION_USER_STATE_CHANGED))


    }

    private fun refreshUserState() {
        if (PostUtil.user != null) {
            nav_view.getHeaderView(0).nv_username.text = (PostUtil.user as User).username
            glide.load(PostUtil.getProfilePicUrl(PostUtil.user!!.username))
                    .bitmapTransform(circleTransformation)
                    .into(nav_view.getHeaderView(0).nv_user_icon)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        broadcastManager.unregisterReceiver(receiver)
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            alert(getString(R.string.exit_icode_msg), getString(R.string.app_name)) {
                positiveButton(getString(R.string.ok_btn)) { super.onBackPressed() }
                negativeButton(getString(R.string.no_btn)) {}
            }.show()
        }
    }

  /*  override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_exit){
            alert(getString(R.string.exit_icode_msg), getString(R.string.app_name)) {
                positiveButton(getString(R.string.ok_btn)) { super.onBackPressed() }
                negativeButton(getString(R.string.no_btn)) {}
            }.show()
        }

        return super.onOptionsItemSelected(item)
    }

    fun getAccount() {
        if (PostUtil.user == null) {
            startActivity<LoginActivity>()
        } else {
            startActivity<UserActivity>("username" to (PostUtil.user as User).username.toString())
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem?): Boolean {
        // Handle navigation view item clicks here.

        val id = item?.itemId

        when (id) {
            R.id.nav_home -> replaceFragment(mainFragment)
            R.id.nav_about -> replaceFragment(aboutFragment)
            R.id.nav_edit -> startActivity(Intent(this@MainActivity, WriteCodeActivity::class.java))
//            R.id.nav_favourite -> replaceFragment(favoriteFragment)
            R.id.nav_setting -> replaceFragment(settingFragment)
            R.id.nav_homepage -> OpenWeb()

        //etc...
            else -> return false
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun OpenWeb() {
        var uri = Uri.parse("http://www.rayfantasy.com:8088")
        var intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != mainFragment)
            app_bar.setExpanded(false)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.main_contain, fragment).commit()
    }
}

