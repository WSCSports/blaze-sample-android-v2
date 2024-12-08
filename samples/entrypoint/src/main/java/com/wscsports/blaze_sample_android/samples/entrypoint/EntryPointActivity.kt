package com.wscsports.blaze_sample_android.samples.entrypoint

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.shared.BlazeSDK
import com.blaze.blazesdk.shared.results.doOnFailure
import com.blaze.blazesdk.shared.results.doOnSuccess
import com.wscsports.android.blaze.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.entrypoint.databinding.ActivityEntryPointBinding

class EntryPointActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityEntryPointBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAppbar()
        handleUniversalLinkIfNeeded()
        handlePushNotificationIfNeeded()
        setClickListeners()
        initLabelExpressionStringInputs()
        preparePlayers()
        setPlayerState()
    }

    private fun setupAppbar() {
        binding.appbar.setupView("Entry Point") {
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
        with(binding) {
            simulateUniversalLinkButton.setOnClickListener {
                // Mock universal link URI received from intent.data
                val universalLinkUri = "https://prime.mvp.fan/moments/64ee1f9f1396e4277f059613"
                handleUniversalLink(universalLinkUri)
            }
            playStoriesButton.setOnClickListener {
                playStoriesByInputLabelExpression()
            }
            playMomentsButton.setOnClickListener {
                playMomentsByInputLabelExpression()
            }
            playStoryButton.setOnClickListener {
                BlazeSDK.playStory("your-story-id")
            }
            playMomentButton.setOnClickListener {
                BlazeSDK.playMoment("your-moment-id")
            }
        }
    }

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
            storiesLabelExpressionEditText.setText(INIT_STORIES_LABEL_EXPRESSION)
            momentsLabelExpressionEditText.setText(INIT_MOMENTS_LABEL_EXPRESSION)
        }
    }

    /**
     * Prepares the players for stories and moments by initializing the data sources and calling the prepare method.
     */
    private fun preparePlayers() {
        val storiesDataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.Companion.singleLabel(INIT_STORIES_LABEL_EXPRESSION)
        )
        val momentsDataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(INIT_MOMENTS_LABEL_EXPRESSION)
        )
        BlazeSDK.prepareStories(storiesDataSource)
        BlazeSDK.prepareMoments(momentsDataSource)
    }

    private fun playStoriesByInputLabelExpression() {
        binding.storiesLabelExpressionEditText.text?.toString()?.let { labelExpressionStr ->
            val storiesDataSource = BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.singleLabel(labelExpressionStr)
            )
            BlazeSDK.playStories(storiesDataSource)
        }
    }

    private fun playMomentsByInputLabelExpression() {
        binding.momentsLabelExpressionEditText.text?.toString()?.let { labelExpressionStr ->
            val momentsDataSource = BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.singleLabel(labelExpressionStr)
            )
            BlazeSDK.playMoments(momentsDataSource)
        }
    }

    // TODO: description for use cases
    private fun setPlayerState() {
//        BlazeSDK.pauseCurrentPlayer()
//        BlazeSDK.resumeCurrentPlayer()
//        BlazeSDK.dismissCurrentPlayer()
    }

    companion object {
        private const val PUSH_INTENT_WSC_DATA_EXTRA_PARAM = "WscIasData"
        const val INIT_STORIES_LABEL_EXPRESSION = "top-stories"
        const val INIT_MOMENTS_LABEL_EXPRESSION = "moments"
    }

}