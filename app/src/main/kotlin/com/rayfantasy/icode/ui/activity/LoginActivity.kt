package com.rayfantasy.icode.ui.activity


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.android.volley.Request
import com.rayfantasy.icode.R
import com.rayfantasy.icode.databinding.ActivityLoginBinding
import com.rayfantasy.icode.extension.snackBar
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.extension.e
import kotlinx.android.synthetic.main.content_login.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity

class LoginActivity : ActivityBase() {
    private var request: Request<*>? = null

    override val bindingStatus: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login).theme = ICodeTheme
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        login_fab.onClick {
            request = PostUtil.loginUser(
                    login_et_username.text.toString(),
                    login_et_password.text.toString()) {
                onSuccess {
                    loginSucceed()
                    request = null
                }
                onFailed { throwable, rc ->
                    e("failed, rc =  $rc")
                    login_fab.snackBar("${getString(R.string.sign_in_failed)}" +
                            "${com.rayfantasy.icode.util.error("loginUser", rc, this@LoginActivity) }", Snackbar.LENGTH_LONG)
                    request = null
                }
            }

        }
        register_button.onClick {
            startActivity<RegisterActivity>()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        request?.let {
            PostUtil.cancel(request)
            request = null
        }
    }

    fun loginSucceed() {
        login_fab.snackBar("欢迎回来" + PostUtil.user?.username, Snackbar.LENGTH_LONG)
        finish()
    }
}
