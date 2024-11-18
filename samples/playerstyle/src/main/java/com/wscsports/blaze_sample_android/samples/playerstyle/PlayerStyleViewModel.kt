package com.wscsports.blaze_sample_android.samples.playerstyle

import android.graphics.Color
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.style.players.BlazePlayerButtonCustomImageStates
import com.blaze.blazesdk.style.players.BlazePlayerDisplayMode
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerBodyTextStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerCtaIconStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerCtaStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerFooterGradientStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerHeadingTextStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerStyle
import com.blaze.blazesdk.style.shared.models.blazeDp
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.android.blaze.blaze_sample_android.core.ui.R.drawable

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

    val customStoryPlayerStyle = defaultStoryPlayerStyle.apply {
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
        buttons.apply {
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
        }
        /** Player chips appearance */
        chips.apply {
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
        firstTimeSlide.apply {
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
            }
        }
    }
    
    val customMomentPlayerStyle = defaultMomentPlayerStyle.apply {
        backgroundColor = Color.BLACK

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
        buttons.apply {
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
        }
        /** Player chips appearance */
        chips.apply {
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
        /** Cta button appearance */
        cta.apply {
            cornerRadius = 2.blazeDp
            textSize = 16f
            width = null
            height = 48.blazeDp
            fontResId = null
            icon?.apply {
                iconResId = drawable.ic_play
                iconPositioning = BlazeMomentsPlayerCtaIconStyle.BlazeIconPositioning.START
                iconTint = null
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
            startColor = Color.BLACK
            endColor = Color.TRANSPARENT
        }
        /** Player header gradient appearance */
        footerGradient.apply {
            isVisible = true
            startColor = Color.TRANSPARENT
            endColor = Color.BLACK
            endPositioning = BlazeMomentsPlayerFooterGradientStyle.BlazeEndPositioning.BOTTOM_TO_PLAYER
        }
        /** Player seek bar appearance */
        seekBar.apply {
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
        /** First time slide appearance */
        firstTimeSlide.apply {
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
            }
        }
    }

    companion object {
        private const val HEADER_TEXT_SIZE = 22f
        private const val DESCRIPTION_TEXT_SIZE = 16f

        private val seekBarUnplayedSegmentColor = Color.parseColor("#33FFFFFF")
        private val seekBarPlayedSegmentColor = Color.parseColor("#FFFFFF")

        private val liveChipBgColor = Color.parseColor("#FF364E")
        private val adChipBgColor = Color.parseColor("#FFAE00")
    }

}