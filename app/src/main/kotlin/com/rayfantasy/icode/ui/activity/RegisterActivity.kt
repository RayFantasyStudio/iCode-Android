package com.rayfantasy.icode.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.android.volley.Request
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.snackBar
import com.rayfantasy.icode.extension.string
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.extension.e
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_register.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.onClick

class RegisterActivity : ActivityBase() {
    private companion object {
        const val PERMISSIONS_REQUEST_READ_PHONE_STATE = 0
    }

    private var request: Request<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        toolbar.setLogo(R.mipmap.ic_launcher)
        /*        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
                supportActionBar.setDefaultDisplayHomeAsUpEnabled(true)*/
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        register_fab.onClick {
            val password = register_et_password.string
            val username = register_et_username.string
            if (!checkArgs(username, password)) return@onClick
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED)
                alert {
                    title(R.string.title_request_permission)
                    message(R.string.msg_request_permission_read_phone_state)
                    positiveButton(android.R.string.ok, {
                        ActivityCompat.requestPermissions(this@RegisterActivity,
                                arrayOf(Manifest.permission.READ_PHONE_STATE), PERMISSIONS_REQUEST_READ_PHONE_STATE)
                    })
                    negativeButton(android.R.string.cancel, { onBackPressed() })
                    cancellable(false)
                    show()
                }
            else
                registerUser(username, password)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_PHONE_STATE -> if (grantResults!!.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val password = register_et_password.string
                val username = register_et_username.string
                registerUser(username, password)
            } else {
                register_fab.snackBar(R.string.error_request_permission)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun checkArgs(username: String, password: String): Boolean {
        if (username.length <= 3) {
            (register_et_username.parent as TextInputLayout).error = getString(R.string.validation_name_length)
            return false
        }
        if (password.length < 7) {
            (register_et_password.parent as TextInputLayout).error = getString(R.string.validation_password_length)
            return false
        }
        return true
    }

    fun registerUser(username: String, password: String) {
        if (request != null) return

        request = PostUtil.registerUser(
                username,
                password,
                {
                    register_fab.snackBar("欢迎加入iCode, ${it.username}")
                    request = null
                },
                { t, rc ->
                    e("failed, rc = $rc")
                    register_fab.snackBar("注册失败", Snackbar.LENGTH_LONG)
                    request = null
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        request?.let {
            PostUtil.cancel(request)
            request = null
        }
    }


    /*    @OnClick(R.id.register_iv_icon)
        internal fun setUserIcon() {
            val picUri: String
            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.setType("image/jpeg")
            startActivity(intent)
            picUri = intent.data.toString()



        }

        @OnClick(R.id.register_fab)
        internal fun register() {
            if (FormValidator.validate(this, TextInputLayoutCallback(this))) {
                val username = this.username.text.toString()
                val password = this.password.text.toString()
                postUtil?.registerUser(username, password, {
                    Toast.makeText(this@RegisterActivity, "欢迎加入iCode" + username, Toast.LENGTH_LONG).show()
                    finish()
                }, { t, rc ->
                    e("failed, rc =  $ rc")
                    Toast.makeText(this@RegisterActivity, "注册失败", Toast.LENGTH_LONG).show()
                })
            }
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    return true
                }
            }
            return super.onOptionsItemSelected(item)
        }*/
}




