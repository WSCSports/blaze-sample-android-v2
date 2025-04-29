package com.wscsports.blaze_sample_android.samples.playerstyle

import androidx.lifecycle.ViewModel
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import androidx.core.graphics.toColorInt

class PlayerStyleViewModel: ViewModel() {

    val storiesDataSource: BlazeDataSourceType
        get() = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel("top-stories"))

    val momentsDataSource: BlazeDataSourceType
        get() = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel("moments"))

    val storiesWidgetLayout: BlazeWidgetLayout
        get() = BlazeWidgetLayout.Presets.StoriesWidget.Row.circles

    val momentsWidgetLayout: BlazeWidgetLayout
        get() = BlazeWidgetLayout.Presets.MomentsWidget.Row.verticalAnimatedThumbnailsRectangles

    val defaultStoryPlayerStyle: BlazeStoryPlayerStyle
        get() = BlazeStoryPlayerStyle.base()

    val defaultMomentPlayerStyle: BlazeMomentsPlayerStyle
        get() = BlazeMomentsPlayerStyle.base()

    val customStoryPlayerStyle = defaultStoryPlayerStyle.applyCustomStoryPlayerParams()
    
    val customMomentPlayerStyle = defaultMomentPlayerStyle.applyCustomMomentsPlayerParams()

    companion object {
        const val HEADER_TEXT_SIZE = 22f
        const val DESCRIPTION_TEXT_SIZE = 16f

        val seekBarUnplayedSegmentColor = "#33FFFFFF".toColorInt()
        val seekBarPlayedSegmentColor = "#FFFFFF".toColorInt()

        val liveChipBgColor = "#FF364E".toColorInt()
        val adChipBgColor = "#FFAE00".toColorInt()
    }

}