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

    private val _onMomentsTabSelectedEvent = MutableSharedFlow<Unit>()
    val onMomentsTabSelectedEvent = _onMomentsTabSelectedEvent.asSharedFlow()

    val momentsPlayerStyle: BlazeMomentsPlayerStyle
        get() = BlazeMomentsPlayerStyle.base().apply {
            // buttons customization
            buttons.apply {
                mute.isVisible = true // true by default
                exit.isVisible = false
            }
            // Seek bar customization
            seekBar.apply {
                playingState.cornerRadius = 8.blazeDp
                pausedState.cornerRadius = 8.blazeDp
                pausedState.isThumbVisible = true // true by default
                bottomMargin = 16.blazeDp
                horizontalMargin = 16.blazeDp
            }
            // cta customization
            cta.layoutPositioning = BlazeMomentsPlayerCtaStyle.BlazeCTAPositioning.CTA_NEXT_TO_BOTTOM_BUTTONS_BOX
        }

    fun setMomentsTabSelectedEvent() {
        viewModelScope.launch {
            _onMomentsTabSelectedEvent.emit(Unit)
        }
    }

    fun setOnVolumeChangedEvent() {
        viewModelScope.launch {
            _onVolumeChangedEvent.emit(Unit)
        }
    }

}