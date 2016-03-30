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

import android.graphics.Color

/**
 * Default Java Syntax Highlighter
 * Created by Tony Yang on 2016/3/2.
 */
object JavaSyntaxGroup : SyntaxHighlighter() {
    override fun initSyntaxElements() {
        // Classes
        addSyntaxElement(SyntaxElement("\\b([A-Z]\\w*)", Color.rgb(118, 0, 0)))
        // Functions
        addSyntaxElement(SyntaxElement("\\b([a-z]\\w*)", Color.parseColor("#795DA3")))
        // Keywords
        addSyntaxElement(SyntaxElement("\\b(abstract|assert|boolean|break|byte|case|catch" +
                "char|class|const|continue|default|do|double|else|" +
                "enum|extends|final|finally|float|" +
                "for|goto|if|implements|import|" +
                "instanceof|int|interface|long|native|" +
                "new|package|private|protected|public|" +
                "return|strictfp|short|static|super|" +
                "switch|synchronized|this|throw|throws|" +
                "transient|try|void|volatile|while)", Color.parseColor("#A71D5D")))
        // Values
        addSyntaxElement(SyntaxElement("\".*\"", Color.parseColor("#0086B3")))
        addSyntaxElement(SyntaxElement("(-?\\d+)(\\.\\d+)?", Color.parseColor("#0086B3")))
        // Comments
        addSyntaxElement(SyntaxElement("//.*\\s", Color.parseColor("#969896")))
        addSyntaxElement(SyntaxElement("/\\*.*\\*/", Color.parseColor("#969896")))
    }
}
