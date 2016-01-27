package com.rayfantasy.icode.editor

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.text.*
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewManager
import android.widget.EditText
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extra.HighLight
import org.jetbrains.anko.custom.ankoView
import java.util.regex.Pattern


class TextEditorView : EditText {


    private var res: Resources? = null
    private var updateHandler: Handler? = null
    private val updateDelay: Int = 0
    private var errorLine: Int = 0
    private var textChange = false
    private var modified = true

    var line: Pattern? = null
    var number: Pattern? = null
    var headfile: Pattern? = null
    var string: Pattern? = null
    var keyword: Pattern? = null
    var pretreatment: Pattern? = null
    var builtin: Pattern? = null
    var comment: Pattern? = null
    var trailingWhiteSpace: Pattern? = null
    private var mode = 0
    internal var oldDist: Float = 0.toFloat()
    internal var textSize = 0f
    private val mPaint = Paint()
    private val onTextChangedListener: OnTextChangedListener? = null
    private val updateThread = Runnable {
        val edit = text
        onTextChangedListener?.onTextChanged(edit.toString())
        highlightWithoutChange(edit)
    }
    //private var context: Context? = null


    constructor(context: Context) : super(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }


    /* 初始化一些参数 */
    fun init(context: Context) {
        gravity = Gravity.TOP
        mPaint.isAntiAlias = false
        mPaint.color = Color.argb(200, 140, 140, 140)
        mPaint.textSize = dip2px(context, 10f)
        setPadding(dip2px(context, 20f).toInt(), 0, 0, 0)
        setHorizontallyScrolling(true)
        updateHandler = Handler()
        filters = arrayOf(inputFilter)
        addTextChangedListener(watcher)
        res = context.resources
        initPattern()
    }


    /* 初始化正则匹配的模板 */
    fun initPattern() {
        line = Pattern.compile(".*\\n")
        //headfile =Pattern.compile("#\\b(include)\\b\\s*<\\w*(/?.*/?)[\\w+|h]>[^\"]");
        //number = Pattern.compile("\\b(\\d*[.]?\\d+)\\b");
        string = Pattern.compile("\"(\\\"|.)*?\"")
        keyword = Pattern.compile("[^<,\",|]\\b(auto|int|short|double|float|void|long|signed|unsigned|char|struct|public|protected|private|class|union|bool|string|vector|typename|do|for|while|if|else|switch|case|default|new|delete|true|false|typedef|static|const|register|extern|volatile|goto|return|continue|break|using|namespace|try|catch|import|package)\\b[^>,|\"]")
        pretreatment = Pattern.compile("[^\"]#\\b(ifdef|ifndef|define|undef|if|else|elif|endif|pragma|error|line)\\b[\\s|\\S].*[^\"]")
        builtin = Pattern.compile("[^\"]\\b(printf|scanf|std::|cout|cin|cerr|clog|endl|template|sizeof)\\b[^\"]")
        comment = Pattern.compile("/\\*(.|[\r\n])*?(\\*/)|/\\*(.|[\r\n])*|[^\"](?<!:)//.*[^\"]")
        trailingWhiteSpace = Pattern.compile("[\\t ]+$", Pattern.MULTILINE)

    }

    override fun onDraw(canvas: Canvas) {
        val lineHeight = lineHeight
        val x = scrollX
        val t = scrollY
        val b = height + t
        val layout = layout
        val topLine = layout.getLineForVertical(t)
        val bottomLine = layout.getLineForVertical(b)
        for (i in topLine..bottomLine + 1) {
            canvas.drawText(i.toString(), (x + 5).toFloat(), (i * lineHeight).toFloat(), mPaint)
        }
        canvas.translate(0f, 0f)
        super.onDraw(canvas)
    }

    /*
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
    fun dip2px(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }


    /*
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
    fun px2dip(context: Context, pxValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return pxValue / scale + 0.5f
    }


    /* 对输入的内容进行过滤 */
    private val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
        if (modified && end - start == 1 && start < source.length && dstart < dest.length) {
            val c = source[start]
            if (c == '\n')
                return@InputFilter autoIndent(source, start, end, dest, dstart, dend)
        }
        source
    }


    /* 对EditText输入内容进行判断 */
    private val watcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(edit: Editable) {
            //匹配不包含中文[^\u4e00-\u9fa5]+   /*只包含中文[\u4e00-\u9fa5]+*/
            //[\\s|\\S].*匹配当前行任意字符
            cancelUpdate()
            if (!modified)
                return
            textChange = true
            updateHandler!!.postDelayed(updateThread, updateDelay.toLong())
        }
    }


    fun setTextHighlighted(text: CharSequence) {
        cancelUpdate()

        errorLine = 0
        textChange = false

        modified = false
        setText(highlight(SpannableStringBuilder(text)))
        modified = true

        onTextChangedListener?.onTextChanged(text.toString())
    }


    val cleanText: String
        get() = trailingWhiteSpace!!.matcher(text).replaceAll("")


    fun refresh() {
        highlightWithoutChange(text)
    }


    fun cancelUpdate() {
        updateHandler!!.removeCallbacks(updateThread)
    }


    fun highlightWithoutChange(e: Editable) {
        modified = false
        highlight(e)
        modified = true
    }


    /* 实现语法高亮 */
    fun highlight(edit: Editable): Editable {
        try {
            clearSpans(edit)
            if (edit.length == 0)
                return edit

            if (errorLine > 0) {
                val m = line!!.matcher(edit)

                var n = errorLine
                while (n-- > 0 && m.find())
                    edit.setSpan(BackgroundColorSpan(res!!.getColor(R.color.syntax_error)), m.start(),
                            m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            run {
                val m = BLUE.matcher(edit)
                while (m.find()) {
                    edit.setSpan(ForegroundColorSpan(HighLight.COLOR_BLUE), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            run {
                val m = OTHER.matcher(edit)
                while (m.find()) {
                    edit.setSpan(ForegroundColorSpan(HighLight.COLOR_OTHER), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            run {
                val m = RED.matcher(edit)
                while (m.find()) {
                    edit.setSpan(ForegroundColorSpan(HighLight.COLOR_RED), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            val m = GREEN.matcher(edit)
            while (m.find()) {
                edit.setSpan(ForegroundColorSpan(HighLight.COLOR_GREEN), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        } catch (ex: Exception) {
        }

        return edit
    }


    /* 移除span */
    fun clearSpans(e: Editable) {
        val foreSpan = e.getSpans(0, e.length, ForegroundColorSpan::class.java)
        run {
            var n = foreSpan.size
            while (n-- > 0)
                e.removeSpan(foreSpan[n])
        }

        val backSpan = e.getSpans(0, e.length, BackgroundColorSpan::class.java)

        var n = backSpan.size
        while (n-- > 0)
            e.removeSpan(backSpan[n])

    }

    /* 自动插入内容 */
    fun autoIndent(source: CharSequence, start: Int, end: Int,
                   dest: Spanned, dstart: Int, dend: Int): CharSequence {
        var indent = ""
        var istart = dstart - 1
        var iend = -1

        var dataBefore = false
        var pt = 0

        while (istart > -1) {
            val c = dest[istart]

            if (c == '\n')
                break

            if (c != ' ' && c != '\t') {
                if (!dataBefore) {
                    if (c == '{' || c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^' || c == '=')
                        --pt

                    dataBefore = true
                }

                if (c == '(')
                    --pt
                else if (c == ')')
                    ++pt
            }
            --istart
        }

        if (istart > -1) {
            val charAtCursor = dest[dstart]

            iend = ++istart
            while (iend < dend) {
                val c = dest[iend]

                if (charAtCursor != '\n' && c == '/' && iend + 1 < dend && dest[iend] == c) {
                    iend += 2
                    break
                }

                if (c != ' ' && c != '\t')
                    break
                ++iend
            }

            indent += dest.subSequence(istart, iend)
        }

        if (pt < 0)
            indent += "\t"

        return source as String + indent
    }

    /* 跳转到指定行 */
    fun gotoLine(line: Int): Boolean {
        if (line > lineCount) {
            setSelection(text.toString().length)
            return false
        }

        val layout = layout
        setSelection(layout.getLineStart(line), layout.getLineEnd(line))
        return true
    }

    /* 获得行 行高 当前行 */
    val rowHeight: Int
        get() = lineHeight

    val totalRows: Int
        get() = lineCount

    val currRow: Int
        get() = layout.getLineForOffset(selectionStart) + 1

    interface OnTextChangedListener {
        fun onTextChanged(text: String)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (textSize.toInt() == 0) {
            textSize = this.getTextSize()
        }
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> mode = 1
            MotionEvent.ACTION_UP -> mode = 0
            MotionEvent.ACTION_POINTER_UP -> mode -= 1
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                mode += 1
            }

            MotionEvent.ACTION_MOVE -> if (mode >= 2) {
                val newDist = spacing(event)
                if (newDist > oldDist + 1) {
                    zoom(newDist / oldDist)
                    oldDist = newDist
                }
                if (newDist < oldDist - 1) {
                    zoom(newDist / oldDist)
                    oldDist = newDist
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun zoom(f: Float) {
        val value = textSize * f
        this.setTextSize(value)
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    companion object {
        private val GREEN = Pattern.compile("//(.*)|/\\*(.|[\r\n])*?\\*/|=|==")
        private val RED = Pattern.compile("true|false|\\b(\\d*[.]?\\d+)\\b|\".+?\"|\\d+(\\.\\d+)?")
        private val BLUE = Pattern.compile("class|import|extends|package|implements|switch|while|break|case|private|public|protected|void|super|Bundle|this|static|final|if|else|return|new|catch|try")
        private val OTHER = Pattern.compile(";|\\(|\\)|\\{|\\}|R\\..+?\\..+?|([\t|\n| ][A-Z].+? )|String |int |boolean |float |double |char |long |Override ")
    }

}

@Suppress("NOTHING_TO_INLINE")
public inline fun ViewManager.textEditorView() = textEditorView {}

public inline fun ViewManager.textEditorView(init: TextEditorView.() -> Unit) = ankoView({ TextEditorView(it) }, init)
