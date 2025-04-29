package com.wscsports.blaze_sample_android.samples.playerstyle

import android.graphics.Color
import android.widget.ImageView
import com.blaze.blazesdk.style.players.BlazeFirstTimeSlideInstructionStyle
import com.blaze.blazesdk.style.players.BlazeFirstTimeSlideTextStyle
import com.blaze.blazesdk.style.players.BlazePlayerButtonCustomImageStates
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerButtonsStyle
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerChipsStyle
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerCustomActionButton
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerFirstTimeSlideStyle
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerStyle
import com.blaze.blazesdk.style.shared.models.BlazePlayerCustomActionButtonParams
import com.blaze.blazesdk.style.shared.models.blazeDp
import com.wscsports.blaze_sample_android.core.ui.R.*
import com.wscsports.blaze_sample_android.samples.playerstyle.PlayerStyleViewModel.Companion.DESCRIPTION_TEXT_SIZE
import com.wscsports.blaze_sample_android.samples.playerstyle.PlayerStyleViewModel.Companion.HEADER_TEXT_SIZE
import com.wscsports.blaze_sample_android.samples.playerstyle.PlayerStyleViewModel.Companion.adChipBgColor
import com.wscsports.blaze_sample_android.samples.playerstyle.PlayerStyleViewModel.Companion.liveChipBgColor

fun BlazeStoryPlayerStyle.applyCustomStoryPlayerParams(): BlazeStoryPlayerStyle {
    return this.apply {
        backgroundColor = Color.BLACK

        /** Story title appearance */
        title.apply {
            textColor = Color.WHITE
            textSize = 12f
            fontResId = null
        }
        /** Story last update appearance */
        lastUpdate.apply {
            textColor = Color.WHITE
            textSize = 12f
            fontResId = null
        }

        /** Player buttons appearance */
        buttons.applyButtonsStyle()

        /** Player chips appearance */
        chips.applyChipsStyle()

        /** Cta button appearance */
        cta.apply {
            cornerRadius = 0.blazeDp
            textSize = 16f
            fontResId = null
        }
        /** Player header gradient appearance */
        headerGradient.apply {
            isVisible = true
            startColor = Color.BLACK
            endColor = Color.TRANSPARENT
        }
        /** Player progress bar appearance */
        progressBar.apply {
            backgroundColor = Color.GRAY
            progressColor = Color.WHITE
        }
        /** First time slide appearance */
        firstTimeSlide.applyFirstTimeSlideStyle()
    }
}

fun BlazeStoryPlayerButtonsStyle.applyButtonsStyle(): BlazeStoryPlayerButtonsStyle {
    return this.apply {
        /** Exit button appearance */
        exit.apply {
            isVisible = true
            color = Color.WHITE
            width = 48.blazeDp
            height = 48.blazeDp
            scaleType = ImageView.ScaleType.CENTER
            isVisibleForAds = false
            /** Button images state appearance */
            customImage = BlazePlayerButtonCustomImageStates(
                imageUnselectedPathResId = drawable.ic_rounded_close,
                imageSelectedPathResId = null
            )
        }
        /** Share button appearance */
        share.apply {
            isVisible = true
            color = Color.WHITE
            width = 48.blazeDp
            height = 48.blazeDp
            scaleType = ImageView.ScaleType.CENTER
            isVisibleForAds = true
            /** Button images state appearance */
            customImage = BlazePlayerButtonCustomImageStates(
                imageUnselectedPathResId = drawable.ic_share,
                imageSelectedPathResId = null
            )
        }
        /** Mute button appearance */
        mute.apply {
            isVisible = true
            color = Color.WHITE
            width = 48.blazeDp
            height = 48.blazeDp
            scaleType = ImageView.ScaleType.CENTER
            isVisibleForAds = true
            /** Button images state appearance */
            customImage = BlazePlayerButtonCustomImageStates(
                imageUnselectedPathResId = drawable.ic_sound_off,
                imageSelectedPathResId = drawable.ic_sound_on
            )
        }
        /** Captions button appearance */
        captions.apply {
            isVisible = true
        }

        /** Custom action button appearance */
        val custom1 =  BlazeStoryPlayerCustomActionButton(
            customParams = BlazePlayerCustomActionButtonParams(
                id = "custom1", // Must be unique ID for the custom action button
                name = "My custom 1",
                appMetadata = emptyMap()
            )
        ).apply {
            style.customImage = BlazePlayerButtonCustomImageStates(
                imageSelectedPathResId = null,
                imageUnselectedPathResId = com.blaze.blazesdk.R.drawable.blaze_drawable_ic_video_like_selected
            )
        }
        val custom2 =  BlazeStoryPlayerCustomActionButton(
            customParams = BlazePlayerCustomActionButtonParams(
                id = "custom2", // Must be unique ID for the custom action button
                name = "My custom 2",
                appMetadata = emptyMap()
            )
        ).apply {
            style.customImage = BlazePlayerButtonCustomImageStates(
                imageSelectedPathResId = null,
                imageUnselectedPathResId = com.blaze.blazesdk.R.drawable.blaze_drawable_ic_settings
            )
            style.isVisible = true
            style.isVisibleForAds = true
        }
        setTopStackCustomActionButtons(listOf(custom2, custom1))
        /** Story top stack button ordering */
        setTopStackOrder(listOf(
            BlazeStoryPlayerButtonsStyle.TopStackButtons.Custom(custom1.customParams.id),
            BlazeStoryPlayerButtonsStyle.TopStackButtons.Mute,
            BlazeStoryPlayerButtonsStyle.TopStackButtons.Custom(custom2.customParams.id),
            BlazeStoryPlayerButtonsStyle.TopStackButtons.Exit,
            BlazeStoryPlayerButtonsStyle.TopStackButtons.Captions,
        ))
    }
}

fun BlazeStoryPlayerChipsStyle.applyChipsStyle(): BlazeStoryPlayerChipsStyle {
    return this.apply {
        /** Live chip appearance */
        live.apply {
            padding.apply {
                start = 12.blazeDp
                top = 2.blazeDp
                end = 12.blazeDp
                bottom = 2.blazeDp
            }
            text = "LIVE"
            textColor = Color.WHITE
            backgroundColor = liveChipBgColor
        }
        /** Ad chip appearance */
        ad.apply {
            padding.apply {
                start = 12.blazeDp
                top = 2.blazeDp
                end = 12.blazeDp
                bottom = 2.blazeDp
            }
            text = "AD"
            textColor = Color.WHITE
            backgroundColor = adChipBgColor
        }
    }
}

fun BlazeStoryPlayerFirstTimeSlideStyle.applyFirstTimeSlideStyle(): BlazeStoryPlayerFirstTimeSlideStyle {
    return this.apply {
        show = true
        backgroundColorResId = R.color.first_time_slide_background_color
        /** Cta appearance */
        cta.apply {
            backgroundColor = R.color.first_time_slide_cta_button_color
            textColorResId = R.color.first_time_slide_cta_button_text_color
            cornerRadius = 8.blazeDp
            title = "Tap to start"
            fontResId = null
        }
        /** Main title appearance */
        mainTitle.apply {
            text = "Navigating Stories"
            textColorResId = R.color.first_time_slide_sub_title_color
            fontResId = null
        }
        /** Sub title appearance */
        subtitle.apply {
            text = "Browse story content using these gestures"
            textColorResId = R.color.first_time_slide_sub_title_color
            fontResId = null
        }
        /** Instructions appearance */
        instructions.apply {
            /** Forward instruction appearance */
            forward.apply {
                headerText.apply {
                    text = "Go forward"
                    textColorResId = R.color.first_time_slide_header_color
                    fontResId = null
                    textSize = HEADER_TEXT_SIZE
                }
                descriptionText.apply {
                    text = "Tap the screen"
                    textColorResId = R.color.first_time_slide_description_color
                    fontResId = null
                    textSize = DESCRIPTION_TEXT_SIZE
                }
            }
            /** Backward instruction appearance */
            backward.apply {
                headerText.apply {
                    text = "Go back"
                    textColorResId = R.color.first_time_slide_header_color
                    fontResId = null
                    textSize = HEADER_TEXT_SIZE
                }
                descriptionText.apply {
                    text = "Tap the left edge"
                    textColorResId = R.color.first_time_slide_description_color
                    fontResId = null
                    textSize = DESCRIPTION_TEXT_SIZE
                }
            }
            /** Pause instruction appearance */
            pause.apply {
                headerText.apply {
                    text = "Pause"
                    textColorResId = R.color.first_time_slide_header_color
                    fontResId = null
                    textSize = HEADER_TEXT_SIZE
                }
                descriptionText.apply {
                    text = "Press and hold the screen"
                    textColorResId = R.color.first_time_slide_description_color
                    fontResId = null
                    textSize = DESCRIPTION_TEXT_SIZE
                }
            }
            /** Transition instruction appearance */
            transition.apply {
                headerText.apply {
                    text = "Move between stories"
                    textColorResId = R.color.first_time_slide_header_color
                    fontResId = null
                    textSize = HEADER_TEXT_SIZE
                }
                descriptionText.apply {
                    text = "Swipe left or right"
                    textColorResId = R.color.first_time_slide_description_color
                    fontResId = null
                    textSize = DESCRIPTION_TEXT_SIZE
                }
            }
            customs = listOf(
                BlazeFirstTimeSlideInstructionStyle(
                    headerText = BlazeFirstTimeSlideTextStyle(
                        text = "Custom instruction 1",
                        textColorResId = color.black,
                        textSize = 22f
                    ),
                    descriptionText = BlazeFirstTimeSlideTextStyle(
                        text = "Custom instruction 1 description",
                        textColorResId = color.black,
                        textSize = 16f
                    ),
                    iconDrawableResId = drawable.ic_rounded_close,
                    isVisible = true
                ),
                BlazeFirstTimeSlideInstructionStyle(
                    headerText = BlazeFirstTimeSlideTextStyle(
                        text = "Custom instruction 2",
                        textColorResId = color.blue,
                        textSize = 22f
                    ),
                    descriptionText = BlazeFirstTimeSlideTextStyle(
                        text = "Custom instruction 2 description",
                        textColorResId = color.blue,
                        textSize = 16f
                    ),
                    iconDrawableResId = drawable.ic_like_selected,
                    isVisible = true
                ),
                BlazeFirstTimeSlideInstructionStyle(
                    headerText = BlazeFirstTimeSlideTextStyle(
                        text = "Custom instruction 3",
                        textColorResId = color.blueviolet,
                        textSize = 22f
                    ),
                    descriptionText = BlazeFirstTimeSlideTextStyle(
                        text = "Custom instruction 3 description",
                        textColorResId = color.blueviolet,
                        textSize = 16f
                    ),
                    iconDrawableResId = drawable.ic_back_button,
                    isVisible = true
                )
            )
        }
    }
}