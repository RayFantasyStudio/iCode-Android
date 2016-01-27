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

import java.security.Key
import java.security.NoSuchAlgorithmException

import javax.crypto.KeyGenerator

const val KEY_STRING = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCh26b3BIB2yjD61nDkqaXAOWr1ouDdqcZveEthKj86s15WWWP9Juqq2bxpXrZdjk9EaQcaE8kahBR4WLNugT+DQRCrhcgR9VwCTFJJdRWDEwmBJVb2DPlkHY9XetuRgnBWASF27c0Ee+kyFLcr0sSzM/J4cmb9s3aSzjnoLY+pZQIDAQAB"
val RSA_KEY = RSAUtils.getPublicKey(KEY_STRING)

@Throws(NoSuchAlgorithmException::class)
fun generateAESKey(): Key {
    val keyGenerator = KeyGenerator.getInstance("AES")
    keyGenerator.init(128)
    return keyGenerator.generateKey()
}
