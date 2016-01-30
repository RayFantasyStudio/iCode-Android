package com.rayfantasy.icode.ui.activity


import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.ACTION_USER_STATE_CHANGED
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.ui.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nv_layout.view.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity

class MainActivity : ActivityBase(), NavigationView.OnNavigationItemSelectedListener {

    private val aboutFragment = AboutFragment()
    //var db_manager: DBManager = DBManager(this)
    private val favoriteFragment = FavoriteFragment()
    private val mainFragment = MainFragment()
    private val payFragment = PayFragment()
    private val settingFragment = SettingFragment()
    private lateinit var broadcastManager: LocalBroadcastManager
    private var username: String = "未登录"
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            refreshUserState()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(mainFragment)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()
        //db_manager.initTable(this)
        nav_view.setNavigationItemSelectedListener(this)

        refreshUserState()


        nav_view.getHeaderView(0).nv_user_icon.onClick {
            /*startActivity(new Intent(MainActivity.this, LoginActivity.class));*/
            getAccount()
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        broadcastManager = LocalBroadcastManager.getInstance(this)
        broadcastManager.registerReceiver(receiver, IntentFilter(ACTION_USER_STATE_CHANGED))
        Glide.with(this)

        //main_fab
        main_fab.onClick {

            startActivity(Intent(this, WriteCodeActivity::class.java))
        }


    }

    private fun refreshUserState() {
        val username1 = PostUtil.user?.username
        nav_view.getHeaderView(0).nv_username.text = username1
        /*      Glide.with(this@MainActivity).load(postUtil.getProfilePicUrl(username1!!)).into(nav_view.getHeaderView(0).nv_user_icon)*/
    }


    override fun onDestroy() {
        super.onDestroy()
        broadcastManager.unregisterReceiver(receiver)
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun getAccount() {
        if (PostUtil!!.user == null) {
            startActivity<LoginActivity>()
        } else {
            startActivity<AccountActivity>()
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
            R.id.nav_favourite -> replaceFragment(favoriteFragment)
            R.id.nav_setting -> replaceFragment(settingFragment)
            R.id.nav_loyalty -> replaceFragment(payFragment)

        //etc...
            else -> return false
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.main_contain, fragment).commit()
    }

    override fun onResume() {
        super.onResume()
        /*        if(postUtil?.user != null){
            username = postUtil!!.user!!.username.toString()
            nv_username.text = username
        }
    }*/
    }
}

