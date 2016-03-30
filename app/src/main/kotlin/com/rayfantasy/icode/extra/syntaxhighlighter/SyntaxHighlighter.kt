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

package com.rayfantasy.icode.extra.syntaxhighlighter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import java.util.*
import java.util.regex.Pattern

/**
 * Created by dell on 2016/3/2.
 */
abstract class SyntaxHighlighter {
    private val mSyntaxElements: MutableList<SyntaxElement>

    init {
        mSyntaxElements = ArrayList<SyntaxElement>()
        initSyntaxElements()
    }

    fun addSyntaxElement(element: SyntaxElement) {
        mSyntaxElements.add(element)
    }

    fun highlight(textview: TextView) {
        val sp = SpannableString(textview.text)
        for (element in mSyntaxElements) {
            //System.out.println(element.getRegex()+element.getColor());
            val p = Pattern.compile(element.regex)
            val m = p.matcher(sp)
            while (m.find()) {
                sp.setSpan(ForegroundColorSpan(element.color), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        textview.text = sp
        //        String regex = "(";
        //        int counter = 0;
        //        String[] reservedWords = new String[]{"super","int", "bool", "float", "string", "function", "class", "new", "this", "private", "public", "protected"};
        //        for(String word : reservedWords) {
        //            counter += 1;
        //            regex += "\\b" + word;
        //            if(counter < reservedWords.length) {
        //                regex += "|";
        //            }
        //        }
        //        regex += "\\s)";
        //
        //        //Log.d("reg",regex);
        //

    }

    protected abstract fun initSyntaxElements()
}
