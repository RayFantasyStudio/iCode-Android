package com.rayfantasy.icode.editor

import android.view.GestureDetector
import android.view.MotionEvent

/*
 * 手势监听处理，在子View以外单击时弹出输入法
 */

class GestureListener(private val codeEditor: CodeEditor) : GestureDetector.SimpleOnGestureListener(), GestureDetector.OnGestureListener {


    override fun onDown(e: MotionEvent): Boolean {
        return true
    }


    /* down事件发生而move或up还没发生前，触发该事件 */
    override fun onShowPress(e: MotionEvent) {
    }


    override fun onSingleTapUp(e: MotionEvent): Boolean {
        //codeEditor.showIME(true);
        return true
    }


    /* 在屏幕上拖动事件，在ACTION_MOVE动作发生时触发，会多次触发 */
    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        return true
    }


    /* 长按事件，Touch了不移动一直Touch down时触发 */
    override fun onLongPress(e: MotionEvent) {
    }


    /* 滑动手势事件，Touch了滑动一点距离后，在抬起时才会触发 */
    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        return true
    }


}
