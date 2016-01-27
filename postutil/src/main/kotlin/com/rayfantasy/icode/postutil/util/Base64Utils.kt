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

package com.rayfantasy.icode.postutil.util

import android.util.Base64

/**
 *
 *
 * BASE64编码解码工具包
 *
 *
 *
 * 依赖javabase64-1.3.1.jar
 *

 * @author IceWee
 * *
 * @version 1.0
 * *
 * @date 2012-5-19
 */

/**
 *
 *
 * BASE64字符串解码为二进制数据
 *

 * @param base64
 * *
 * @return
 * *
 * @throws Exception
 */
@Throws(Exception::class)
fun base64Decode(base64: String): ByteArray {
    return Base64.decode(base64, Base64.DEFAULT)
}

/**
 *
 *
 * 二进制数据编码为BASE64字符串
 *

 * @param bytes
 * *
 * @return
 * *
 * @throws Exception
 */
@Throws(Exception::class)
fun base64Encode(bytes: ByteArray): String {
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}
