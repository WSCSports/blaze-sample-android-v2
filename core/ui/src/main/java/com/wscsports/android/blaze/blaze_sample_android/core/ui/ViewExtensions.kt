package com.wscsports.android.blaze.blaze_sample_android.core.ui

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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