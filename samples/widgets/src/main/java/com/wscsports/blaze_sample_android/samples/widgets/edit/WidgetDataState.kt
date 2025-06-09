package com.wscsports.blaze_sample_android.samples.widgets.edit

import com.blaze.blazesdk.data_source.BlazeOrderType

data class WidgetDataState(
    val labelName: String = "live-stories",
    val orderType: BlazeOrderType = BlazeOrderType.MANUAL,
)
