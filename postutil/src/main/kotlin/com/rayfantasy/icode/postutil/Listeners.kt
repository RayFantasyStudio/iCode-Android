/*
 * Copyright 2016 Alex Zhang aka. ztc1997
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

import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.bean.Favorite
import com.rayfantasy.icode.postutil.bean.Reply
import com.rayfantasy.icode.postutil.bean.User

open class OnResponseListener {
    internal var _onFailed: ((Throwable, Int) -> Unit)? = null

    internal fun onFailed(t: Throwable, rc: Int) = _onFailed?.invoke(t, rc)

    fun onFailed(listener: (Throwable, Int) -> Unit) {
        _onFailed = listener
    }
}

open class OnSuccessListenerNoParam : OnResponseListener() {
    internal var _onSuccess: (() -> Unit)? = null

    internal fun onSuccess() = _onSuccess?.invoke()

    fun onSuccess(listener: () -> Unit) {
        _onSuccess = listener
    }
}

open class OnSuccessListenerCodeGoodList : OnResponseListener() {
    internal var _onSuccess: ((MutableList<CodeGood>) -> Unit)? = null

    internal fun onSuccess(codeGoods: MutableList<CodeGood>) = _onSuccess?.invoke(codeGoods)

    fun onSuccess(listener: (MutableList<CodeGood>) -> Unit) {
        _onSuccess = listener
    }
}

open class OnSuccessListenerString : OnResponseListener() {
    internal var _onSuccess: ((String?) -> Unit)? = null

    internal fun onSuccess(s: String?) = _onSuccess?.invoke(s)

    fun onSuccess(listener: (String?) -> Unit) {
        _onSuccess = listener
    }
}

open class OnSuccessListenerReplyList : OnResponseListener() {
    internal var _onSuccess: ((MutableList<Reply>) -> Unit)? = null

    internal fun onSuccess(replies: MutableList<Reply>) = _onSuccess?.invoke(replies)

    fun onSuccess(listener: (MutableList<Reply>) -> Unit) {
        _onSuccess = listener
    }
}

open class OnSuccessListenerFavoriteList : OnResponseListener() {
    internal var _onSuccess: ((MutableList<Favorite>) -> Unit)? = null

    internal fun onSuccess(favorites: MutableList<Favorite>) = _onSuccess?.invoke(favorites)

    fun onSuccess(listener: (MutableList<Favorite>) -> Unit) {
        _onSuccess = listener
    }
}

open class OnSuccessListenerUser : OnResponseListener() {
    internal var _onSuccess: ((User) -> Unit)? = null

    internal fun onSuccess(user: User) = _onSuccess?.invoke(user)

    fun onSuccess(listener: (User) -> Unit) {
        _onSuccess = listener
    }
}

open class OnSuccessAndProccessListener : OnSuccessListenerNoParam() {
    internal var _onProgress: ((Long, Long) -> Unit)? = null

    internal fun onProgress(bytesWritten: Long, totalSize: Long) = _onProgress?.invoke(bytesWritten, totalSize)

    fun onProgress(listener: (Long, Long) -> Unit) {
        _onProgress = listener
    }
}
