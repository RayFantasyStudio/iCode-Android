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

object KotlinSyntaxGroup : SyntaxHighlighter() {
    override fun initSyntaxElements() {
        // Classes
        addSyntaxElement(SyntaxElement("\\b([A-Z]\\w*)", Color.rgb(118, 0, 0)))
        // Functions
        addSyntaxElement(SyntaxElement("\\b([a-z]\\w*)", Color.parseColor("#795DA3")))
        // Keywords
        addSyntaxElement(SyntaxElement("\\b(abstract|if|private|this|do|protected|throw" +
                "break|import|public|else|return|transient|try|catch" +
                "final|interface|finally|class|super|while|const|for" +
                "when|continue|package|as|in|object|override" +
                "sealed|trait|type|val|var|constructor|is|init" +
                "companion|operator|fun|infix|open)", Color.parseColor("#A71D5D")))
        // Basic Types
        addSyntaxElement(SyntaxElement("\\b(Any|Unit|String|Int|Boolean|Char|Long|Double" +
                "Float|Short|Byte)", Color.parseColor("#DC143C")))
        // Values
        addSyntaxElement(SyntaxElement("\".*\"", Color.parseColor("#0086B3")))
        addSyntaxElement(SyntaxElement("(-?\\d+)(\\.\\d+)?", Color.parseColor("#0086B3")))
        // Comments
        addSyntaxElement(SyntaxElement("//.*\\s", Color.parseColor("#969896")))
        addSyntaxElement(SyntaxElement("/\\*.*\\*/", Color.parseColor("#969896")))
    }
}