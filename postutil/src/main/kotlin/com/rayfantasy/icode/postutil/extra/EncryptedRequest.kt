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

package com.rayfantasy.icode.postutil.extra

import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.rayfantasy.icode.postutil.CHARSET
import com.rayfantasy.icode.postutil.exception.PostException
import com.rayfantasy.icode.postutil.extension.i
import com.rayfantasy.icode.postutil.extension.v
import com.rayfantasy.icode.postutil.util.*
import org.json.JSONObject
import java.util.*
import javax.crypto.Cipher

internal class EncryptedRequest(url: String, val data: String, val onSuccess: (String?) -> Unit,
                                val onFailed: (Exception, Int) -> Unit) : StringRequest(Request.Method.POST, url, null, null) {
    companion object {
        private const val KEY_ALGORITHM = "AES/ECB/PKCS5Padding"
    }

    private val key by lazy { generateAESKey() }

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        val map = HashMap<String, String>()
        try {
            v(Arrays.toString(key.encoded))
            map.put("key", base64Encode(RSAUtils.encryptByPublicKey(key.encoded, RSA_KEY)))
            val cipher = Cipher.getInstance(KEY_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            map.put("data", base64Encode(cipher.doFinal(data.toByteArray(CHARSET))))
        } catch (e: Exception) {
            onFailed(e, -2)
        }
        return map
    }

    override fun deliverResponse(response: String) {
        try {
            val cipher = Cipher.getInstance(KEY_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key)
            val json = JSONObject(String(cipher.doFinal(base64Decode(response)), CHARSET))
            i(json.toString())
            val rc = json.getInt("rc")
            if (rc == 0) {
                var data: String? = null
                if (json.has("data"))
                    data = json.getString("data")
                onSuccess(data)
            } else
                onFailed(PostException("服务器报告异常，rc = $rc"), rc)
        } catch (e: Exception) {
            onFailed(e, -3)
        }

    }

    override fun deliverError(error: VolleyError) {
        onFailed(error, -1)
    }
}
