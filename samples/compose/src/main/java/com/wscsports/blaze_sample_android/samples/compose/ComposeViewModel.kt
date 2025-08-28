package com.wscsports.blaze_sample_android.samples.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Compose screen.
 * Note: State Handlers should NOT be stored in ViewModels as they reference views and can cause memory leaks.
 * State Handlers should be created in the UI layer.
 */
class ComposeViewModel: ViewModel() {

    private val _onVolumeChangedEvent = MutableSharedFlow<Unit>()
    val onVolumeChangedEvent = _onVolumeChangedEvent.asSharedFlow()

    fun onVolumeChanged() {
        viewModelScope.launch {
            _onVolumeChangedEvent.emit(Unit)
        }
    }

}