package com.rayfantasy.icode.postutil

import com.rayfantasy.icode.postutil.extension.fromJson
import java.io.Serializable

data class User(val username: String, val id: Int? = null, val createat: Long? = null) : Serializable
data class Reply(val content: String?, var goodId: Int, var username: String? = null, val createat: Long? = null, val id: Int? = null) : Serializable

data class CodeGood(var title: String, var subtitle: String, var content: String?, var username: String? = null,
                    val updateat: Long? = null, val createat: Long? = null, val id: Int? = null, var highlight: Boolean? = null) : Serializable {
    var blocks: List<Block>
        set(value) {
            content = PostUtil.gson.toJson(value)
        }
        get() = PostUtil.gson.fromJson(content!!)
}

data class Block(val blockType: Int, var content: String, val extra: String? = null)

object BlockType {
    const val TEXT = 0
    const val CODE = 1
}
