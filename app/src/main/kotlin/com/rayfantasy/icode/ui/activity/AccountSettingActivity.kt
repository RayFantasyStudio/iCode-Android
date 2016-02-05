package com.rayfantasy.icode.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import com.amulyakhare.textdrawable.TextDrawable
import com.bumptech.glide.Glide
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.snackBar
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.extension.e
import com.yalantis.ucrop.UCrop
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_account_setting.*
import kotlinx.android.synthetic.main.content_account_setting.*
import org.jetbrains.anko.onClick
import java.io.File
import java.net.URI

class AccountSettingActivity : ActivityBase() {
    private val glide by lazy { Glide.with(this) }
    private val circleTransformation by lazy { CropCircleTransformation(this) }
    final val REQUEST_SELECT_PICTURE: Int = 0x01
    lateinit var tagetUri: Uri
    lateinit var DestinationUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        DestinationUri = Uri.fromFile(File(cacheDir, "Cache_Icon.jpeg"))
        account_setting_fab.onClick {

            if (et_newPwd.text != null && et_oldPwd.text != null && et_newPwd_check.text != null) {
                var oldPwd: String = et_oldPwd.text.toString()
                var newPwd: String = et_newPwd.text.toString()
                var newPwd_check: String = et_newPwd_check.text.toString()
                if (!checkArgs(oldPwd, newPwd, newPwd_check)) return@onClick
                resPwd(oldPwd, newPwd)
            } else account_setting_fab.snackBar("请检查你的输入", Snackbar.LENGTH_LONG)
        }
        account_setting_icon.onClick {
            changeUserIcon()
        }
        val str: String = PostUtil.user!!.username
        val icon: TextDrawable = TextDrawable.builder().buildRound((str[0]).toString(), str.hashCode())
        glide.load(PostUtil.getProfilePicUrl(str)).error(icon).bitmapTransform(circleTransformation).into(account_setting_icon)
    }

    //重置密码
    private fun resPwd(oldPwd: String, newPwd: String) {
        PostUtil.resetPwd(oldPwd, newPwd,
                { account_setting_fab.snackBar("修改密码成功", Snackbar.LENGTH_LONG) },
                { t, rc -> account_setting_fab.snackBar("修改密码失败，错误代码:$rc", Snackbar.LENGTH_LONG) }
        )
    }

    //检查密码
    private fun checkArgs(oldPwd: String, newPwd: String, newPwd_check: String): Boolean {
        if (et_oldPwd.text.length <= 6) {
            (et_oldPwd.parent as TextInputLayout).error = getString(R.string.validation_password_length)
            return false
        }
        if (newPwd.length < 6) {
            (et_newPwd.parent as TextInputLayout).error = getString(R.string.validation_password_length)
            return false
        }
        if (!(newPwd.equals(newPwd_check))) {
            account_setting_fab.snackBar("确认密码不匹配，请检查后重新输入", Snackbar.LENGTH_LONG)
            return false
        }
        return true
    }

    private fun changeUserIcon() {
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


    //选择图片返回，UCrop返回
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SELECT_PICTURE) {
            if (data?.data == null) {
                account_setting_fab.snackBar("被用户取消操作", Snackbar.LENGTH_LONG)
            } else {
                tagetUri = data!!.data
                UCrop.of(tagetUri, DestinationUri)
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(this)
            }

        }
        if (requestCode == UCrop.REQUEST_CROP && data is Intent) {
            val resultUri = UCrop.getOutput(data)
            Glide
                    .with(this)
                    .load(resultUri)
                    .bitmapTransform(CropCircleTransformation(this))
                    .error(R.mipmap.ic_account_box_black)
                    .into(account_setting_icon)
            val cache: File = File(URI(resultUri.toString()))
            e("exist = ${cache.exists()}, length = ${cache.length()}")
            PostUtil.uploadProfilePic(cache, {
                account_setting_fab.snackBar(getString(R.string.upload_success), Snackbar.LENGTH_LONG)
                cache.delete()
            }, { t, rc ->
                account_setting_fab.snackBar("${getString(R.string.cannot_upload)}${com.rayfantasy.icode.util.error("uploadProfilePic", rc, this) }", Snackbar.LENGTH_LONG)
                t.printStackTrace()
            })
        } else if (resultCode == UCrop.RESULT_ERROR && data is Intent) {
            val UCropError = UCrop.getError(data)
            UCropError?.cause
        }
    }

}
