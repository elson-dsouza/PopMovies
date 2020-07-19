package com.example.elson.popmovies.ui.movies.detail

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebChromeClient
import android.widget.FrameLayout

internal class MovieDetailWebClient(private val activity: Activity) : WebChromeClient() {
    private var mCustomView: View? = null
    private var mCustomViewCallback: CustomViewCallback? = null

    override fun onHideCustomView() {
        (activity.window.decorView as FrameLayout).removeView(mCustomView)
        mCustomView = null
        mCustomViewCallback!!.onCustomViewHidden()
        mCustomViewCallback = null
        showSystemUI()
    }

    override fun onShowCustomView(paramView: View?, paramCustomViewCallback: CustomViewCallback?) {
        paramView ?: return
        if (mCustomView != null) {
            onHideCustomView()
            return
        }
        hideSystemUI()
        paramView.setBackgroundColor(Color.BLACK)
        mCustomView = paramView
        mCustomViewCallback = paramCustomViewCallback
        (activity.window.decorView as FrameLayout).addView(mCustomView,
                FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
    }

    private fun hideSystemUI() {
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun showSystemUI() {
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}