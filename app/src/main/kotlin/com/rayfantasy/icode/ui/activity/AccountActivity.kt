package com.rayfantasy.icode.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.bumptech.glide.Glide
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.PostUtil
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.content_account.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import java.io.File

class AccountActivity : ActivityBase() {
    final val REQUEST_SELECT_PICTURE: Int = 0x01
    lateinit var tagetUri: Uri
    lateinit var DestinationUri: Uri
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
        account_btn_change.onClick {
            getIcon()


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SELECT_PICTURE) {
            if (data?.data == null) {
                Toast.makeText(this, "被用户取消", Toast.LENGTH_SHORT).show()
            } else {
                tagetUri = data!!.data
                UCrop.of(tagetUri, DestinationUri)
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(100, 100)
                        .start(this)
            }

        }
        if (requestCode == UCrop.REQUEST_CROP) {
            val resultUri: Uri = UCrop.getOutput(data)
            Glide
                    .with(this)
                    .load(resultUri)
                    .into(account_iv_usericon)
            PostUtil.uploadProfilePic(File(resultUri.toString()), {
                Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show()
            }, { t, rc ->
                toast("rc = $rc")
                t.printStackTrace()
            })
            var cache: File = File(resultUri.toString())
            cache.delete()


        } else if (resultCode == UCrop.RESULT_ERROR) {
            val UCropError: Throwable = UCrop.getError(data)
            UCropError.cause
        }
    }

    fun getIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), R.string.title_request_permission)
        } else {
            val intent: Intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(intent, "选择头像"), REQUEST_SELECT_PICTURE)

        }
    }
}

