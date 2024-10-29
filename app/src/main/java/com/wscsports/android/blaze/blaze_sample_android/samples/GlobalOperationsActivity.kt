package com.wscsports.android.blaze.blaze_sample_android.samples

import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.shared.BlazeSDK
import com.blaze.blazesdk.shared.results.doOnFailure
import com.blaze.blazesdk.shared.results.doOnSuccess
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerStyle
import com.example.blaze_sample_android.R
import com.example.blaze_sample_android.databinding.ActivityGlobalConfigurationBinding
import com.wscsports.android.blaze.blaze_sample_android.SampleItem
import com.wscsports.android.blaze.blaze_sample_android.core.ui.viewBinding

class GlobalOperationsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityGlobalConfigurationBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAppbar()
        setClickListeners()
        initLabelExpressionStringInputs()
        preparePlayers()
        setDefaultPlayersStyle()
        setPlayerState()
    }

    private fun setupAppbar() {
        binding.appbar.setupView(SampleItem.GLOBAL_OPERATIONS.title) {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Optional - Set default style for Blaze players.
     * If not implemented, the default style will be BlazeStoryPlayerStyle.base() and BlazeMomentsPlayerStyle.base()
     * More information about Blaze players style customization can be found in the documentation
     * https://dev.wsc-sports.com/docs/android-blaze-story-player-style
     */
    private fun setDefaultPlayersStyle() {
        val storyPlayerStyle = BlazeStoryPlayerStyle.base().apply {
            backgroundColor = getColor(R.color.black)
            title.textColor = getColor(R.color.wsc_accent)
            title.textSize = 16f
        }
        val momentPlayerStyle = BlazeMomentsPlayerStyle.base().apply {
            backgroundColor = getColor(R.color.black)
            headingText.textSize = 22f
            bodyText.textSize = 18f
        }

        BlazeSDK.setDefaultStoryPlayerStyle(storyPlayerStyle)
        BlazeSDK.setDefaultMomentsPlayerStyle(momentPlayerStyle)
    }

    private fun initLabelExpressionStringInputs() {
        with(binding) {
            storiesLabelExpressionEditText.setText(INIT_STORIES_LABEL_EXPRESSION)
            momentsLabelExpressionEditText.setText(INIT_MOMENTS_LABEL_EXPRESSION)
        }
    }


    // TODO: add documentation for this use case
    private fun preparePlayers() {
        val storiesDataSource =  BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(INIT_STORIES_LABEL_EXPRESSION)
        )
        val momentsDataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(INIT_MOMENTS_LABEL_EXPRESSION)
        )
        BlazeSDK.prepareStories(storiesDataSource)
        BlazeSDK.prepareMoments(momentsDataSource)
    }

    private fun setClickListeners() {
        with(binding) {
            externalUserIdButton.setOnClickListener {
                setExternalUserIdFromInput()
            }
            geoLocationButton.setOnClickListener {
                setGeoLocationFromInput()
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
            switchDoNotTrackUser.setOnCheckedChangeListener { _, isChecked ->
                BlazeSDK.setDoNotTrack(isChecked)
            }
        }
    }

    private fun playStoriesByInputLabelExpression() {
        binding.storiesLabelExpressionEditText.text?.toString()?.let { labelExpressionStr ->
            val storiesDataSource =  BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.singleLabel(labelExpressionStr)
            )
            BlazeSDK.playStories(storiesDataSource)
        }
    }

    private fun playMomentsByInputLabelExpression() {
        binding.momentsLabelExpressionEditText.text?.toString()?.let { labelExpressionStr ->
            val momentsDataSource =  BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.singleLabel(labelExpressionStr)
            )
            BlazeSDK.playMoments(momentsDataSource)
        }
    }

    private fun setExternalUserIdFromInput() {
        binding.externalUserIdEditText.text?.toString()?.let { externalUserId ->
            BlazeSDK.setExternalUserId(externalUserId) { result ->
                result.doOnSuccess {
                    Log.d("GlobalConfigurationActivity", "setExternalUserId: doOnSuccess")
                }
                result.doOnFailure { message, cause ->
                    Log.e("GlobalConfigurationActivity", "setExternalUserId: doOnFailure: message = $message, cause = $cause")
                }
            }
        }
    }

    private fun setGeoLocationFromInput() {
        binding.geoLocationEditText.text?.toString()?.let { geoLocation ->
            BlazeSDK.updateGeoRestriction(geoLocation) { result ->
                result.doOnSuccess {
                    Log.d("GlobalConfigurationActivity", "setGeoLocation: doOnSuccess")
                }
                result.doOnFailure { message, cause ->
                    Log.e("GlobalConfigurationActivity", "setGeoLocation: doOnFailure: message = $message, cause = $cause")
                }
            }
        }
    }

    // TODO: description for use cases
    private fun setPlayerState() {
//        BlazeSDK.pauseCurrentPlayer()
//        BlazeSDK.resumeCurrentPlayer()
//        BlazeSDK.dismissCurrentPlayer()
    }


    companion object {
        const val INIT_STORIES_LABEL_EXPRESSION = "top-stories"
        const val INIT_MOMENTS_LABEL_EXPRESSION = "moments"
    }
}