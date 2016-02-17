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
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.rayfantasy.icode.postutil.database.PostUtilDatabase;

import java.io.Serializable;

@Table(name = "favorite", database = PostUtilDatabase.class)
public class Favorite extends BaseModel implements Serializable {
    @Expose
    @PrimaryKey
    @Column(name = "goodId")
    @SerializedName("goodId")
    public Integer goodId;
    @Expose
    @Column(name = "createat")
    @SerializedName("createat")
    public Long createAt;

    public Favorite() {

    }

    public Favorite(Integer goodId, Long createAt) {
        this.goodId = goodId;
        this.createAt = createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Favorite)) return false;
        Favorite favorite = (Favorite) o;
        return goodId.equals(favorite.goodId);
    }
}
