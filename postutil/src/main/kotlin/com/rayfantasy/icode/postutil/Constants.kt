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

const val URL_ROOT = "http://icode.rayfantasy.com:8088/"
//const val URL_SERVLET = "http://192.168.1.200:8080/iCode/";
const val URL_SERVLET = URL_ROOT + "servlet/"
const val URL_UPLOAD = URL_ROOT + "upload/"
const val URL_ADD_CODEGOOD = URL_SERVLET + "AddCodeGood"
const val URL_FIND_CODEGOOD = URL_SERVLET + "FindCodeGood"
const val URL_ADDUSER = URL_SERVLET + "AddUser"
const val URL_LOGIN = URL_SERVLET + "Login"
const val URL_ADD_REPLY = URL_SERVLET + "AddReply"
const val URL_FIND_REPLY = URL_SERVLET + "FindReply"
const val URL_LOAD_CODE_CONTENT = URL_SERVLET + "LoadCodeContent"
const val URL_PROFILE_PIC = URL_UPLOAD + "profile_pic/"
const val URL_ADD_FAVORITE = URL_SERVLET + "AddFavorite"
const val URL_FIND_FAVORITE = URL_SERVLET + "FindFavorite"
const val URL_DEL_FAVORITE = URL_SERVLET + "DelFavorite"
const val CHARSET = "utf-8"
const val ACTION_USER_STATE_CHANGED = "com.rayfantasy.icode.postutil.ACTION_USER_STATE_CHANGED"
