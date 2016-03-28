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

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.rayfantasy.icode.postutil.database.PostUtilDatabase;

import java.io.Serializable;

@Table(name = "code_good", database = PostUtilDatabase.class)
public class CodeGood extends BaseModel implements Serializable {
    @Expose
    @Column(name = "title")
    @SerializedName("title")
    public String title;

    @Expose
    @Column(name = "subtitle")
    @SerializedName("subtitle")
    public String subtitle;

    @Expose
    @Column(name = "username")
    @SerializedName("username")
    public String username;

    @Expose
    @Column(name = "content")
    @SerializedName("content")
    public String content;

    //updateat、createat、id由服务器生成，本地不提供setter方法
    @Expose
    @Column(name = "createat")
    @SerializedName("createat")
    public Long createAt;

    @Expose
    @Column(name = "updateat")
    @SerializedName("updateat")
    public Long updateAt;

    @Expose
    @PrimaryKey(autoincrement = false)
    @Column(name = "id")
    @SerializedName("id")
    public Integer id;

    @Expose
    @Column(name = "highlight")
    @SerializedName("highlight")
    public Boolean highlight;

    @Expose
    @Column(name = "favorite")
    @SerializedName("favorite")
    public Integer favorite;

    @Expose
    @Column(name = "reply")
    @SerializedName("reply")
    public Integer reply;


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CodeGood)) return false;
        CodeGood codeGood = (CodeGood) o;
        return codeGood.id.equals(id) && codeGood.updateAt.equals(updateAt);
    }

    public void loadContentFromCache() {
        if (content == null) {
            CodeGood codeGood = new Select(CodeGood_Table.content)
                    .from(CodeGood.class)
                    .where(CodeGood_Table.id.is(id))
                    .querySingle();
            if (codeGood != null)
                content = codeGood.content;
        }
    }

    public CodeGood() {
    }

    public CodeGood(@NonNull String title, @NonNull String subtitle, @NonNull String content) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
    }

    public void liked() {
        new Favorite(id, System.currentTimeMillis()).save();
        favorite++;
        save();
    }

    public void unLiked() {
        new Delete()
                .from(Favorite.class)
                .where(Favorite_Table.goodId.is(id))
                .execute();
        favorite--;
        save();
    }

    public static class Block {
        @Expose
        @SerializedName("extra")
        public String extra;

        @Expose
        @SerializedName("content")
        public String content;

        @Expose
        @SerializedName("blockType")
        public Integer blockType;

        public Block(Integer blockType, String content, String extra) {
            this.extra = extra;
            this.content = content;
            this.blockType = blockType;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getBlockType() {
            return blockType;
        }

        public void setBlockType(Integer blockType) {
            this.blockType = blockType;
        }
    }

    public static class BlockType {
        public static final int TEXT = 0;
        public static final int CODE = 1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUsername() {
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

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getHighlight() {
        return highlight;
    }

    public void setHighlight(Boolean highlight) {
        this.highlight = highlight;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public Integer getReply() {
        return reply;
    }

    public void setReply(Integer reply) {
        this.reply = reply;
    }
}