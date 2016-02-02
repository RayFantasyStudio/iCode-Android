/*
 * Copyright 2015 Alex Zhang aka. ztc1997
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.rayfantasy.icode.postutil

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.content.LocalBroadcastManager
import android.telephony.TelephonyManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestHandle
import com.loopj.android.http.RequestParams
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.bean.Favorite
import com.rayfantasy.icode.postutil.bean.Reply
import com.rayfantasy.icode.postutil.bean.User
import com.rayfantasy.icode.postutil.exception.PostException
import com.rayfantasy.icode.postutil.extension.fromJson
import com.rayfantasy.icode.postutil.extra.EncryptedRequest
import com.rayfantasy.icode.postutil.extra.OkHttpStack
import com.rayfantasy.icode.postutil.util.RSAUtils
import com.rayfantasy.icode.postutil.util.RSA_KEY
import com.rayfantasy.icode.postutil.util.base64Encode
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject
import java.io.File

object PostUtil {
    val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    private lateinit var requestQueue: RequestQueue
    private lateinit var preferences: SharedPreferences
    private lateinit var broadcastManager: LocalBroadcastManager
    var user: User? = null
        private set
    private var key: String? = null
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context.applicationContext
        requestQueue = Volley.newRequestQueue(this.context, OkHttpStack())
        preferences = this.context.getSharedPreferences("PostUtil", Context.MODE_PRIVATE)
        broadcastManager = LocalBroadcastManager.getInstance(this.context.applicationContext)
        loadUserInfo()
    }

    /**
     * 向服务器数据库插入一个CodeGood

     * @param codeGood 需要插入的CodeGood实例
     * *
     */
    fun insertCodeGood(codeGood: CodeGood, onSuccess: () -> Unit, onFailed: (Throwable, Int) -> Unit): Request<out Any>? {
        if (user == null || key == null) {
            onFailed(PostException("登陆后才能进行此操作"), -4)
            return null
        }
        codeGood.username = user!!.username
        val data = JSONObject()
        data.put("codeGood", gson.toJson(codeGood))
        data.put("key", key)
        val request = EncryptedRequest(URL_ADD_CODEGOOD, data.toString(), { onSuccess() }, onFailed)
        requestQueue.add(request)
        return request
    }

    /**
     * 从服务器数据库批量查询已有CodeGood

     * @param condition 查询条件，输入MariaDB的查询条件
     * *
     */
    fun selectCodeGood(condition: String, onSuccess: (MutableList<CodeGood>) -> Unit, onFailed: (Throwable, Int) -> Unit): Request<out Any> {
        val data = JSONObject()
        data.put("condition", condition)
        val request = EncryptedRequest(URL_FIND_CODEGOOD, data.toString(), {
            try {
                val codeGoods = gson.fromJson<MutableList<CodeGood>>(it!!)
                onSuccess(codeGoods)
            } catch (e: Exception) {
                onFailed(e, -3)
            }
        }, onFailed)
        requestQueue.add(request)
        return request
    }

    /**
     * 通过CodeGood的id获得content

     * @param id CodeGood的id
     * *
     */
    fun loadCodeContent(id: Int, onSuccess: (String?) -> Unit, onFailed: (Throwable, Int) -> Unit): Request<*> {
        val data = JSONObject()
        data.put("id", id)
        val request = EncryptedRequest(URL_LOAD_CODE_CONTENT, data.toString(), onSuccess, onFailed)
        requestQueue.add(request)
        return request
    }

    /**
     * 向服务器数据库插入一个Reply

     * @param reply    需要插入的Reply实例
     * *
     */
    fun addReply(reply: Reply, onSuccess: () -> Unit, onFailed: (Throwable, Int) -> Unit): Request<out Any>? {
        if (user == null || key == null) {
            onFailed(PostException("登陆后才能进行此操作"), -4)
            return null
        }
        reply.username = user!!.username
        val data = JSONObject()
        data.put("reply", gson.toJson(reply))
        data.put("key", key)
        val request = EncryptedRequest(URL_ADD_REPLY, data.toString(), { onSuccess() }, onFailed)
        requestQueue.add(request)
        return request
    }

    /**
     * 从服务器数据库批量查询已有CodeGood

     * @param condition 查询条件，输入MariaDB的查询条件
     * *
     */
    fun findReply(condition: String, onSuccess: (MutableList<Reply>) -> Unit, onFailed: (Throwable, Int) -> Unit): Request<out Any> {
        val data = JSONObject()
        data.put("condition", condition)
        val request = EncryptedRequest(URL_FIND_REPLY, data.toString(), {
            try {
                val replies = gson.fromJson<MutableList<Reply>>(it!!)
                onSuccess(replies)
            } catch (e: Exception) {
                onFailed(e, -3)
            }
        }, onFailed)
        requestQueue.add(request)
        return request
    }

    fun addFavorite(goodId: Int, onSuccess: () -> Unit, onFailed: (Throwable, Int) -> Unit): Request<*>? {
        if (user == null || key == null) {
            onFailed(PostException("登陆后才能进行此操作"), -4)
            return null
        }
        val data = JSONObject()
        data.put("userId", user!!.id)
        data.put("key", key)
        data.put("goodId", goodId)
        val request = EncryptedRequest(URL_ADD_FAVORITE, data.toString(), { onSuccess() }, onFailed)
        requestQueue.add(request)
        return request
    }

    fun findFavorite(onSuccess: (MutableList<Favorite>) -> Unit, onFailed: (Throwable, Int) -> Unit): Request<*>? {
        if (user == null || key == null) {
            onFailed(PostException("登陆后才能进行此操作"), -4)
            return null
        }
        val data = JSONObject()
        data.put("userId", user!!.id)
        data.put("key", key)
        val request = EncryptedRequest(URL_FIND_FAVORITE, data.toString(), {
            try {
                onSuccess(gson.fromJson(it!!))
            } catch(e: Exception) {
                onFailed(e, -3)
            }
        }, onFailed)
        requestQueue.add(request)
        return request
    }

    fun delFavorite(goodId: Int, onSuccess: () -> Unit, onFailed: (Throwable, Int) -> Unit): Request<*>? {
        if (user == null || key == null) {
            onFailed(PostException("登陆后才能进行此操作"), -4)
            return null
        }
        val data = JSONObject()
        data.put("userId", user!!.id)
        data.put("key", key)
        data.put("goodId", goodId)
        val request = EncryptedRequest(URL_DEL_FAVORITE, data.toString(), { onSuccess() }, onFailed)
        requestQueue.add(request)
        return request
    }

    fun registerUser(username: String, password: String, onSuccess: (User) -> Unit, onFailed: (Throwable, Int) -> Unit): Request<out Any> {
        val data = JSONObject()
        data.put("username", username)
        data.put("password", password)
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imei = tm.deviceId
        data.put("imei", imei)
        val request = EncryptedRequest(URL_ADDUSER, data.toString(), { loginUser(username, password, onSuccess, onFailed) }, onFailed)
        requestQueue.add(request)
        return request
    }

    fun loginUser(username: String, password: String, onSuccess: (User) -> Unit, onFailed: (Throwable, Int) -> Unit): Request<out Any> {
        val data = JSONObject()
        data.put("username", username)
        data.put("password", password)
        val request = EncryptedRequest(URL_LOGIN, data.toString(), {
            try {
                val json = JSONObject(it)
                key = json.getString("key")
                if (key!!.length > 1) {
                    user = gson.fromJson<User>(json.getString("user"))
                    saveUserInfo()
                    onSuccess(user!!)
                    broadcastManager.sendBroadcast(Intent(ACTION_USER_STATE_CHANGED))
                } else
                    onFailed(PostException("登陆失败"), -3)
            } catch (e: JSONException) {
                onFailed(e, -3)
            }
        }, onFailed)
        requestQueue.add(request)
        return request
    }

    fun resetPwd(oldPwd: String, newPwd: String, onSuccess: (User) -> Unit, onFailed: (Throwable, Int) -> Unit): Request<*>? {
        if (user == null) {
            onFailed(PostException("登陆后才能进行此操作"), -4)
            return null
        }
        val data = JSONObject()
        data.put("oldPwd", oldPwd)
        data.put("username", user!!.username)
        data.put("newPwd", newPwd)
        val request = EncryptedRequest(URL_RESET_PWD, data.toString(), { loginUser(user!!.username, newPwd, onSuccess, onFailed) }, onFailed)
        requestQueue.add(request)
        return request
    }

    fun getProfilePicUrl(username: String): String {
        return URL_PROFILE_PIC + username
    }

    fun uploadProfilePic(pic: File, onSuccess: () -> Unit, onProgress: (Long, Long) -> Unit, onFailed: (Throwable, Int) -> Unit): RequestHandle? {
        if (user == null || key == null) {
            onFailed(PostException("登陆后才能进行此操作"), -4)
            return null
        }
        val client = AsyncHttpClient()
        if (pic.exists() && pic.length() > 0) {
            val params = RequestParams()
            try {
                params.put("profilePic", pic)
                params.put("data", base64Encode(RSAUtils.encryptByPublicKey(
                        (key!! + user!!.username).toByteArray(CHARSET), RSA_KEY)))
            } catch (e: Exception) {
                onFailed(e, -2)
                return null
            }
            return client.post(URL_UPLOAD_PROFILE_PIC, params, object : AsyncHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    try {
                        val rc = Integer.parseInt(String(responseBody).trim { it <= ' ' })
                        if (rc == 0) {
                            onSuccess()
                            broadcastManager.sendBroadcast(Intent(ACTION_USER_STATE_CHANGED))
                        } else
                            onFailed(PostException("服务器报告异常"), rc)
                    } catch (e: Exception) {
                        onFailed(e, -3)
                    }

                }

                override fun onFailure(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray, error: Throwable) {
                    onFailed(error, -1)
                }

                override fun onProgress(bytesWritten: Long, totalSize: Long) {
                    onProgress(bytesWritten, totalSize)
                }
            })
        } else {
            onFailed(PostException("本地文件不存在"), -2)
        }
        return null
    }

    fun cancel(request: Request<*>?) = requestQueue.cancelAll(RequestQueue.RequestFilter { it === request })

    fun cancelAll() = requestQueue.cancelAll(RequestQueue.RequestFilter { true })

    fun logoutUser() {
        preferences.edit().remove("user_key").remove("user_name").remove("user_id").remove("user_createat").apply()
        user = null
        key = null
        broadcastManager.sendBroadcast(Intent(ACTION_USER_STATE_CHANGED))
    }

    private fun saveUserInfo() {
        preferences.edit().putString("user_key", key).putString("user_name", user!!.username).putInt("user_id", user!!.id!!).putLong("user_createat", user!!.createAt!!).apply()
    }

    private fun loadUserInfo() {
        val username = preferences.getString("user_name", null)
        val id = preferences.getInt("user_id", -1)
        val createat = preferences.getLong("user_createat", -1)
        //i { "username = $username, id = $id, createat = $createat" }
        if (username != null && id > 0 && createat > 0) {
            user = User(username, id, createat)
            key = preferences.getString("user_key", key)
        }
    }
}