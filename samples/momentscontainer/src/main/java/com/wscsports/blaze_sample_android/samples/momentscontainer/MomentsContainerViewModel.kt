package com.wscsports.blaze_sample_android.samples.momentscontainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerCtaStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.blaze.blazesdk.style.shared.models.blazeDp
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * This ViewModel is used to demonstrate how to use BlazeMomentsPlayerContainer.
 * It contains two BlazeMomentsPlayerStyle instances.
 * For more information and player container customizations, see https://dev.wsc-sports.com/docs/android-moments-player-customizations#/.
 */
class MomentsContainerViewModel: ViewModel() {

    private val _onVolumeChangedEvent = MutableSharedFlow<Unit>()
    val onVolumeChangedEvent = _onVolumeChangedEvent.asSharedFlow()

    val lazyMomentsPlayerStyle: BlazeMomentsPlayerStyle
        get() = BlazeMomentsPlayerStyle.base().apply {
            // buttons customization
            buttons.mute.isVisible = true // true by default
            buttons.exit.isVisible = true // true by default
            // Seek bar customization
            seekBar.playingState.cornerRadius = 0.blazeDp
            seekBar.pausedState.cornerRadius = 0.blazeDp
            seekBar.pausedState.isThumbVisible = false
            seekBar.bottomMargin = 0.blazeDp
            seekBar.horizontalMargin = 0.blazeDp
            // cta customization
            cta.layoutPositioning = BlazeMomentsPlayerCtaStyle.BlazeCTAPositioning.CTA_NEXT_TO_BOTTOM_BUTTONS_BOX
        }

    // In the instant moments player, we hide the exit button since we want to keep the player open
    val instantMomentsPlayerStyle: BlazeMomentsPlayerStyle
        get() = BlazeMomentsPlayerStyle.base().apply {
            // buttons customization
            buttons.exit.isVisible = false
        }


    fun setOnVolumeChangedEvent() {
        viewModelScope.launch {
            _onVolumeChangedEvent.emit(Unit)
        }
    }

}