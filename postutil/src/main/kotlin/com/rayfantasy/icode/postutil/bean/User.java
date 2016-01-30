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

package com.rayfantasy.icode.postutil.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.rayfantasy.icode.postutil.database.PostUtilDatabase;

import java.io.Serializable;

@Table(name = "user", database = PostUtilDatabase.class)
public class User extends BaseModel implements Serializable {
    @Expose
    @Column(name = "username")
    @SerializedName("username")
    public String username;

    @Expose
    @PrimaryKey(autoincrement = false)
    @Column(name = "id")
    @SerializedName("id")
    public int id;

    @Expose
    @Column(name = "createat")
    @SerializedName("createat")
    public Long createAt;

    public User(){}

    public User(String username, Integer id, Long createAt) {
        this.username = username;
        this.id = id;
        this.createAt = createAt;
    }

/*    public String getUsername() {
        return username;
    }

    public Integer getId() {
        return id;
    }

    public Long getCreateAt() {
        return createAt;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }*/
}
