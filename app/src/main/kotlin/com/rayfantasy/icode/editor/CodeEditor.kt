/*
 * Copyright 2015. Alex Zhang aka. ztc1997
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rayfantasy.icode.editor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.*
import com.rayfantasy.icode.BuildConfig
import com.rayfantasy.icode.R
import org.jetbrains.anko.custom.ankoView


class CodeEditor @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ViewGroup(context, attrs, defStyle) {

    private var backPaint: Paint? = null            //绘制当前行的背景色画笔
    private var detector: GestureDetector? = null    //手势对象
    private val rectStartX: Int = 0
    private var rectStartY: Int = 0    //绘制当前行的背景色的开始X Y坐标
    private var rectEndX: Int = 0
    private var rectEndY: Int = 0        //绘制当前行的背景色的结束X Y坐标
    /* 获得EditText的行 行高 当前行 */
    var rowHeight: Int = 0
        private set
    var currRow: Int = 0
        private set        //行高 当前行
    var totalRows: Int = 0
        private set                //总共行数

    private var scrollX: Int? = 0
    private var scrollY: Int? = 0        //滚动的X Y距离
    //private InputMethodManager imm;        //输入法对象
    private var isKeyEnter = false    //是否按下确定键
    private var isKeyDelete = false//是否按下删除键
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0    //View的宽度和高度
    private var mChildHeight: Int = 0
    private var srcRow: Int = 0    //n-1个子View的高度
    var textEditor: TextEditorView? = null    //自定义的EditText对象

    private var screenWidth: Int = 0
    private var screenHeight: Int = 0//屏幕的宽度和高度

    private val MARGIN_LEFT = 100//文本左侧的间距


    init {
        init(context)
    }


    /* 初始化一些参数 */
    fun init(context: Context) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        screenWidth = windowManager.defaultDisplay.width
        screenHeight = windowManager.defaultDisplay.height

        //imm = (InputMethodManager) getContext()
        //        .getSystemService(Context.INPUT_METHOD_SERVICE);
        detector = GestureDetector(GestureListener(this))
        setBackgroundColor(context.resources.getColor(R.color.editor_backgroud))
        backPaint = Paint()
        backPaint!!.color = context.resources.getColor(R.color.current_row_backgroud)
        //rectStartX = MARGIN_LEFT;
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        //获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMode)
        val heightSize = View.MeasureSpec.getSize(heightMode)

        calcuDimension(widthMode, heightMode, widthSize, heightSize)

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec)

    }


    /* 计算子View的大小 */
    fun calcuDimension(widthMode: Int, heightMode: Int, widthSize: Int, heightSize: Int) {

        //计算子View的尺寸
        var width = 0
        var height = 0
        var childWidth = 0
        var childHeight = 0
        var mLayoutParams: ViewGroup.MarginLayoutParams? = null

        for (i in 0..childCount - 1) {
            val childView = getChildAt(i)
            childWidth = getChildAt(i).measuredWidth
            childHeight = getChildAt(i).measuredHeight
            mLayoutParams = childView.layoutParams as ViewGroup.MarginLayoutParams

            width = childWidth + mLayoutParams.leftMargin + mLayoutParams.rightMargin
            height = childHeight + mLayoutParams.topMargin + mLayoutParams.bottomMargin

        }

        //设置View的尺寸
        if (width < screenWidth) {
            width = screenWidth + screenWidth / 2
        } else {
            width = width + screenWidth / 2
        }

        if (height < screenHeight) {
            height = screenHeight + screenHeight / 2
        } else {
            height = height + screenHeight / 2
        }
        rectEndX = width

        if (BuildConfig.DEBUG)
            println("calcuDimension width: $width height: $height")

        //如果是wrap_content设置为我们计算的值，否则：直接设置为父容器计算的值
        setMeasuredDimension(if ((widthMode == View.MeasureSpec.EXACTLY))
            widthSize
        else
            width, if ((heightMode == View.MeasureSpec.EXACTLY)) heightSize else height)
    }


    /*
     * 父View对子View进行布局，按垂直线性进行布局
     * 设置子View的宽度在为 childWidth +rectEnd
     * 否则在子View外单击，不会改变当前行的背景色
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var l = l
        var t = t
        var r = r
        var b = b


        var childWidth = 0
        var childHeight = 0
        var mLayoutParams: ViewGroup.MarginLayoutParams? = null
        for (i in 0..childCount - 1) {
            val childView = getChildAt(i)
            childWidth = getChildAt(i).measuredWidth
            childHeight = getChildAt(i).measuredHeight
            mLayoutParams = childView.layoutParams as ViewGroup.MarginLayoutParams


            l = mLayoutParams.leftMargin
            if (i == 0) {
                t = mLayoutParams.topMargin
            } else {
                t = b + mLayoutParams.topMargin
            }

            r = l + childWidth + rectEndX
            b = t + childHeight

            childView.layout(l, t, r, b)

        }


    }


    /* 这三个方法必须覆写，否则无法获得MarginLayoutParams的对象和属性 */
    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(
            p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(p)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //此处必须强制刷新，否则子View不会时时刷新
        invalidate()
    }


    override fun dispatchDraw(canvas: Canvas) {
        if (textEditor == null) {
            textEditor = findViewById(R.id.tv_code) as? TextEditorView
        }
        super.dispatchDraw(canvas)
        canvas.save()
        rowHeight = textEditor!!.rowHeight
        currRow = textEditor!!.currRow
        totalRows = textEditor!!.totalRows
        //如果当前选择的行，与之前选择的行不同，才进行计算，提高效率
        if (srcRow != currRow) {
            var totalHeight = 0
            rectEndY = currRow * rowHeight + rowHeight / 5
            rectStartY = rectEndY - rowHeight - rowHeight / 10
            for (i in 0..childCount - 1 - 1) {
                totalHeight += getChildAt(i).measuredHeight
            }
            mChildHeight = totalHeight
            srcRow = currRow

        }
    }


    //必须在此方法中绘制当前行的背景色，否则背景色会覆盖文本
    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        if (textEditor == null) {
            textEditor = findViewById(R.id.tv_code) as TextEditorView
        }
        if (!textEditor!!.hasSelection()) {
            canvas.save()
            //调整Y的坐标
            if ((currRow + 1) * rowHeight + rowHeight / 4 - scrollY!! >= viewHeight - mChildHeight && isKeyEnter == true) {
                rectEndY = scrollY!! + viewHeight - mChildHeight
                rectStartY = rectEndY - rowHeight
            }

            if ((currRow + 1) * rowHeight - scrollY!! <= rowHeight / 2 && isKeyDelete == true) {
                rectEndY = scrollY!! - rowHeight - rowHeight / 4
                rectStartY = rectEndY - rowHeight
            }

            canvas.drawRect(textEditor!!.paddingLeft.toFloat(), (rectStartY + mChildHeight).toFloat(), rectEndX.toFloat(), (rectEndY + mChildHeight).toFloat(), backPaint)
            canvas.restore()
        }
        return super.drawChild(canvas, child, drawingTime)
    }


    /* 此处交给手势去处理，就能在子View以外单击也能弹出输入法 */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        isKeyEnter = false
        isKeyDelete = false
        return detector!!.onTouchEvent(event)
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        isKeyEnter = false
        isKeyDelete = false
        return super.dispatchTouchEvent(ev)
    }


    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        when (event.keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                isKeyEnter = true
                isKeyDelete = false
            }
            KeyEvent.KEYCODE_DEL -> {
                isKeyEnter = false
                isKeyDelete = true
            }
            else -> {
                isKeyEnter = false
                isKeyDelete = false
            }
        }
        return super.dispatchKeyEvent(event)
    }


    /*    *//* 显示隐藏输入法 *//*
    public void showIME(boolean show) {
        if (show) {
            imm.showSoftInput(textEditor, 0);

        } else {
            imm.hideSoftInputFromWindow(textEditor.getWindowToken(), 0);
        }
    }*/


    /* 获得View的宽度和高度 */
    fun getViewWidth(viewWidth: Int) {
        this.viewWidth = viewWidth
    }

    fun getViewHeight(viewHeight: Int) {
        this.viewHeight = viewHeight
    }


    /* 获得View滚动的X Y距离 */
    fun scrollX(scrollX: Int) {
        this.scrollX = scrollX
    }

    fun scrollY(scrollY: Int) {
        this.scrollY = scrollY
    }

}

@Suppress("NOTHING_TO_INLINE")
public inline fun ViewManager.codeEditor() = codeEditor {}

public inline fun ViewManager.codeEditor(init: CodeEditor.() -> Unit) = ankoView({ CodeEditor(it) }, init)