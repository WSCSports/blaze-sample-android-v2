package com.wscsports.blaze_sample_android.samples.playerstyle

import android.graphics.Color
import android.widget.ImageView
import androidx.core.graphics.toColorInt
import com.blaze.blazesdk.style.players.BlazeCaptionsXPosition
import com.blaze.blazesdk.style.players.BlazeCaptionsYPosition
import com.blaze.blazesdk.style.players.BlazePlayerButtonCustomImageStates
import com.blaze.blazesdk.style.players.videos.BlazeVideosPlayerButtonsStyle
import com.blaze.blazesdk.style.players.videos.BlazeVideosPlayerCtaIconStyle
import com.blaze.blazesdk.style.players.videos.BlazeVideosPlayerSeekBarStyle
import com.blaze.blazesdk.style.players.videos.BlazeVideosPlayerStyle
import com.blaze.blazesdk.style.shared.models.blazeDp
import com.wscsports.blaze_sample_android.core.ui.R.drawable
import com.wscsports.blaze_sample_android.samples.playerstyle.PlayerStyleViewModel.Companion.seekBarPlayedSegmentColor
import com.wscsports.blaze_sample_android.samples.playerstyle.PlayerStyleViewModel.Companion.seekBarUnplayedSegmentColor

fun BlazeVideosPlayerStyle.applyCustomVideosPlayerParams(): BlazeVideosPlayerStyle {
    return this.apply {
        backgroundColor = "#181820".toColorInt()

        /** Player buttons appearance */
        buttons.applyCustomVideosButtonsStyle()

        /** Cta button appearance */
        cta.apply {
            cornerRadius = 20.blazeDp
            textSize = 16f
            width = 120.blazeDp
            height = 40.blazeDp
            fontResId = null
            icon?.apply {
                iconResId = R.drawable.ic_play_cta
                iconPositioning = BlazeVideosPlayerCtaIconStyle.BlazeIconPositioning.START
                iconTint = Color.WHITE
            }
        }

        /** Player seek bar appearance */
        seekBar.applyCustomVideosSeekBarStyle()

        /** Captions text appearance and positioning */
        captions.apply {
            textSize = 15f
            fontResId = null
            positioning.xPosition = BlazeCaptionsXPosition.Center
            positioning.yPosition = BlazeCaptionsYPosition.Bottom
        }

        /** Overlay "hide time" - how long the controls stay visible before auto-hiding */
        applyCustomOverlayVisibility()
    }
}

/**
 * Demonstrates customizing the videos player's overlay "hide time".
 *
 * [BlazeVideosPlayerStyle.overlayVisibilityThreshold] is how long (in milliseconds) the controls
 * overlay stays visible after being shown before it auto-hides. The SDK default is 3000ms
 * ([BlazeVideosPlayerStyle.DEFAULT_OVERLAY_VISIBILITY_THRESHOLD_MS]); non-positive values fall back
 * to that default. Here we keep the controls on screen longer (6s) than the default.
 */
fun BlazeVideosPlayerStyle.applyCustomOverlayVisibility(): BlazeVideosPlayerStyle {
    return this.apply {
        overlayVisibilityThreshold = 6000L
    }
}

fun BlazeVideosPlayerButtonsStyle.applyCustomVideosButtonsStyle(): BlazeVideosPlayerButtonsStyle {
    return this.apply {
        /** Exit button appearance */
        exit.apply {
            isVisible = true
            color = Color.WHITE
            width = 48.blazeDp
            height = 48.blazeDp
            scaleType = ImageView.ScaleType.CENTER
            isVisibleForAds = false
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
            customImage = BlazePlayerButtonCustomImageStates(
                imageUnselectedPathResId = drawable.ic_like_unselected,
                imageSelectedPathResId = drawable.ic_like_selected
            )
        }
        /** Play / pause button appearance */
        playPause.apply {
            isVisible = true
            color = Color.WHITE
            width = 48.blazeDp
            height = 48.blazeDp
            scaleType = ImageView.ScaleType.CENTER
            isVisibleForAds = false
            customImage = BlazePlayerButtonCustomImageStates(
                imageUnselectedPathResId = drawable.ic_play,
                imageSelectedPathResId = null
            )
        }
        /** Captions button appearance */
        captions.apply {
            isVisible = true
        }
    }
}

fun BlazeVideosPlayerSeekBarStyle.applyCustomVideosSeekBarStyle(): BlazeVideosPlayerSeekBarStyle {
    return this.apply {
        isVisible = true
        playingState.apply {
            isVisible = true
            backgroundColor = seekBarUnplayedSegmentColor
            progressColor = seekBarPlayedSegmentColor
            thumbColor = seekBarPlayedSegmentColor
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
            cornerRadius = 4.blazeDp
            isThumbVisible = true
            height = 8.blazeDp
            thumbSize = 14.blazeDp
        }
    }
}
