package com.example.elson.popmovies.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnDetachedFromWindow
import androidx.fragment.app.Fragment

/**
 * Inflate a compose view in a Fragment just like how we do in an activity
 */
fun Fragment.contentView(
    compositionStrategy: ViewCompositionStrategy = DisposeOnDetachedFromWindow,
    context: Context = requireContext(),
    content: @Composable () -> Unit
) = ComposeView(context).apply {
    setViewCompositionStrategy(compositionStrategy)
    setContent(content)
}
