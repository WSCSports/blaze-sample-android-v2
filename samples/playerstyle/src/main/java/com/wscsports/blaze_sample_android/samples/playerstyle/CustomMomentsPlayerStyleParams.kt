package com.wscsports.blaze_sample_android.samples.playerstyle

import android.graphics.Color
import android.widget.ImageView
import androidx.core.graphics.toColorInt
import com.blaze.blazesdk.follow.models.BlazeFollowEntityType
import com.blaze.blazesdk.style.players.BlazeFirstTimeSlideInstructionStyle
import com.blaze.blazesdk.style.players.BlazeFirstTimeSlideTextStyle
import com.blaze.blazesdk.style.players.BlazePlayerButtonCustomImageStates
import com.blaze.blazesdk.style.players.BlazePlayerDisplayMode
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerBodyTextStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerButtonsStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerChipsStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerCtaIconStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerCtaStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerCustomActionButton
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerFirstTimeSlideStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerFollowEntityChipStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerFollowEntityStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerFooterGradientStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerHeadingTextStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerSeekBarStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.blaze.blazesdk.style.shared.models.BlazePlayerCustomActionButtonParams
import com.blaze.blazesdk.style.shared.models.blazeDp
import com.wscsports.blaze_sample_android.core.ui.R.color
import com.wscsports.blaze_sample_android.core.ui.R.drawable
import com.wscsports.blaze_sample_android.samples.playerstyle.PlayerStyleViewModel.Companion.DESCRIPTION_TEXT_SIZE
import com.wscsports.blaze_sample_android.samples.playerstyle.PlayerStyleViewModel.Companion.HEADER_TEXT_SIZE
import com.wscsports.blaze_sample_android.samples.playerstyle.PlayerStyleViewModel.Companion.seekBarPlayedSegmentColor
import com.wscsports.blaze_sample_android.samples.playerstyle.PlayerStyleViewModel.Companion.seekBarUnplayedSegmentColor

fun BlazeMomentsPlayerStyle.applyCustomMomentsPlayerParams(): BlazeMomentsPlayerStyle {
    return this.apply {
        val bgColor = "#181820".toColorInt()
        backgroundColor = bgColor

        /** Title appearance */
        headingText.apply {
            textColor = Color.WHITE
            textSize = 16f
            fontResId = null
            contentSource = BlazeMomentsPlayerHeadingTextStyle.BlazeContentSource.TITLE
            isVisible = false
        }

        /** description appearance */
        bodyText.apply {
            textColor = Color.WHITE
            textSize = 16f
            fontResId = null
            contentSource = BlazeMomentsPlayerBodyTextStyle.BlazeContentSource.TITLE
            isVisible = true
        }

        /** Player buttons appearance */
        buttons.applyButtonsStyle()

        /** Player chips appearance */
        chips.applyChipsStyle()

        /** Cta button appearance */
        cta.apply {
            cornerRadius = 50.blazeDp
            textSize = 16f
            width = 112.blazeDp
            height = 36.blazeDp
            fontResId = null
            icon?.apply {
                iconResId = R.drawable.ic_play_cta
                iconPositioning = BlazeMomentsPlayerCtaIconStyle.BlazeIconPositioning.START
                iconTint = Color.WHITE
            }
            layoutPositioning = BlazeMomentsPlayerCtaStyle.BlazeCTAPositioning.CTA_NEXT_TO_BOTTOM_BUTTONS_BOX
            horizontalAlignment = BlazeMomentsPlayerCtaStyle.BlazeCTAHorizontalAlignment.START
        }

        playerDisplayMode = BlazePlayerDisplayMode.FIXED_RATIO_9_16

        /** Bottom Components Alignment */
        bottomComponentsAlignment = BlazeMomentsPlayerStyle.BlazeBottomComponentsAlignment.RELATIVE_TO_CONTAINER

        /** Player header gradient appearance */
        headerGradient.apply {
            isVisible = true
            startColor = bgColor
            endColor = Color.TRANSPARENT
        }
        /** Player header gradient appearance */
        footerGradient.apply {
            isVisible = true
            startColor = Color.TRANSPARENT
            endColor = bgColor
            endPositioning = BlazeMomentsPlayerFooterGradientStyle.BlazeEndPositioning.BOTTOM_TO_PLAYER
        }

        /** Player seek bar appearance */
        seekBar.applySeekBarStyle()

        /** First time slide appearance */
        firstTimeSlide.applyFirstTimeSlideStyle()

        followEntity.applyFollowEntityStyle()
    }
}

fun BlazeMomentsPlayerButtonsStyle.applyButtonsStyle(): BlazeMomentsPlayerButtonsStyle {
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
        /** Like button appearance */
        like.apply {
            isVisible = true
            color = Color.WHITE
            width = 48.blazeDp
            height = 48.blazeDp
            scaleType = ImageView.ScaleType.CENTER
            isVisibleForAds = false
            /** Button images state appearance */
            customImage = BlazePlayerButtonCustomImageStates(
                imageUnselectedPathResId = drawable.ic_like_unselected,
                imageSelectedPathResId = drawable.ic_like_selected
            )
        }
        /** Like button appearance */
        play.apply {
            isVisible = true
            color = Color.WHITE
            width = 48.blazeDp
            height = 48.blazeDp
            scaleType = ImageView.ScaleType.CENTER
            isVisibleForAds = false
            /** Button images state appearance */
            customImage = BlazePlayerButtonCustomImageStates(
                imageUnselectedPathResId = drawable.ic_play,
                imageSelectedPathResId = null
            )
        }
        /** Captions button appearance */
        captions.apply {
            isVisible = true
        }

        val custom1 = BlazeMomentsPlayerCustomActionButton(
            customParams = BlazePlayerCustomActionButtonParams(
                id = "custom1",
                name = "My custom 1",
                appMetadata = emptyMap()
            )
        ).apply {
            style.customImage = BlazePlayerButtonCustomImageStates(
                imageSelectedPathResId = null,
                imageUnselectedPathResId = R.drawable.ic_custom_player_button
            )
        }
        /** Custom buttons declaration */
        setBottomStackCustomActionButtons(listOf(custom1))
        /** Story bottom stack button ordering */
        setBottomStackOrder(
            listOf(
                BlazeMomentsPlayerButtonsStyle.BottomStackButtons.Captions,
                BlazeMomentsPlayerButtonsStyle.BottomStackButtons.Share,
                BlazeMomentsPlayerButtonsStyle.BottomStackButtons.Like,
                BlazeMomentsPlayerButtonsStyle.BottomStackButtons.Custom(custom1.customParams.id)
            )
        )
    }
}

fun BlazeMomentsPlayerChipsStyle.applyChipsStyle(): BlazeMomentsPlayerChipsStyle {
    return this.apply {
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
            backgroundColor = Color.YELLOW
        }
    }
}

fun BlazeMomentsPlayerSeekBarStyle.applySeekBarStyle(): BlazeMomentsPlayerSeekBarStyle {
    return this.apply {
        isVisible = true
        bottomMargin = 0.blazeDp
        horizontalMargin = 0.blazeDp
        playingState.apply {
            isVisible = false
            backgroundColor = seekBarUnplayedSegmentColor
            progressColor = seekBarPlayedSegmentColor
            thumbColor = seekBarPlayedSegmentColor
            thumbImageResId = null
            cornerRadius = 2.blazeDp
            isThumbVisible = false
            height = 4.blazeDp
            thumbSize = 10.blazeDp
        }
        pausedState.apply {
            isVisible = true
            backgroundColor = seekBarUnplayedSegmentColor
            progressColor = seekBarPlayedSegmentColor
            thumbColor = seekBarPlayedSegmentColor
            thumbImageResId = null
            cornerRadius = 4.blazeDp
            isThumbVisible = true
            height = 8.blazeDp
            thumbSize = 14.blazeDp
        }
    }
}

fun BlazeMomentsPlayerFirstTimeSlideStyle.applyFirstTimeSlideStyle(): BlazeMomentsPlayerFirstTimeSlideStyle {
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
        mainTitle .apply {
            text = "Navigating Moments"
            textColorResId = R.color.first_time_slide_sub_title_color
            fontResId = null
        }
        /** Sub title appearance */
        subtitle.apply {
            text = "Browse moments content using these gestures"
            textColorResId = R.color.first_time_slide_sub_title_color
            fontResId = null
        }
        /** Instructions appearance */
        instructions.apply {
            /** Next instruction appearance */
            next.apply {
                headerText.apply {
                    text = "Go to the next video"
                    textColorResId = R.color.first_time_slide_header_color
                    fontResId = null
                    textSize = HEADER_TEXT_SIZE
                }
                descriptionText.apply {
                    text = "Swipe up"
                    textColorResId = R.color.first_time_slide_description_color
                    fontResId = null
                    textSize = DESCRIPTION_TEXT_SIZE
                }
            }
            /** Prev instruction appearance */
            previous.apply {
                headerText.apply {
                    text = "Go to the previous video"
                    textColorResId = R.color.first_time_slide_header_color
                    fontResId = null
                    textSize = HEADER_TEXT_SIZE
                }
                descriptionText.apply {
                    text = "Swipe down"
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
                    text = "Tap on screen"
                    textColorResId = R.color.first_time_slide_description_color
                    fontResId = null
                    textSize = DESCRIPTION_TEXT_SIZE
                }
            }
            /** Play instruction appearance */
            play.apply {
                headerText.apply {
                    text = "Play"
                    textColorResId = R.color.first_time_slide_header_color
                    fontResId = null
                    textSize = HEADER_TEXT_SIZE
                }
                descriptionText.apply {
                    text = "Tap on screen"
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

fun BlazeMomentsPlayerFollowEntityStyle.applyFollowEntityStyle(): BlazeMomentsPlayerFollowEntityStyle {
    return this.apply {
        isVisible = true

        // First, try to find the player entity, then the team, and finally retrieve the first available entity.
        entityType = BlazeFollowEntityType.Player(
            fallbackType = BlazeFollowEntityType.Team(
                fallbackType = BlazeFollowEntityType.FirstAvailable
            )
        )

        followState.apply {
            avatar.borderColor = "#00B27C".toColorInt()
            chip.backgroundColor = "#00B27C".toColorInt()
            chip.iconColor = "#000000".toColorInt()
        }

        unfollowState.apply {
            avatar.borderColor = "#FFFFFF".toColorInt()
            chip.backgroundColor = "#FFFFFF".toColorInt()
            chip.iconColor = "#000000".toColorInt()
            chip.contentSource = BlazeMomentsPlayerFollowEntityChipStyle.BlazeMomentsPlayerFollowEntityChipContentSource.TEXT
        }
    }
}