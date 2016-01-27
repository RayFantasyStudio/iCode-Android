package com.rayfantasy.icode.extension

import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout

fun SlidingUpPanelLayout.onPanelSlide(listener: _PanelSlideListener.() -> Unit)
        = setPanelSlideListener(_PanelSlideListener().apply(listener))

class _PanelSlideListener : SlidingUpPanelLayout.PanelSlideListener {
    private var _onPanelExpanded: ((View?) -> Unit)? = null
    private var _onPanelSlide: ((View?, Float) -> Unit)? = null
    private var _onPanelCollapsed: ((View?) -> Unit)? = null
    private var _onPanelHidden: ((View?) -> Unit)? = null
    private var _onPanelAnchored: ((View?) -> Unit)? = null

    override fun onPanelExpanded(panel: View?) {
        _onPanelExpanded?.invoke(panel)
    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
        _onPanelSlide?.invoke(panel, slideOffset)
    }

    override fun onPanelCollapsed(panel: View?) {
        _onPanelCollapsed?.invoke(panel)
    }

    override fun onPanelHidden(panel: View?) {
        _onPanelHidden?.invoke(panel)
    }

    override fun onPanelAnchored(panel: View?) {
        _onPanelAnchored?.invoke(panel)
    }

    fun onPanelExpanded(listener: (View?) -> Unit) {
        _onPanelExpanded = listener
    }

    fun onPanelSlide(listener: (View?, Float) -> Unit) {
        _onPanelSlide = listener
    }

    fun onPanelCollapsed(listener: (View?) -> Unit) {
        _onPanelCollapsed = listener
    }

    fun onPanelHidden(listener: (View?) -> Unit) {
        _onPanelHidden = listener
    }

    fun onPanelAnchored(listener: (View?) -> Unit) {
        _onPanelAnchored = listener
    }
}