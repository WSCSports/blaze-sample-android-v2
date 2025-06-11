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
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemImageStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemImageStyle.BlazeImagePosition
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemStatusIndicatorStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemStyleOverrides
import com.blaze.blazesdk.style.widgets.BlazeWidgetItemTitleStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.samples.widgets.R
import com.wscsports.blaze_sample_android.samples.widgets.WidgetScreenType
import com.wscsports.blaze_sample_android.samples.widgets.databinding.FragmentMomentsGridBinding
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetDataState
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetLayoutStyleState
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

/**
 * MomentsGridFragment is a Fragment that displays a grid of moments.
 * It manages widget initialization, style customization, and data source updates.
 * For more information on [BlazeMomentsWidgetGridView], see https://dev.wsc-sports.com/docs/android-widgets#/moments-grid
 */
class MomentsGridFragment : BaseWidgetFragment(R.layout.fragment_moments_grid) {

    private val binding by viewBinding(FragmentMomentsGridBinding::bind)
    override val widgetType = WidgetScreenType.MOMENTS_GRID

    override fun initWidgetView() {
        // The custom layout can also be set during initialization, rather than using updateWidgetLayout.
        // In this case, we are setting the layout to some default preset.
        val widgetLayout = viewModel.getWidgetLayoutBasePreset()
        val dataState = viewModel.getCurrWidgetDataState()
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(dataState.labelName),
            orderType = dataState.orderType,
        )
        binding.momentsGridWidgetView.initWidget(
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
        binding.momentsGridWidgetView.updateWidgetLayout(newWidgetLayout)
        if (styleState.isCustomItemStyleOverrides) {
            setOverrideStylesByPlayerId(newWidgetLayout)
        } else {
            binding.momentsGridWidgetView.resetOverriddenStyles()
        }
    }

    override fun onNewDatasourceState(dataState: WidgetDataState) {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(dataState.labelName),
            orderType = dataState.orderType,
        )
        binding.momentsGridWidgetView.updateDataSource(dataSource, false)
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-image-style
    private fun BlazeWidgetItemImageStyle.setMyCustomImageStyle() {
        position = BlazeImagePosition.TopCenter
        cornerRadius = 16.blazeDp
        cornerRadiusRatio = null
        border.apply {
            isVisible = true
            val borderColor = "#0057FF".toColorInt()
            val borderWidth = 4.blazeDp
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
        position.apply {
            xPosition = BlazeObjectXPosition.END_TO_END
            yPosition = BlazeObjectYPosition.BOTTOM_TO_BOTTOM
        }
        margins.apply {
            end = 8.blazeDp
            bottom = 8.blazeDp
        }
        padding.apply {
            start = 12.blazeDp
            end = 12.blazeDp
        }
        unreadState.apply {
            isVisible = true
            backgroundColor = "#E5FF00".toColorInt()
            text = "NEW"
            textStyle.apply {
                textSize = 12f
                textColor = "#3F3F2B".toColorInt()
            }
            cornerRadius = 8.blazeDp
        }
        readState.apply {
            isVisible = true
            backgroundColor = "#E5FF00".toColorInt()
            text = "NEW"
            textStyle.apply {
                textSize = 12f
                textColor = "#3F3F2B".toColorInt()
            }
            cornerRadius = 8.blazeDp
        }
    }

    private fun BlazeWidgetItemTitleStyle.setMyCustomTitleStyle() {
        isVisible = true
        position.apply {
            xPosition = BlazeObjectXPosition.START_TO_START
            yPosition = BlazeObjectYPosition.TOP_TO_BOTTOM
        }
        val customTextColor = "#000000".toColorInt()
        val customFontResId = R.font.fira_sans_extra_condensed_medium_italic
        readState.apply {
            fontResId = customFontResId
            textColor = customTextColor
            textSize = 14f
            maxLines = 1
        }
        unreadState.apply {
            fontResId = customFontResId
            textColor = customTextColor
            textSize = 14f
            maxLines = 1
        }
        margins.apply {
            top = 12.blazeDp
            end = 16.blazeDp
        }
    }

    // for more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-badge-style
    private fun BlazeWidgetItemBadgeStyle.setMyCustomBadgeStyle() {
        isVisible = true
        position.xPosition = BlazeObjectXPosition.CENTER_TO_END
        position.yPosition = BlazeObjectYPosition.CENTER_TO_TOP
        val customBadgeText = "3"
        val customBackgroundColor = "#FF3131".toColorInt()
        val customTextSize = 14f
        val customTextColor = Color.WHITE
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
            text = customBadgeText
            textStyle.apply {
                textSize = customTextSize
                textColor = customTextColor
            }
            backgroundColor = customBackgroundColor
            width = customWidth
            height = customHeight
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
        readState.apply {
            text = customBadgeText
            textStyle.apply {
                textSize = customTextSize
                textColor = customTextColor
            }
            backgroundColor = customBackgroundColor
            width = customWidth
            height = customHeight
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
        liveUnreadState.apply {
            text = customBadgeText
            textStyle.apply {
                textSize = customTextSize
                textColor = customTextColor
            }
            backgroundColor = customBackgroundColor
            width = customWidth
            height = customHeight
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
        liveReadState.apply {
            text = customBadgeText
            textStyle.apply {
                textSize = customTextSize
                textColor = customTextColor
            }
            backgroundColor = customBackgroundColor
            width = customWidth
            height = customHeight
            borderColor = customBorderColor
            borderWidth = customBorderWidth
        }
    }

    // Example of setting custom styles for a specific widget item by it player ID.
    // We get the mapping key and value from the BE, inside the item object entities field.
    // For more information see https://dev.wsc-sports.com/docs/android-blaze-widget-item-custom-mapping#/
    private fun setOverrideStylesByPlayerId(widgetLayout: BlazeWidgetLayout) {
        val layoutDeepCopy = widgetLayout.blazeDeepCopy()
        val mappingKey =  BlazeWidgetItemCustomMapping.BlazeKeysPresets.PLAYER_ID
        val mappingValue = "441303"
        binding.momentsGridWidgetView.updateOverrideStyles(
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
                val borderColor = "#2FB2A5".toColorInt()
                val borderWidth = 4.blazeDp
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
                val customBadgeText = "1"
                val customBackgroundColor = "#CAFFFA".toColorInt()
                val customTextSize = 14f
                val customTextColor = "#2FB2A5".toColorInt()
                val customBorderColor = "#2FB2A5".toColorInt()
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
                    text = customBadgeText
                    textStyle.apply {
                        textSize = customTextSize
                        textColor = customTextColor
                    }
                    backgroundColor = customBackgroundColor
                    width = customWidth
                    height = customHeight
                    borderColor = customBorderColor
                    borderWidth = customBorderWidth
                }
                readState.apply {
                    text = customBadgeText
                    textStyle.apply {
                        textSize = customTextSize
                        textColor = customTextColor
                    }
                    backgroundColor = customBackgroundColor
                    width = customWidth
                    height = customHeight
                    borderColor = customBorderColor
                    borderWidth = customBorderWidth
                }
                liveUnreadState.apply {
                    text = customBadgeText
                    textStyle.apply {
                        textSize = customTextSize
                        textColor = customTextColor
                    }
                    backgroundColor = customBackgroundColor
                    width = customWidth
                    height = customHeight
                    borderColor = customBorderColor
                    borderWidth = customBorderWidth
                }
                liveReadState.apply {
                    text = customBadgeText
                    textStyle.apply {
                        textSize = customTextSize
                        textColor = customTextColor
                    }
                    backgroundColor = customBackgroundColor
                    width = customWidth
                    height = customHeight
                    borderColor = customBorderColor
                    borderWidth = customBorderWidth
                }
            },
            statusIndicator = newWidgetLayout.widgetItemStyle.statusIndicator.apply {
                isVisible = true
                val statusBackgroundColor = "#2FB2A5".toColorInt()
                val statusBorderColor = "#FFFFFF".toColorInt()
                val statusTextColor = "#FFFFFF".toColorInt()
                // Set cornerRadiusRatio to null when using cornerRadius,
                // cornerRadiusRatio overrides cornerRadius if both are set.
                val statusCornerRadius = 4.blazeDp
                val statusBorderWidth = 1.blazeDp
                position.apply {
                    xPosition = BlazeObjectXPosition.END_TO_END
                    yPosition = BlazeObjectYPosition.BOTTOM_TO_BOTTOM
                }
                margins.apply {
                    end = 8.blazeDp
                    bottom = 8.blazeDp
                }
                padding.apply {
                    start = 12.blazeDp
                    end = 12.blazeDp
                }
                liveUnreadState.apply {
                    isVisible = true
                    backgroundColor = statusBackgroundColor
                    text = "New"
                    textStyle.textColor = statusTextColor
                    cornerRadius = statusCornerRadius
                    cornerRadiusRatio = null
                    borderColor = statusBorderColor
                    borderWidth = statusBorderWidth
                }
                unreadState.apply {
                    isVisible = true
                    backgroundColor = statusBackgroundColor
                    text = "New"
                    textStyle.textColor = statusTextColor
                    cornerRadius = statusCornerRadius
                    cornerRadiusRatio = null
                    borderColor = statusBorderColor
                    borderWidth = statusBorderWidth
                }
                readState.apply {
                    isVisible = true
                    backgroundColor = statusBackgroundColor
                    text = "New"
                    textStyle.textColor = statusTextColor
                    cornerRadius = statusCornerRadius
                    cornerRadiusRatio = null
                    borderColor = statusBorderColor
                    borderWidth = statusBorderWidth
                }
            }
        )
}