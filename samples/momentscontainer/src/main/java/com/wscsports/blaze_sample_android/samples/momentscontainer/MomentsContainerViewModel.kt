package com.wscsports.blaze_sample_android.samples.momentscontainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MomentsContainerViewModel: ViewModel() {

    private val _onVolumeChangedEvent = MutableSharedFlow<Unit>()
    val onVolumeChangedEvent = _onVolumeChangedEvent.asSharedFlow()

    fun setOnVolumeChangedEvent() {
        viewModelScope.launch {
            _onVolumeChangedEvent.emit(Unit)
        }
    }
}