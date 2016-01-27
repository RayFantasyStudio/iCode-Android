package com.rayfantasy.icode.util

import com.google.gson.Gson
import com.rayfantasy.icode.FILES_PATH
import com.rayfantasy.icode.postutil.CodeGood
import java.io.File
import java.util.*

/**
 * Created by Allen on 2015/12/7 0007.
 */
class FavoritesUtil {
    val FAVORITES_PATH = FILES_PATH + File.separator + "FAVORITES" + File.separator
    private val gson = Gson()

    fun readDataFromFavorites(id: String): CodeGood? {
        try {
            val json = FileUtil.readString(FAVORITES_PATH + id)
            return gson.fromJson(json, CodeGood::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun readDataFromFavorites(file: File): CodeGood? {
        try {
            val json = FileUtil.readString(file.path)
            return gson.fromJson(json, CodeGood::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun writeDataToFavorites(data: CodeGood): Boolean {
        try {
            val json = gson.toJson(data)
            /* FileUtil.writeString(FAVORITES_PATH + CodeGood.getID(), json);*/
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun deleteDataFromFavorites(id: String): Boolean {
        val file = File(FAVORITES_PATH + id)
        return file.delete()
    }

    fun readDataListFromFavorites(): List<CodeGood> {
        val files = FileUtil.getFileSort(FAVORITES_PATH)
        val datas = ArrayList<CodeGood>()
        for (f in files!!) {
            val data = readDataFromFavorites(f)
            if (data != null) datas.add(data)
        }
        return datas
    }
}