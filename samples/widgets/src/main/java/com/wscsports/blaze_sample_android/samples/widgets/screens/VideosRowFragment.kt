package com.wscsports.blaze_sample_android.samples.widgets.screens

import android.graphics.Color
import androidx.core.graphics.toColorInt
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
import com.wscsports.blaze_sample_android.samples.widgets.databinding.FragmentVideosRowBinding
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetDataState
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetLayoutStyleState
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

/**
 * [VideosRowFragment] is a Fragment that displays a row of Videos.
 * It manages widget initialization, style customization, and data source updates.
 * For more information on [BlazeVideosWidgetRowView], see https://dev.wsc-sports.com/docs/android-widgets#videos-row
 */
class VideosRowFragment : BaseWidgetFragment(R.layout.fragment_videos_row) {

    private val binding by viewBinding(FragmentVideosRowBinding::bind)
    override val widgetType = WidgetScreenType.VIDEOS_ROW

    override fun initWidgetView() {
        // The custom layout can also be set during initialization, rather than using updateWidgetLayout.
        // In this case, we are setting the layout to some default preset.
        val widgetLayout = viewModel.getWidgetLayoutBasePreset()
        val dataState = viewModel.getCurrWidgetDataState()
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(dataState.labelName),
            orderType = dataState.orderType,
        )
        binding.videosRowWidgetView.initWidget(
            widgetLayout = widgetLayout,
            dataSource = dataSource,
            widgetId = widgetType.name, // Or any unique identifier for the widget
            widgetDelegate = this,
            shouldOrderWidgetByReadStatus = true,
        )
    }

    override fun onNewWidgetLayoutState(styleState: WidgetLayoutStyleState) {
        val newWidgetLayout = viewModel.getWidgetLayoutBasePreset().apply {
            if (styleState.isCustomAppearance) widgetItemStyle.image.setMyCustomImageStyle()
            if (styleState.isCustomStatusIndicator) widgetItemStyle.statusIndicator.setMyCustomStatusIndicatorStyle()
            if (styleState.isCustomTitle) widgetItemStyle.title.setMyCustomTitleStyle()
            if (styleState.isCustomBadge) widgetItemStyle.badge.setMyCustomBadgeStyle()
        }
        binding.videosRowWidgetView.updateWidgetLayout(newWidgetLayout)
        if (styleState.isCustomItemStyleOverrides) {
            setOverrideStylesByTeamId(newWidgetLayout)
        } else {
            binding.videosRowWidgetView.resetOverriddenStyles()
        }
    }

    override fun onNewDatasourceState(dataState: WidgetDataState) {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(dataState.labelName),
            orderType = dataState.orderType,
        )
        binding.videosRowWidgetView.updateDataSource(dataSource, false)
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-image-style
    private fun BlazeWidgetItemImageStyle.setMyCustomImageStyle() {
        position = BlazeImagePosition.TopCenter
        cornerRadius = 0.blazeDp
        cornerRadiusRatio = null
        border.apply {
            isVisible = true
            val borderColor = "#FF60C9".toColorInt()
            val borderWidth = 3.blazeDp
            liveUnreadState.apply {
                width = borderWidth
                color = borderColor
                margin = 0.blazeDp
            }
            liveReadState.apply {
                width = borderWidth
                color = borderColor
                margin = 0.blazeDp
            }
            unreadState.apply {
                width = borderWidth
                color = borderColor
                margin = 0.blazeDp
            }
            readState.apply {
                width = borderWidth
                color = borderColor
                margin = 0.blazeDp
            }
        }
    }

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
        unreadState.apply {
            backgroundColor = "#FFC7F2".toColorInt()
            text = "NEW"
            textStyle.apply {
                textSize = 12f
                textColor = "#333333".toColorInt()
            }
            cornerRadius = 8.blazeDp
        }
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-title-style
    private fun BlazeWidgetItemTitleStyle.setMyCustomTitleStyle() {
        isVisible = true
        position.apply {
            xPosition = BlazeObjectXPosition.START_TO_START
            yPosition = BlazeObjectYPosition.BOTTOM_TO_BOTTOM
        }
        val customTextColor = "#FFCE66".toColorInt()
        val customFontResId = R.font.fira_sans_extra_condensed_medium_italic
        readState.apply {
            fontResId = customFontResId
            textColor = customTextColor
            textSize = 17f
            maxLines = 1
        }
        unreadState.apply {
            fontResId = customFontResId
            textColor = customTextColor
            textSize = 17f
            maxLines = 1
        }
        margins.apply {
            bottom = 12.blazeDp
        }
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-badge-style
    private fun BlazeWidgetItemBadgeStyle.setMyCustomBadgeStyle() {
        isVisible = true
        position.xPosition = BlazeObjectXPosition.CENTER_TO_END
        position.yPosition = BlazeObjectYPosition.CENTER_TO_TOP
        val customBackgroundColor = "#FF3131".toColorInt()
        val customBorderColor = "#FF6161".toColorInt()
        val customBorderWidth = 2.blazeDp
        val customWidth = 24.blazeDp
        val customHeight = 24.blazeDp
        // Inorder to see the border we need to set the padding to the same value as the border width.
        padding.apply {
            top = customBorderWidth
            bottom = customBorderWidth
            start = customBorderWidth
            end = customBorderWidth
        }
        unreadState.apply {
            backgroundColor = customBackgroundColor
            width = customWidth
            height = customHeight
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
        readState.apply {
            backgroundColor = customBackgroundColor
            width = customWidth
            height = customHeight
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
        liveUnreadState.apply {
            backgroundColor = customBackgroundColor
            width = customWidth
            height = customHeight
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
        liveReadState.apply {
            backgroundColor = customBackgroundColor
            width = customWidth
            height = customHeight
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
    }

    // Example of setting custom styles for a specific widget item by it team ID
    // We get the mapping key and value from the BE, inside the item object entities field.
    // For more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-custom-mapping#/
    private fun setOverrideStylesByTeamId(widgetLayout: BlazeWidgetLayout) {
        val layoutDeepCopy = widgetLayout.blazeDeepCopy()
        val mappingKey =  BlazeWidgetItemCustomMapping.BlazeKeysPresets.TEAM_ID
        val mappingValue = "1610612755"
        binding.videosRowWidgetView.updateOverrideStyles(
            perItemStyleOverrides = mapOf(
                BlazeWidgetItemCustomMapping(mappingKey, mappingValue) to getBlazeWidgetItemStyleOverrides(layoutDeepCopy)
            ),
            shouldUpdateUi = true
        )
    }

    // For more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-style-overrides#/
    private fun getBlazeWidgetItemStyleOverrides(newWidgetLayout: BlazeWidgetLayout) =
        BlazeWidgetItemStyleOverrides(
            imageBorder = newWidgetLayout.widgetItemStyle.image.border.apply {
                isVisible = true
                val borderColor = "#67FFF5".toColorInt()
                val borderWidth = 1.blazeDp
                liveUnreadState.apply {
                    width = borderWidth
                    color = borderColor
                    margin = 0.blazeDp
                }
                liveReadState.apply {
                    width = borderWidth
                    color = borderColor
                    margin = 0.blazeDp
                }
                unreadState.apply {
                    width = borderWidth
                    color = borderColor
                    margin = 0.blazeDp
                }
                readState.apply {
                    width = borderWidth
                    color = borderColor
                    margin = 0.blazeDp
                }
            },
            badge = newWidgetLayout.widgetItemStyle.badge.apply {
                isVisible = true
                position.xPosition = BlazeObjectXPosition.CENTER_TO_END
                position.yPosition = BlazeObjectYPosition.CENTER_TO_TOP
                val customBackgroundColor = "#CAFFFA".toColorInt()
                val customBorderColor = "#2FB2A5".toColorInt()
                val customBorderWidth = 2.blazeDp
                val customWidth = 24.blazeDp
                val customHeight = 24.blazeDp
                // Inorder to see the border we need to set the padding to the same value as the border width.
                padding.apply {
                    top = customBorderWidth
                    bottom = customBorderWidth
                    start = customBorderWidth
                    end = customBorderWidth
                }
                unreadState.apply {
                    backgroundColor = customBackgroundColor
                    width = customWidth
                    height = customHeight
                    borderColor = customBorderColor
                    borderWidth = customBorderWidth
                }
                readState.apply {
                    backgroundColor = customBackgroundColor
                    width = customWidth
                    height = customHeight
                    borderColor = customBorderColor
                    borderWidth = customBorderWidth
                }
                liveUnreadState.apply {
                    backgroundColor = customBackgroundColor
                    width = customWidth
                    height = customHeight
                    borderColor = customBorderColor
                    borderWidth = customBorderWidth
                }
                liveReadState.apply {
                    backgroundColor = customBackgroundColor
                    width = customWidth
                    height = customHeight
                    borderColor = customBorderColor
                    borderWidth = customBorderWidth
                }

            },
            statusIndicator = newWidgetLayout.widgetItemStyle.statusIndicator.apply {
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
                unreadState.apply {
                    backgroundColor = "#CAFFFA".toColorInt()
                    text = "NEW"
                    textStyle.apply {
                        textSize = 12f
                        textColor = "#2FB2A5".toColorInt()
                    }
                    cornerRadius = 8.blazeDp
                }
            }
        )
}