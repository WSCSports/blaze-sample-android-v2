package com.wscsports.android.blaze.blaze_sample_android.core.ui

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding

fun View.showView(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.INVISIBLE
}

fun View.showOrGone(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

fun View.applySafeAreaPadding(shouldIncludeBottom: Boolean = true) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        val systemBarsInsets = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or
                    WindowInsetsCompat.Type.displayCutout()
        )

        val bottomPadding = if (shouldIncludeBottom) systemBarsInsets.bottom else 0
        updatePadding(
            top = systemBarsInsets.top, // Padding for the status bar
            left = systemBarsInsets.left, // Padding for the left side
            right = systemBarsInsets.right, // Padding for the right side
            bottom = bottomPadding // Padding for the bottom side
        )

        insets
    }
}