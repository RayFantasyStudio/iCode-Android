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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

public class Reply extends BaseModel implements Serializable {
    @Expose
    @Column(name = "content")
    @SerializedName("content")
    public String content;

    @Expose
    @Column(name = "username")
    @SerializedName("username")
    public String username;

    @Expose
    @Column(name = "goodId")
    @SerializedName("goodId")
    public Integer goodId;

    @Expose
    @PrimaryKey(autoincrement = false)
    @Column(name = "id")
    @SerializedName("id")
    public Integer id;

    @Expose
    @Column(name = "createat")
    @SerializedName("createat")
    public Long createAt;

/*    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public Long getCreateAt() {
        return createAt;
    }*/
}
