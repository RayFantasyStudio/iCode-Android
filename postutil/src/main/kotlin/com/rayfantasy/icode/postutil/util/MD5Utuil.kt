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

import java.security.MessageDigest

@Throws(Exception::class)
fun getMD5(`val`: String): String {
    val md5 = MessageDigest.getInstance("MD5")
    md5.update(`val`.toByteArray())
    val m = md5.digest()//加密
    return base64Encode(m)
}
