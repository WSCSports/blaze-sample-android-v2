package com.wscsports.blaze_sample_android.samples.playerstyle

import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout

fun BlazeWidgetLayout.applyCustomMomentsWidgetParams(): BlazeWidgetLayout {
    return this.apply {
        /** Show the play icon overlay on each widget item thumbnail */
        widgetItemStyle.playIcon.isVisible = true
    }
}

fun BlazeWidgetLayout.applyCustomVideosWidgetParams(): BlazeWidgetLayout {
    return this.apply {
        /** Show the play icon overlay on each widget item thumbnail */
        widgetItemStyle.playIcon.isVisible = true
    }
}
