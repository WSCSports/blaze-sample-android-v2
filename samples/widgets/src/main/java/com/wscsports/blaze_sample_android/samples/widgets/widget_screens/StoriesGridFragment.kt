package com.wscsports.blaze_sample_android.samples.widgets.widget_screens

import android.graphics.Color
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.extentions.blazeDeepCopy
import com.blaze.blazesdk.style.shared.models.BlazeObjectXPosition
import com.blaze.blazesdk.style.shared.models.BlazeObjectYPosition
import com.blaze.blazesdk.style.shared.models.blazeDp
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemBadgeStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemCustomMapping
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemImageGradientOverlayStyle.BlazeGradientPosition
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemImageStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemImageStyle.BlazeImagePosition
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemImageStyle.BlazeThumbnailType
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemStatusIndicatorStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemStyleOverrides
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemTitleStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.samples.widgets.R
import com.wscsports.blaze_sample_android.samples.widgets.WidgetScreenType
import com.wscsports.blaze_sample_android.samples.widgets.databinding.FragmentStoriesGridBinding
import com.wscsports.blaze_sample_android.samples.widgets.widget_screens.state.WidgetDataState
import com.wscsports.blaze_sample_android.samples.widgets.widget_screens.state.WidgetLayoutStyleState
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

/**
 * StoriesGridFragment is a Fragment that displays a grid of stories.
 * It manages widget initialization, style customization, and data source updates.
 * For more information on [BlazeStoriesWidgetGridView], see https://dev.wsc-sports.com/docs/android-widgets#/stories-grid
 */
class StoriesGridFragment : BaseWidgetFragment(R.layout.fragment_stories_grid) {

    private val binding by viewBinding(FragmentStoriesGridBinding::bind)
    override val widgetType = WidgetScreenType.STORIES_GRID

    override fun initWidgetView() {
        // The custom layout can also be set during initialization, rather than using updateWidgetLayout.
        // In this case, we are setting the layout to some default preset.
        val widgetLayout = viewModel.getWidgetLayoutPreset()
        val dataState = viewModel.getCurrWidgetDataState()
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(dataState.labelName),
            orderType = dataState.orderType,
        )
        binding.storiesGridWidgetView.initWidget(
            widgetLayout = widgetLayout,
            dataSource = dataSource,
            widgetId = "stories-grid",
            widgetDelegate = this,
            shouldOrderWidgetByReadStatus = true,
        )
    }

    override fun onNewDatasourceState(dataState: WidgetDataState) {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(dataState.labelName),
            orderType = dataState.orderType,
        )
        binding.storiesGridWidgetView.updateDataSource(dataSource, false)
    }

    override fun onNewWidgetLayoutState(styleState: WidgetLayoutStyleState) {
        val newWidgetLayout = viewModel.getWidgetLayoutPreset().apply {
            if (styleState.isCustomImage) widgetItemStyle.image.setMyCustomImageStyle()
            if (styleState.isCustomStatusIndicator) widgetItemStyle.statusIndicator.setMyCustomIndicatorStyle()
            if (styleState.isCustomTitle) widgetItemStyle.title.setMyCustomTitleStyle()
            if (styleState.isCustomBadge) widgetItemStyle.badge.setMyCustomBadgeStyle()
        }
        binding.storiesGridWidgetView.updateWidgetLayout(newWidgetLayout)
        if (styleState.isCustomItemStyleOverrides) {
            setOverrideStylesByGameId(newWidgetLayout)
        } else {
            binding.storiesGridWidgetView.resetOverriddenStyles()
        }
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-image-style
    private fun BlazeWidgetItemImageStyle.setMyCustomImageStyle() {
        height = 150.blazeDp
        position = BlazeImagePosition.TopCenter
        cornerRadiusRatio = 0.25f
        ratio = 4f/5f
        context?.let { context ->
            border.apply {
                isVisible = true
                liveUnreadState.color = context.getColor(R.color.mediumspringgreen)
                liveReadState.color = context.getColor(R.color.darkslategray)
                unreadState.color = context.getColor(R.color.coral)
                readState.color = context.getColor(R.color.gray)
            }
        }
        margins.apply {
            top = 2.blazeDp
            bottom = 2.blazeDp
            start = 2.blazeDp
            end = 2.blazeDp
        }
        thumbnailType = BlazeThumbnailType.VERTICAL_TWO_BY_THREE
        gradientOverlay.apply {
            isVisible = true
            startColor = Color.BLACK
            endColor = Color.TRANSPARENT
            position = BlazeGradientPosition.BOTTOM
        }
        animatedThumbnail.apply {
            isEnabled = false
            horizontalAnimationTriggerPercentage = 0.3f
        }
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-status-indicator-style
    private fun BlazeWidgetItemStatusIndicatorStyle.setMyCustomIndicatorStyle() {
        isVisible = true
        position.xPosition = BlazeObjectXPosition.START_TO_START
        position.yPosition = BlazeObjectYPosition.BOTTOM_TO_BOTTOM
        liveUnreadState.apply {
            backgroundColor = Color.RED
            text = "Live-New"
            cornerRadius = 5.blazeDp
            borderColor = Color.YELLOW
            borderWidth = 2.blazeDp
        }
        liveReadState.apply {
            backgroundColor = Color.LTGRAY
            text = "Live-Read"
            cornerRadius = 5.blazeDp
            borderColor = Color.DKGRAY
            borderWidth = 2.blazeDp
        }
        unreadState.apply {
            backgroundColor = Color.GREEN
            text = "New"
            cornerRadius = 5.blazeDp
            borderColor = Color.WHITE
            borderWidth = 2.blazeDp
        }
        readState.apply {
            backgroundColor = Color.DKGRAY
            text = "Read"
            cornerRadius = 5.blazeDp
            borderColor = Color.BLACK
            borderWidth = 2.blazeDp
        }
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-title-style
    private fun BlazeWidgetItemTitleStyle.setMyCustomTitleStyle() {
        isVisible = true
        readState.apply {
            textColor =  context?.getColor(R.color.dimgray) ?: Color.BLACK
            textSize = 14f
            maxLines = 2
            position.xPosition = BlazeObjectXPosition.CENTER_X
            position.yPosition = BlazeObjectYPosition.BOTTOM_TO_BOTTOM
        }
        unreadState.apply {
            textColor = context?.getColor(R.color.darkmagenta) ?: Color.BLACK
            textSize = 14f
            maxLines = 2
            position.xPosition = BlazeObjectXPosition.CENTER_X
            position.yPosition = BlazeObjectYPosition.TOP_TO_BOTTOM
        }
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-badge-style
    private fun BlazeWidgetItemBadgeStyle.setMyCustomBadgeStyle() {
        isVisible = true
        position.xPosition = BlazeObjectXPosition.END_TO_END
        position.yPosition = BlazeObjectYPosition.TOP_TO_TOP
        unreadState.backgroundColor = Color.BLUE
        unreadState.cornerRadiusRatio = 0.5f
        unreadState.borderColor = Color.WHITE
        unreadState.borderWidth = 2.blazeDp
        liveUnreadState.backgroundColor = Color.YELLOW
        liveUnreadState.cornerRadiusRatio = 0.5f
    }

    // Example of setting custom styles for a specific widget item by it game ID.
    // We get the mapping key and value from the BE, inside the item object entities field.
    // For more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-custom-mapping#/
    private fun setOverrideStylesByGameId(widgetLayout: BlazeWidgetLayout) {
        val layoutDeepCopy = widgetLayout.blazeDeepCopy()
        val mappingKey =  BlazeWidgetItemCustomMapping.BlazeKeysPresets.GAME_ID
        val mappingValue = "0022300858"
        binding.storiesGridWidgetView.updateOverrideStyles(
            perItemStyleOverrides = mapOf(
                BlazeWidgetItemCustomMapping(mappingKey, mappingValue) to getBlazeWidgetItemStyleOverrides(layoutDeepCopy)
            ),
            shouldUpdateUi = true
        )
    }

    // For more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-style-overrides#/
    private fun getBlazeWidgetItemStyleOverrides(newWidgetLayout: BlazeWidgetLayout) =
        BlazeWidgetItemStyleOverrides(
            badge = newWidgetLayout.widgetItemStyle.badge.apply {
                isVisible = true
                position.apply {
                    xPosition = BlazeObjectXPosition.END_TO_END
                    yPosition = BlazeObjectYPosition.TOP_TO_TOP
                }
                unreadState.apply {
                    cornerRadiusRatio = 0.5f
                    borderColor = Color.CYAN
                    borderWidth = 4.blazeDp
                }
                readState.apply {
                    backgroundColor = Color.MAGENTA
                    cornerRadiusRatio = 0.5f
                    borderColor = Color.GRAY
                    borderWidth = 2.blazeDp
                }
                liveUnreadState.apply {
                    backgroundColor = Color.YELLOW
                    cornerRadiusRatio = 0.5f
                    isVisible = true
                }
                liveReadState.apply {
                    backgroundColor = Color.GREEN
                    cornerRadiusRatio = 0.5f
                    isVisible = true
                }
            },
            statusIndicator = newWidgetLayout.widgetItemStyle.statusIndicator.apply {
                liveReadState.apply {
                    backgroundColor = Color.MAGENTA
                    text = "Live-customized"
                    cornerRadius = 5.blazeDp
                    borderColor = Color.DKGRAY
                    borderWidth = 2.blazeDp
                }
            }
        )

}