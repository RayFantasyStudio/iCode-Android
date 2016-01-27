package com.rayfantasy.icode.database

import android.content.ContentValues
import android.content.Context


/**
 * Created by Allen on 2015/12/25 0025.
 */
class DBManager(context: Context) {
    var database: DBHelper = DBHelper(context)

    fun favo_insert(codeGoodId: Int, title: String, sub_title: String, name: String, createat: Int, code: String, updateat: Int) {
        var db = database.readableDatabase
        /*       var cv : ContentValues = ContentValues()
               cv.put("codeGoodId",codeGoodId)
               cv.put("time",time)
               cv.put("sub_title",sub_title)
               cv.put("title",title)
               cv.put("createat",createat)
               cv.put("code",code)
               cv.put("name",name)
               db.insert("favo",null,cv)*/
        var sql: String = "insert into favo(codeGoodId,title,sub_title,name,code,createat,updateat values($codeGoodId,$title,$sub_title,$name,$code,$createat,$updateat)"
    }

    fun favo_delete(what: Array<String>) {
        var db = database.readableDatabase
        db.delete("favo", "codeGoodId", what)
    }

    fun codeGoods_insert(codeGoodId: Int, time: Int, title: String, sub_title: String, name: String, createat: Int, code: String) {
        var db = database.readableDatabase
        var cv: ContentValues = ContentValues()
        cv.put("codeGoodId", codeGoodId)
        cv.put("time", time)
        cv.put("sub_title", sub_title)
        cv.put("title", title)
        cv.put("createat", createat)
        cv.put("code", code)
        db.insert("codeGoods", null, cv)
    }
}


