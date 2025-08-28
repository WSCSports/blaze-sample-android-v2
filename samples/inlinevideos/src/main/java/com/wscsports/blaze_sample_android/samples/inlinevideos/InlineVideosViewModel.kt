package com.wscsports.blaze_sample_android.samples.inlinevideos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * Shared ViewModel for inline videos volume management.
 * 
 * Handles volume change events and distributes them to all inline video showcases.
 * Used by both native and Compose implementations to ensure consistent volume
 * behavior across all inline players.
 * 
 * Volume events are triggered by volume key presses in the parent activity and
 * propagated to all active inline video players.
 */
class InlineVideosViewModel : ViewModel() {

    // SharedFlow for native views (fragment subscription pattern)
    private val _onVolumeChangedEvent = MutableSharedFlow<Unit>()
    val onVolumeChangedEvent = _onVolumeChangedEvent.asSharedFlow()
    
    // LiveData for Compose (PlayerVolumeEffect pattern)
    private val _volumeLiveData = MutableLiveData<Boolean>()
    val volumeLiveData: LiveData<Boolean> = _volumeLiveData

    /**
     * Notifies all inline video players of volume changes.
     * 
     * This method should be called when the user presses volume keys
     * to ensure all active players update their volume state accordingly.
     * 
     * Triggers both SharedFlow (for native views) and LiveData (for Compose).
     */
    fun setOnVolumeChangedEvent() {
        viewModelScope.launch {
            // Trigger SharedFlow for native fragments
            _onVolumeChangedEvent.emit(Unit)
            
            // Trigger LiveData for Compose (toggle current value to trigger observers)
            val currentValue = _volumeLiveData.value ?: true
            _volumeLiveData.value = !currentValue
        }
    }
}
