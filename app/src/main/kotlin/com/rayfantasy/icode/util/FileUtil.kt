package com.rayfantasy.icode.util

import java.io.*
import java.util.*

/**
 * Created by Allen on 2015/12/7 0007.
 */
object FileUtil {
    private val LINE_BREAK = System.getProperty("line.separator", "\n")

    fun readObject(name: String): Any? {
        var obj: Any? = null
        val file = File(File.separator + name)
        var ois: ObjectInputStream? = null
        try {
            if (file.exists()) {
                val fis = FileInputStream(file)
                ois = ObjectInputStream(fis)
                try {
                    obj = ois.readObject()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }

                ois.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (ois != null) {
                    ois.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return obj
    }

    fun writeObjecj(obj: Any, path: String) {
        var oos: ObjectOutputStream? = null
        try {
            val f = File(path)
            val dir = f.parentFile
            if (!dir.exists()) dir.mkdirs()
            val fos = FileOutputStream(f)
            oos = ObjectOutputStream(fos)
            oos.writeObject(obj)
            oos.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (oos != null) {
                    oos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun readString(path: String): String? {
        val f = File(path)
        return f.readText()
        /*        var br: BufferedReader? = null
                var ret: String? = null
                try {
                    br = BufferedReader(FileReader(f))
                    var line: String
                    val sb = StringBuilder(f.length().toInt())
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append(LINE_BREAK)
                    }
                    ret = sb.toString()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    if (br != null) {
                        try {
                            br.close()
                        } catch (ignored: Exception) {
                        }
                    }
                }
                return ret*/
    }


    fun writeString(filePath: String, content: String) {
        File(filePath).writeText(content)
        /*var output: BufferedWriter? = null
        try {
            val f = File(File.separator + filePath)
            val dir = f.parentFile
            if (!dir.exists()) dir.mkdirs()
            output = BufferedWriter(FileWriter(f))
            output.write(content)
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (output != null) {
                    output.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }*/
    }

    /**
     * 获取目录下所有文件(按时间排序)

     * @param path
     * *
     * @return
     */
    fun getFileSort(path: String): List<File>? {

        val list = getFiles(path, ArrayList<File>())

        if (list != null && list.size > 0) {

            Collections.sort(list) { file, newFile ->
                if (file.lastModified() < newFile.lastModified()) {
                    1
                } else if (file.lastModified() == newFile.lastModified()) {
                    0
                } else {
                    -1
                }
            }

        }

        return list
    }

    /**
     * 获取目录下所有文件

     * @param realpath
     * *
     * @param files
     * *
     * @return
     */
    fun getFiles(realpath: String, files: MutableList<File>): List<File>? {

        val realFile = File(realpath)
        if (realFile.isDirectory) {
            val subfiles = realFile.listFiles()
            for (file in subfiles) {
                if (file.isDirectory) {
                    getFiles(file.absolutePath, files)
                } else {
                    files.add(file)
                }
            }
        }
        return files
    }
}
