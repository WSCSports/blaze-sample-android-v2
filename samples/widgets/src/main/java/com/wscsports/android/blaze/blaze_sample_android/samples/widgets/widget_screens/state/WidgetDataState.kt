package com.wscsports.android.blaze.blaze_sample_android.samples.widgets.widget_screens.state

import com.blaze.blazesdk.data_source.BlazeOrderType

data class WidgetDataState(
    val labelName: String = "live-stories",
    val orderType: BlazeOrderType = BlazeOrderType.MANUAL,
)
