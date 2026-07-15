package com.wscsports.blaze_sample_android.samples.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeOrderType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.data.FollowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Builds the personalized "Your Picks" data source for the moments follow tabs
 * out of the locally persisted followed entities.
 */
class WidgetsFeedViewModel(
    followRepository: FollowRepository
) : ViewModel() {

    /**
     * "Your Picks" surfaces moments labeled with any of the followed entity ids or the
     * general highlights label. [BlazeDataSourceType.Labels.labelsPriority] ranks the
     * followed entities first (most recently followed on top), so personalized content
     * leads and general highlights fill in whenever it runs short.
     *
     * Deliberately a cold flow: the first emission must reflect the real persisted
     * state, as the screen initializes the widget on it.
     */
    val yourPicksDataSource: Flow<BlazeDataSourceType.Labels> =
        followRepository.followedEntityIds.map { followedEntityIds ->
            val labels = followedEntityIds + MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL
            BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.atLeastOneOf(*labels.toTypedArray()),
                labelsPriority = labels.map { BlazeWidgetLabel.singleLabel(it) },
                orderType = BlazeOrderType.RECENTLY_UPDATED_FIRST
            )
        }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                WidgetsFeedViewModel(FollowRepository.getInstance(application))
            }
        }
    }
}