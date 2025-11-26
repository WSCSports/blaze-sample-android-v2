package com.wscsports.blaze_sample_android.samples.entrypoint

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.shared.BlazeSDK
import com.blaze.blazesdk.shared.results.doOnFailure
import com.blaze.blazesdk.shared.results.doOnSuccess
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.STORIES_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.VIDEOS_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.ui.R.string
import com.wscsports.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.entrypoint.databinding.ActivityEntryPointBinding

/**
 * Entry point activity for the Blaze SDK.
 * This activity demonstrates how to handle universal links and push notifications,
 * and how to play stories and moments without widgets.
 */
class EntryPointActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityEntryPointBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding()
        setupAppbar()
        handleUniversalLinkIfNeeded()
        handlePushNotificationIfNeeded()
        setClickListeners()
        initLabelExpressionStringInputs()
        preparePlayers()
        setPlayerState()
    }

    private fun setupAppbar() {
        binding.appbar.setupView(getString(string.entry_point_title)) {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Process universal link data if the app was opened through a universal link.
     */
    private fun handleUniversalLinkIfNeeded() {
        intent?.data?.let { data ->
            if (intent.action == Intent.ACTION_VIEW) {
                handleUniversalLink(data.toString())
                // Clearing intent's data -> so universalLink handling won't be triggered again on Activity's recreation
                intent.data = null
            }
        }
    }

    /**
     * Process push notification data if the app was opened through a notification.
     * The data will be received via intent.extras under "WscIasData" key.
     * Refer to [SampleFirebaseMessagingService] for handling push notifications when the app is in the foreground.
     */
    private fun handlePushNotificationIfNeeded() {
        intent.getStringExtra(PUSH_INTENT_WSC_DATA_EXTRA_PARAM)?.let { data ->
            BlazeSDK.handleNotificationValue(data) {
                it.doOnSuccess {
                    Log.d("appDebug", "EntryPointActivity: handlePushNotificationIfNeeded: handleNotificationValue completed Successfully")
                }.doOnFailure { message, cause ->
                    Log.e("appDebug", "EntryPointActivity: handlePushNotificationIfNeeded: Unable to open Link: $message", cause)
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.apply {
            val spannable = SpannableString(UNIVERSAL_LINK_SPANNABLE_TEXT)
            spannable.setSpan(UnderlineSpan(), 0, UNIVERSAL_LINK_SPANNABLE_TEXT.length, 0)
            simulateUniversalLinkButton.text = spannable
            simulateUniversalLinkButton.setOnClickListener {
                // Mock universal link URI received from intent.data
                // Also, set your own scheme and host in the manifest file
                handleUniversalLink(MOMENT_UNIVERSAL_LINK_URI)
            }
            playStoriesButton.setOnClickListener {
                playStoriesByInputLabelExpression()
            }
            playMomentsButton.setOnClickListener {
                playMomentsByInputLabelExpression()
            }
            playVideosButton.setOnClickListener {
                playVideosByInputLabelExpression()
            }
            playStoryIdButton.setOnClickListener {
                BlazeSDK.playStory("6541a24b347bb42284ddf5a4") // id of the story to play
            }
            playMomentIdButton.setOnClickListener {
                BlazeSDK.playMoment("684943a1a26bafd24a1f9ea9") // id of the moment to play
            }
            playVideoIdButton.setOnClickListener {
                BlazeSDK.playVideo("6801034b8c4c8e78a2e1d11b") // id of the video to play
            }
        }
    }

    /**
     * Handles the universal link by calling BlazeSDK.handleUniversalLink.
     * For more information, refer to https://dev.wsc-sports.com/docs/android-methods-and-parameters#/universal-links.
     */
    private fun handleUniversalLink(universalLinkStr: String) {
        BlazeSDK.handleUniversalLink(universalLinkStr) {
            it.doOnSuccess {
                Log.d("appDebug", "EntryPointActivity: handleUniversalLink: Completed Successfully")
            }.doOnFailure { message, cause ->
                Log.e("appDebug", "EntryPointActivity: handleUniversalLink: Unable to open Link: $message", cause)
            }
        }
    }

    private fun initLabelExpressionStringInputs() {
        with(binding) {
            storiesLabelExpressionEditText.setText(STORIES_WIDGET_DEFAULT_LABEL)
            momentsLabelExpressionEditText.setText(MOMENTS_WIDGET_DEFAULT_LABEL)
            videosLabelExpressionEditText.setText(VIDEOS_WIDGET_DEFAULT_LABEL)
        }
    }

    /**
     * Prepares the players for stories and moments by initializing the data sources and calling the prepare method.
     */
    private fun preparePlayers() {
        val storiesDataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.Companion.singleLabel(STORIES_WIDGET_DEFAULT_LABEL)
        )
        val momentsDataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(MOMENTS_WIDGET_DEFAULT_LABEL)
        )
        val videosDataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(VIDEOS_WIDGET_DEFAULT_LABEL)
        )
        BlazeSDK.prepareStories(storiesDataSource)
        BlazeSDK.prepareMoments(momentsDataSource)
        BlazeSDK.prepareVideos(videosDataSource)
    }

    /**
     * Plays stories by the input label expression.
     * This method also demonstrates how to use the `entryContentId` param.
     * For more information, refer to https://dev.wsc-sports.com/docs/android-methods-and-parameters#/stories.
     */
    private fun playStoriesByInputLabelExpression() {
        binding.storiesLabelExpressionEditText.text?.toString()?.let { labelExpressionStr ->
            val storiesDataSource = BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.singleLabel(labelExpressionStr)
            )

            // If this story id is part of the `storiesDataSource` response, it will be moved to the front of the list, otherwise
            // it will be added to the front of the list.
            val entryContentId = "684ac5d9755bfece433b8865"

            BlazeSDK.playStories(
                dataSource = storiesDataSource,
                entryContentId = entryContentId
            )
        }
    }

    /**
     * Plays moments by the input label expression.
     * For more information, refer to https://dev.wsc-sports.com/docs/android-methods-and-parameters#/moments.
     */
    private fun playMomentsByInputLabelExpression() {
        binding.momentsLabelExpressionEditText.text?.toString()?.let { labelExpressionStr ->
            val momentsDataSource = BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.singleLabel(labelExpressionStr)
            )
            BlazeSDK.playMoments(momentsDataSource)
        }
    }

    /**
     * Plays videos by the input label expression.
     * For more information, refer to https://dev.wsc-sports.com/docs/android-methods-and-parameters#/videos.
     */
    private fun playVideosByInputLabelExpression() {
        binding.videosLabelExpressionEditText.text?.toString()?.let { labelExpressionStr ->
            val videosDataSource = BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.singleLabel(labelExpressionStr)
            )
            BlazeSDK.playVideos(videosDataSource)
        }
    }

    /**
     * Sets the player state.
     * You can pause, resume, or dismiss the player.
     */
    private fun setPlayerState() {
//        BlazeSDK.pauseCurrentPlayer()
//        BlazeSDK.resumeCurrentPlayer()
//        BlazeSDK.dismissCurrentPlayer()
    }

    companion object {
        private const val PUSH_INTENT_WSC_DATA_EXTRA_PARAM = "WscIasData"
        const val UNIVERSAL_LINK_SPANNABLE_TEXT = "https://your-link.com"
        const val MOMENT_UNIVERSAL_LINK_URI = "https://blazesample.clipro.tv/moments/684943a1a26bafd24a1f9ea9"
    }

}