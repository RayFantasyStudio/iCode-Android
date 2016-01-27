package com.rayfantasy.icode.ui


import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.extension.e
import kotlinx.android.synthetic.main.content_login.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity

class LoginActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        /*
                register.setOnClickListener { startActivity<RegisterActivity>() }
                fab.setOnClickListener {
                    if (FormValidator.validate(this, TextInputLayoutCallback(this))) {
                        val username = etUsername.text.toString()
                        val password = etUsername.text.toString()
                        postUtil?.loginUser(username, password, {

                            Toast.makeText(this@LoginActivity, "欢迎回来$username", Toast.LENGTH_SHORT).show()
                            finish()

                        }, { t, rc ->
                            t.printStackTrace()
                            Toast.makeText(this@LoginActivity, "用户${username}登陆失败", Toast.LENGTH_SHORT).show()
                        })
                    }
                }
            }*/
        login_fab.onClick {
            PostUtil.loginUser(
                    login_et_username.text.toString(),
                    login_et_password.text.toString(),
                    onSuccess = { loginSucceed() },
                    onFailed = { t, rc ->
                        e("failed, rc =  $rc")
                        /*throw RuntimeException("$rc");*/
                        Toast.makeText(this, "登陆失败", Toast.LENGTH_LONG).show()
                    }
            )

        }
        register_button.onClick {
            startActivity<RegisterActivity>()
        }
        /*login_tv_register.onClick {
            startActivity<RegisterActivity>()
        }*/
    }

    fun loginSucceed() {
        Toast.makeText(this, "欢迎回来" + PostUtil.user?.username, Toast.LENGTH_SHORT).show()
        finish()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
