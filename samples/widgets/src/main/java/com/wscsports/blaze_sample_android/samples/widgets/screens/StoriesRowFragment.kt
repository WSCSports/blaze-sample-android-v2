package com.wscsports.blaze_sample_android.samples.widgets.screens

import android.graphics.Color
import android.util.TypedValue
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
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemStatusIndicatorStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemStyleOverrides
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemTitleStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.samples.widgets.R
import com.wscsports.blaze_sample_android.samples.widgets.WidgetScreenType
import com.wscsports.blaze_sample_android.samples.widgets.databinding.FragmentStoriesRowBinding
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetDataState
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetLayoutStyleState
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import androidx.core.graphics.toColorInt

/**
 * StoriesRowFragment is a Fragment that displays a row of stories.
 * It manages widget initialization, style customization, and data source updates.
 * For more information on [BlazeStoriesWidgetRowView], see https://dev.wsc-sports.com/docs/android-widgets#/stories-row
 */
class StoriesRowFragment: BaseWidgetFragment(R.layout.fragment_stories_row) {

    private val binding by viewBinding(FragmentStoriesRowBinding::bind)
    override val widgetType = WidgetScreenType.STORIES_ROW

    override fun initWidgetView() {
        // The custom layout can also be set during initWidget, rather than using updateWidgetLayout.
        // In this case, we are setting the layout to some default preset.
        val widgetLayout = viewModel.getWidgetLayoutBasePreset()
        val dataState = viewModel.getCurrWidgetDataState()
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(dataState.labelName),
            orderType = dataState.orderType,
        )
        binding.storiesRowWidgetView.initWidget(
            widgetLayout = widgetLayout,
            dataSource = dataSource,
            widgetId = widgetType.name, // Or any unique identifier for the widget
            widgetDelegate = this,
            shouldOrderWidgetByReadStatus = true,
        )
    }

    override fun onNewWidgetLayoutState(styleState: WidgetLayoutStyleState) {
        updateWidgetHeightParams(styleState)
        val newWidgetLayout = if (styleState.isCustomAppearance) {
            // example for setting different preset
            BlazeWidgetLayout.Presets.StoriesWidget.Row.verticalRectangles
        } else {
            viewModel.getWidgetLayoutBasePreset()
        }
        newWidgetLayout.apply {
            horizontalItemsSpacing = 8.blazeDp
            if (styleState.isCustomAppearance) widgetItemStyle.image.setMyCustomImageStyle()
            if (styleState.isCustomStatusIndicator) widgetItemStyle.statusIndicator.setMyCustomStatusIndicatorStyle()
            if (styleState.isCustomTitle) widgetItemStyle.title.setMyCustomTitleStyle()
            if (styleState.isCustomBadge) widgetItemStyle.badge.setMyCustomBadgeStyle()
        }
        binding.storiesRowWidgetView.updateWidgetLayout(newWidgetLayout)
        if (styleState.isCustomItemStyleOverrides) {
            setOverrideStylesByGameId(newWidgetLayout)
        } else {
            binding.storiesRowWidgetView.resetOverriddenStyles()
        }
    }

    override fun onNewDatasourceState(dataState: WidgetDataState) {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(dataState.labelName),
            orderType = dataState.orderType,
        )
        binding.storiesRowWidgetView.updateDataSource(dataSource, false)
    }

    // For rectangle preset, we give the widget a height of 180dp, and for circle preset, we give it a height of 160dp.
    private fun StoriesRowFragment.updateWidgetHeightParams(styleState: WidgetLayoutStyleState) {
        binding.storiesRowWidgetView.layoutParams.height = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            if (styleState.isCustomAppearance) 180f else 160f,
            resources.displayMetrics
        ).toInt()
    }


    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-image-style
    private fun BlazeWidgetItemImageStyle.setMyCustomImageStyle() {
        width = 120.blazeDp
        height = 168.blazeDp
        position = BlazeImagePosition.TopCenter
        cornerRadius = 6.blazeDp
        cornerRadiusRatio = null
        border.apply {
            isVisible = true
            val borderColor = "#282828".toColorInt()
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
        margins.apply {
            top = 0.blazeDp
            bottom = 0.blazeDp
            start = 0.blazeDp
            end = 0.blazeDp
        }
        gradientOverlay.apply {
            isVisible = true
            startColor = "#CC000000".toColorInt()
            endColor = Color.TRANSPARENT
            position = BlazeGradientPosition.BOTTOM
        }
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-status-indicator-style
    private fun BlazeWidgetItemStatusIndicatorStyle.setMyCustomStatusIndicatorStyle() {
        isVisible = true
        position.xPosition = BlazeObjectXPosition.CENTER_X
        position.yPosition = BlazeObjectYPosition.TOP_TO_TOP
        val customText = "94:85"
        val customBackgroundColor = "#00B27C".toColorInt()
        val customBorderColor = "#CFFFC2".toColorInt()
        // Set cornerRadiusRatio to null when using cornerRadius,
        // cornerRadiusRatio overrides cornerRadius if both are set.
        val customBorderWidth = 1.blazeDp
        val customCornerRadius = 20.blazeDp
        val customTextSize = 11f
        margins.apply {
            top = 0.blazeDp
            bottom = 0.blazeDp
            start = 0.blazeDp
            end = 0.blazeDp
        }
        padding.apply {
            start = 12.blazeDp
            end = 12.blazeDp
        }
        liveUnreadState.apply {
            isVisible = true
            backgroundColor = customBackgroundColor
            text = customText
            textStyle.textSize = customTextSize
            cornerRadius = customCornerRadius
            cornerRadiusRatio = null
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
        liveReadState.apply {
            isVisible = true
            backgroundColor = customBackgroundColor
            text = customText
            textStyle.textSize = customTextSize
            cornerRadius = customCornerRadius
            cornerRadiusRatio = null
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
        unreadState.apply {
            isVisible = true
            backgroundColor = customBackgroundColor
            text = customText
            textStyle.textSize = customTextSize
            cornerRadius = customCornerRadius
            cornerRadiusRatio = null
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
        readState.apply {
            isVisible = true
            backgroundColor = customBackgroundColor
            text = customText
            textStyle.textSize = customTextSize
            cornerRadius = customCornerRadius
            cornerRadiusRatio = null
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-title-style
    private fun BlazeWidgetItemTitleStyle.setMyCustomTitleStyle() {
        isVisible = true
        val customTextColor = "#A7C7FF".toColorInt()
        val customFontResId = R.font.fira_sans_extra_condensed_medium_italic
        readState.apply {
            fontResId = customFontResId
            textColor = customTextColor
            textSize = 14f
            maxLines = 2
        }
        unreadState.apply {
            fontResId = customFontResId
            textColor = customTextColor
            textSize = 14f
            maxLines = 2
        }
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-badge-style
    private fun BlazeWidgetItemBadgeStyle.setMyCustomBadgeStyle() {
        isVisible = true
        position.xPosition = BlazeObjectXPosition.END_TO_END
        position.yPosition = BlazeObjectYPosition.TOP_TO_TOP
        val customImageResId = R.drawable.image_flag_us
        val customBorderColor = Color.WHITE
        val customBorderWidth = 1.blazeDp
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
            backgroundImageResId = customImageResId
            width = customWidth
            height = customHeight
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
        readState.apply {
            backgroundImageResId = customImageResId
            width = customWidth
            height = customHeight
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
        liveUnreadState.apply {
            backgroundImageResId = customImageResId
            width = customWidth
            height = customHeight
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
        liveReadState.apply {
            backgroundImageResId = customImageResId
            width = customWidth
            height = customHeight
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
    }

    // Example of setting custom styles for a specific widget item by it game ID.
    // We get the mapping key and value from the BE, inside the item object entities field.
    // For more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-custom-mapping#/
    private fun setOverrideStylesByGameId(widgetLayout: BlazeWidgetLayout) {
        val layoutDeepCopy = widgetLayout.blazeDeepCopy() // we create a deep copy of the layout to avoid modifying the original layout
        val mappingKey =  BlazeWidgetItemCustomMapping.BlazeKeysPresets.GAME_ID
        val mappingValue = "0022300858"
        binding.storiesRowWidgetView.updateOverrideStyles(
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
                val borderColor = "#8E1616".toColorInt()
                unreadState.color = borderColor
                readState.color = borderColor
                liveUnreadState.color = borderColor
                liveReadState.color = borderColor
            },
            badge = newWidgetLayout.widgetItemStyle.badge.apply {
                isVisible = true
                position.apply {
                    xPosition = BlazeObjectXPosition.END_TO_END
                    yPosition = BlazeObjectYPosition.TOP_TO_TOP
                }
                val badgeImageResId = R.drawable.image_flag_es
                val badgeBorderColor = Color.WHITE
                val badgeBorderWidth = 1.blazeDp
                val badgeWidth = 24.blazeDp
                val badgeHeight = 24.blazeDp
                val sidePadding = 1.blazeDp
                padding.apply {
                    top = sidePadding
                    bottom = sidePadding
                    start = sidePadding
                    end = sidePadding
                }
                unreadState.apply {
                    width = badgeWidth
                    height = badgeHeight
                    backgroundImageResId = badgeImageResId
                    borderColor = badgeBorderColor
                    borderWidth = badgeBorderWidth
                }
                readState.apply {
                    width = badgeWidth
                    height = badgeHeight
                    backgroundImageResId = badgeImageResId
                    borderColor = badgeBorderColor
                    borderWidth = badgeBorderWidth
                }
                liveUnreadState.apply {
                    width = badgeWidth
                    height = badgeHeight
                    backgroundImageResId = badgeImageResId
                    borderColor = badgeBorderColor
                    borderWidth = badgeBorderWidth
                }
                liveReadState.apply {
                    width = badgeWidth
                    height = badgeHeight
                    backgroundImageResId = badgeImageResId
                    borderColor = badgeBorderColor
                    borderWidth = badgeBorderWidth
                }
            },
            statusIndicator = newWidgetLayout.widgetItemStyle.statusIndicator.apply {
                val statusBackgroundColor = "#8E1616".toColorInt()
                val statusBorderColor = "#FF6161".toColorInt()
                // Set cornerRadiusRatio to null when using cornerRadius,
                // cornerRadiusRatio overrides cornerRadius if both are set.
                val statusCornerRadius = 4.blazeDp
                val statusBorderWidth = 1.blazeDp
                position.apply {
                    xPosition = BlazeObjectXPosition.START_TO_START
                    yPosition = BlazeObjectYPosition.CENTER_TO_BOTTOM
                }
                margins.apply {
                    top = 0.blazeDp
                    bottom = 0.blazeDp
                    start = 0.blazeDp
                    end = 0.blazeDp
                }
                padding.apply {
                    start = 12.blazeDp
                    end = 12.blazeDp
                }
                liveReadState.apply {
                    backgroundColor = statusBackgroundColor
                    text = "Breaking"
                    cornerRadius = statusCornerRadius
                    cornerRadiusRatio = null
                    borderColor = statusBorderColor
                    borderWidth = statusBorderWidth
                }
                liveUnreadState.apply {
                    backgroundColor = statusBackgroundColor
                    text = "Breaking"
                    cornerRadiusRatio = 0f
                    cornerRadius = statusCornerRadius
                    cornerRadiusRatio = null
                    borderColor = statusBorderColor
                    borderWidth = statusBorderWidth
                }
                readState.apply {
                    backgroundColor = statusBackgroundColor
                    text = "Breaking"
                    cornerRadius = statusCornerRadius
                    cornerRadiusRatio = null
                    borderColor = statusBorderColor
                    borderWidth = statusBorderWidth
                }
                unreadState.apply {
                    backgroundColor = statusBackgroundColor
                    text = "Breaking"
                    cornerRadius = statusCornerRadius
                    cornerRadiusRatio = null
                    borderColor = statusBorderColor
                    borderWidth = statusBorderWidth
                }
            }
        )
}

