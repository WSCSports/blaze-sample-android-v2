package com.wscsports.blaze_sample_android.samples.widgets.screens

import androidx.core.graphics.toColorInt
import com.blaze.blazesdk.data_source.BlazeAdvancedOrderType
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.extentions.blazeDeepCopy
import com.blaze.blazesdk.style.shared.models.BlazeObjectXPosition
import com.blaze.blazesdk.style.shared.models.BlazeObjectYPosition
import com.blaze.blazesdk.style.shared.models.blazeDp
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemBadgeStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemCustomMapping
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemImageStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemStatusIndicatorStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemStyleOverrides
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemTitleStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.core.Constants.LIVE_VIDEOS_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.samples.widgets.R
import com.wscsports.blaze_sample_android.samples.widgets.WidgetScreenType
import com.wscsports.blaze_sample_android.samples.widgets.WidgetsViewModel
import com.wscsports.blaze_sample_android.samples.widgets.databinding.FragmentLiveVideoRowBinding
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetDataState
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetLayoutStyleState
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

/**
 * [LiveVideoRowFragment] is a Fragment that displays a row of the Videos widget filtered to
 * live/upcoming/ended stream content via `videosFilterParams`, with a customization example
 * for the per-stream-state status indicator (the "LIVE"/"UPCOMING"/"ENDED" badge).
 * For more information on [BlazeVideosWidgetRowView], see https://dev.wsc-sports.com/docs/android-widgets#videos-row
 *
 * Note: unlike iOS, there is currently no public API to customize the full-screen player's own
 * status indicator on Android (`BlazeVideosPlayerStyle.statusIndicator` is internal) - only the
 * widget-cell badge is customizable here.
 */
class LiveVideoRowFragment : BaseWidgetFragment(R.layout.fragment_live_video_row) {

    private val binding by viewBinding(FragmentLiveVideoRowBinding::bind)
    override val widgetType = WidgetScreenType.LIVE_VIDEO_ROW

    override fun initWidgetView() {
        val widgetLayout = viewModel.getWidgetLayoutBasePreset()
        val dataState = viewModel.getCurrWidgetDataState()
        // advancedOrderType takes priority over orderType, surfacing live streams ahead of
        // upcoming/ended/VOD content regardless of the base order type.
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(LIVE_VIDEOS_WIDGET_DEFAULT_LABEL),
            orderType = dataState.orderType,
            advancedOrderType = BlazeAdvancedOrderType.LiveFirst,
        )
        binding.liveVideoRowWidgetView.initWidget(
            widgetLayout = widgetLayout,
            dataSource = dataSource,
            // Without this, only VOD content is returned - streams are opted-in explicitly.
            videosFilterParams = viewModel.liveVideoFilterParams,
            widgetId = widgetType.name,
            widgetDelegate = this,
        )
    }

    override fun onNewWidgetLayoutState(styleState: WidgetLayoutStyleState) {
        val newWidgetLayout = viewModel.getWidgetLayoutBasePreset().apply {
            if (styleState.isCustomAppearance) widgetItemStyle.image.setMyCustomImageStyle()
            if (styleState.isCustomStatusIndicator) widgetItemStyle.statusIndicator.setMyCustomStatusIndicatorStyle()
            if (styleState.isCustomTitle) widgetItemStyle.title.setMyCustomTitleStyle()
            if (styleState.isCustomBadge) widgetItemStyle.badge.setMyCustomBadgeStyle()
        }
        binding.liveVideoRowWidgetView.updateWidgetLayout(newWidgetLayout)
        if (styleState.isCustomItemStyleOverrides) {
            setOverrideStylesByGameId(newWidgetLayout)
        } else {
            binding.liveVideoRowWidgetView.resetOverriddenStyles()
        }
    }

    override fun onNewDatasourceState(dataState: WidgetDataState) {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(LIVE_VIDEOS_WIDGET_DEFAULT_LABEL),
            orderType = dataState.orderType,
            advancedOrderType = BlazeAdvancedOrderType.LiveFirst,
        )
        binding.liveVideoRowWidgetView.updateDataSource(dataSource, false)
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-image-style
    private fun BlazeWidgetItemImageStyle.setMyCustomImageStyle() {
        cornerRadius = 12.blazeDp
        cornerRadiusRatio = null
        border.apply {
            isVisible = true
            val borderColor = "#FF3131".toColorInt()
            val borderWidth = 2.blazeDp
            unreadState.apply { width = borderWidth; color = borderColor; margin = 0.blazeDp }
            readState.apply { width = borderWidth; color = borderColor; margin = 0.blazeDp }
            liveUnreadState.apply { width = borderWidth; color = borderColor; margin = 0.blazeDp }
            liveReadState.apply { width = borderWidth; color = borderColor; margin = 0.blazeDp }
        }
    }

    // Customizes the per-stream-state badge (the "LIVE"/"UPCOMING"/"ENDED" chip), rather than
    // the generic read/unread indicator - this is what actually reflects a video's stream status.
    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-status-indicator-style
    private fun BlazeWidgetItemStatusIndicatorStyle.setMyCustomStatusIndicatorStyle() {
        isVisible = true
        position.xPosition = BlazeObjectXPosition.START_TO_START
        position.yPosition = BlazeObjectYPosition.TOP_TO_TOP
        margins.apply {
            start = 8.blazeDp
            top = 8.blazeDp
        }
        padding.apply {
            end = 12.blazeDp
            start = 8.blazeDp
        }

        val textSize = 12f
        val cornerRadius = 8.blazeDp

        streamStates.liveStreamState.apply {
            backgroundColor = "#FF3131".toColorInt()
            textStyle.textColor = "#FFFFFF".toColorInt()
            textStyle.textSize = textSize
            this.cornerRadius = cornerRadius
        }
        streamStates.upcomingStreamState.apply {
            backgroundColor = "#3357FF".toColorInt()
            textStyle.textColor = "#FFFFFF".toColorInt()
            textStyle.textSize = textSize
            this.cornerRadius = cornerRadius
        }
        streamStates.endedStreamState.apply {
            backgroundColor = "#888888".toColorInt()
            textStyle.textColor = "#FFFFFF".toColorInt()
            textStyle.textSize = textSize
            this.cornerRadius = cornerRadius
        }
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-title-style
    private fun BlazeWidgetItemTitleStyle.setMyCustomTitleStyle() {
        isVisible = true
        position.apply {
            xPosition = BlazeObjectXPosition.START_TO_START
            yPosition = BlazeObjectYPosition.BOTTOM_TO_BOTTOM
        }
        val customTextColor = "#FFFFFF".toColorInt()
        readState.apply {
            textColor = customTextColor
            textSize = 15f
            maxLines = 1
        }
        unreadState.apply {
            textColor = customTextColor
            textSize = 15f
            maxLines = 1
        }
        margins.apply {
            bottom = 12.blazeDp
        }
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-badge-style
    private fun BlazeWidgetItemBadgeStyle.setMyCustomBadgeStyle() {
        isVisible = true
        position.xPosition = BlazeObjectXPosition.START_TO_START
        position.yPosition = BlazeObjectYPosition.TOP_TO_TOP
        // Offset below the status indicator chip so the two don't overlap.
        margins.apply {
            start = 8.blazeDp
            top = 30.blazeDp
        }
        val text = "HD"
        val customBackgroundColor = "#FF3131".toColorInt()
        val customTextColor = "#FFFFFF".toColorInt()
        val customSize = 22.blazeDp
        unreadState.apply {
            this.text = text
            textStyle.textColor = customTextColor
            backgroundColor = customBackgroundColor
            width = customSize
            height = customSize
        }
        readState.apply {
            this.text = text
            textStyle.textColor = customTextColor
            backgroundColor = customBackgroundColor
            width = customSize
            height = customSize
        }
        liveUnreadState.apply {
            this.text = text
            textStyle.textColor = customTextColor
            backgroundColor = customBackgroundColor
            width = customSize
            height = customSize
        }
        liveReadState.apply {
            this.text = text
            textStyle.textColor = customTextColor
            backgroundColor = customBackgroundColor
            width = customSize
            height = customSize
        }
    }

    // Example of setting custom styles for a specific widget item by its game ID.
    // We get the mapping key and value from the BE, inside the item object entities field.
    // For more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-custom-mapping#/
    private fun setOverrideStylesByGameId(widgetLayout: BlazeWidgetLayout) {
        val layoutDeepCopy = widgetLayout.blazeDeepCopy()
        val mappingKey = BlazeWidgetItemCustomMapping.BlazeKeysPresets.GAME_ID
        val mappingValue = "2445381"
        binding.liveVideoRowWidgetView.updateOverrideStyles(
            perItemStyleOverrides = mapOf(
                BlazeWidgetItemCustomMapping(mappingKey, mappingValue) to getBlazeWidgetItemStyleOverrides(layoutDeepCopy)
            ),
            shouldUpdateUi = true
        )
    }

    private fun getBlazeWidgetItemStyleOverrides(newWidgetLayout: BlazeWidgetLayout) =
        BlazeWidgetItemStyleOverrides(
            imageBorder = newWidgetLayout.widgetItemStyle.image.border.apply {
                isVisible = true
                val accentColor = "#FFD700".toColorInt()
                val borderWidth = 3.blazeDp
                unreadState.apply { width = borderWidth; color = accentColor; margin = 0.blazeDp }
                readState.apply { width = borderWidth; color = accentColor; margin = 0.blazeDp }
                liveUnreadState.apply { width = borderWidth; color = accentColor; margin = 0.blazeDp }
                liveReadState.apply { width = borderWidth; color = accentColor; margin = 0.blazeDp }
            },
            statusIndicator = newWidgetLayout.widgetItemStyle.statusIndicator.apply {
                val accentColor = "#FFD700".toColorInt()
                streamStates.liveStreamState.apply {
                    backgroundColor = accentColor
                    textStyle.textColor = "#000000".toColorInt()
                    text = "FEATURED LIVE"
                }
            }
        )
}
