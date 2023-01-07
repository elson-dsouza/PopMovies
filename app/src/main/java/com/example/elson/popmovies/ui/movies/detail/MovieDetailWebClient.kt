package com.example.elson.popmovies.ui.movies.detail

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

internal class MovieDetailWebClient(private val activity: Activity) : WebChromeClient() {
    private var mCustomView: View? = null
    private var mCustomViewCallback: CustomViewCallback? = null

    override fun onHideCustomView() {
        mCustomView?.visibility = View.GONE
        (activity.window.decorView as FrameLayout).removeView(mCustomView)
        mCustomViewCallback?.onCustomViewHidden()
        mCustomView = null
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
        (activity.window.decorView as FrameLayout).addView(
            mCustomView,
            FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        )
    }

    private fun hideSystemUI() {
        val windowController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        windowController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun showSystemUI() {
        val windowController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        windowController.show(WindowInsetsCompat.Type.systemBars())
    }
}
