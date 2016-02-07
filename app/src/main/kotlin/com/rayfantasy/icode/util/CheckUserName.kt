package com.rayfantasy.icode.util

/**
 * Created by qweas on 2016/2/4 0004.
 */
fun checkName(username: String): Boolean {
    var flag: Int = 1
    for (i in username) {
        if (isChinese(i)) {
            continue
        } else {
            if (isEnglish(i)) {
                continue
            } else {
                if (isNumber(i)) {
                    continue
                } else {
                    if (isOther(i)) {
                        continue
                    } else {
                        flag = 0; break
                    }
                }
            }
        }

    }
    if (flag == 0) return false else return true
}

fun isChinese(c: Char): Boolean {
    val ub: Character.UnicodeBlock = Character.UnicodeBlock.of(c)
    if (       ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
        return true;
    }
    return false;
}

fun isEnglish(c: Char): Boolean {
    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) return true
    return false
}

fun isNumber(c: Char): Boolean {
    if (c <= '9' && c >= '0') return true
    return false
}

fun isOther(c: Char): Boolean {
    if ( c == '_') return true
    return false
}