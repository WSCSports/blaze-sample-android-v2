package com.wscsports.blaze_sample_android.samples.momentscontainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.delegates.models.BlazePlayerEvent
import com.blaze.blazesdk.shared.BlazeSDK
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerCtaStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.blaze.blazesdk.style.shared.models.blazeDp
import com.wscsports.blaze_sample_android.core.Constants
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * This ViewModel is used to demonstrate how to use BlazeMomentsPlayerContainer.
 * It contains two BlazeMomentsPlayerStyle instances and shows usage of the BlazeSDK.appendMomentsToPlayer() method.
 * For more information and player container customizations, see https://dev.wsc-sports.com/docs/android-moments-player-customizations#/.
 */
class MomentsContainerViewModel: ViewModel() {

    private var didAppendNewMoments = false

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

    fun handlePlayerEvent(event: BlazePlayerEvent, sourceId: String?) {
        if (shouldAppendMoreMoments(event)) {
            sourceId?.let {
                didAppendNewMoments = true
                appendNewMoments(it)
            }
        }
    }

    /**
     * Determines whether to append more moments to the player.
     * Appends content only one time when the user reaches above percentage th of the total content.
     */
    private fun shouldAppendMoreMoments(event: BlazePlayerEvent): Boolean {
        if (event !is BlazePlayerEvent.OnMomentStart) return false
        val momentIndex = event.params.momentIndex
        val totalCount = event.params.totalMomentsCount
        if (totalCount <= 0 || momentIndex < 0) return false
        return (momentIndex > (totalCount * APPEND_MOMENTS_TH_PERCENTAGE) && !didAppendNewMoments)
    }

    private fun appendNewMoments(sourceId: String) {
        BlazeSDK.appendMomentsToPlayer(
            sourceId = sourceId,
            dataSourceType = BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.singleLabel(Constants.MOMENTS_CONTAINER_TABS_2_DEFAULT_LABEL)
            ),
        )
    }

    companion object {
        const val APPEND_MOMENTS_TH_PERCENTAGE = 0.8
    }

}