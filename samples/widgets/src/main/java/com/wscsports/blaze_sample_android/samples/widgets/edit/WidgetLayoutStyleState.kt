package com.wscsports.blaze_sample_android.samples.widgets.edit

data class WidgetLayoutStyleState(
    val isCustomAppearance: Boolean = false,
    val isCustomStatusIndicator: Boolean = false,
    val isCustomTitle: Boolean = false,
    val isCustomBadge: Boolean = false,
    val isCustomItemStyleOverrides: Boolean = false,
) {
    fun isNotEmpty(): Boolean {
        return isCustomAppearance || isCustomStatusIndicator || isCustomTitle || isCustomBadge || isCustomItemStyleOverrides
    }
}