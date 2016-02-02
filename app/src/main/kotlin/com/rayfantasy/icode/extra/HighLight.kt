package com.rayfantasy.icode.extra

/**
 * Created by Allen on 2015/11/27 0027.
 */
object HighLight {
    val GREEN = "//(.*)|/\\*(.|[\r\n])*?\\*/|=|=="
    val RED = "true|false| [0-9]*$|\".+?\"|\\d+(\\.\\d+)?"
    val BLUE = "class|import|extends|package|implements|switch|while|break|case|private|public|protected|void|super|Bundle|this|static|final|if|else|return|new|catch|try"
    val OTHER = "\\;|\\(|\\)|\\{|\\}|R\\..+?\\..+?|([\t|\n| ][A-Z].+? )|String |int |boolean |float |double |char |long |Override "
    val COLOR_GREEN = -16745442
    val COLOR_RED = -65536
    val COLOR_BLUE = -13860152
    val COLOR_OTHER = -16738561
}
