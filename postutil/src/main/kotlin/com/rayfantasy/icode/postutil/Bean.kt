package com.rayfantasy.icode.postutil

import com.google.gson.*
import com.rayfantasy.icode.postutil.extension.fromJson
import java.io.Serializable
import java.lang.reflect.Type

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

data class Block(val blockType: BlockType, var content: String)

enum class BlockType {
    TEXT, JAVA, KOTLIN, XML
}

internal class BlockTypeSerializer : JsonSerializer<BlockType>, JsonDeserializer<BlockType> {
    override fun serialize(src: BlockType?, typeOfSrc: Type?, context: JsonSerializationContext?)
            = if (src != null) JsonPrimitive(src.ordinal) else null

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?)
            = if (json != null) BlockType.values()[json.asInt] else null
}