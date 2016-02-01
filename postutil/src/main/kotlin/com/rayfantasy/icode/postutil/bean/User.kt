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

package com.rayfantasy.icode.postutil.bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import com.rayfantasy.icode.postutil.database.PostUtilDatabase
import java.io.Serializable

@Table(tableName = "user", databaseName = PostUtilDatabase.NAME)
class User : BaseModel, Serializable {
    @Expose
    @Column(name = "username")
    @SerializedName("username")
    var username: String? = null

    @Expose
    @PrimaryKey(autoincrement = false)
    @Column(name = "id")
    @SerializedName("id")
    var id: Int = 0

    @Expose
    @Column(name = "createat")
    @SerializedName("createat")
    var createAt: Long? = null

    constructor() {
    }

    constructor(username: String, id: Int, createAt: Long?) {
        this.username = username
        this.id = id
        this.createAt = createAt
    }
}
