package com.rayfantasy.icode.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Allen on 2015/12/25 0025.
 */
class DBHelper(ctx: Context) : SQLiteOpenHelper(ctx, "icode_db", null, 1) {


    override fun onCreate(db: SQLiteDatabase?) {
        var sql: String = "CREATE TABLE favo (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, codeGoodId VCHAR, title VCHAR, sub_title VCHAR, name VCHAR, code VCHAR, createat int (30),updateat int (30))"
        db?.execSQL(sql)
        throw UnsupportedOperationException()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        throw UnsupportedOperationException()
    }
}

