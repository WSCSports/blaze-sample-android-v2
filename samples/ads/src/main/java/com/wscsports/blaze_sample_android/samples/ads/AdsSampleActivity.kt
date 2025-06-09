package com.wscsports.blaze_sample_android.samples.ads

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blaze.blazesdk.ads.models.ui.BlazeMomentsAdsConfigType
import com.blaze.blazesdk.ads.models.ui.BlazeStoriesAdsConfigType
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.blaze.gam.BlazeGAM
import com.blaze.gam.banner.BlazeGAMBannerAdsDelegate
import com.blaze.gam.custom_native.BlazeGAMCustomNativeAdsDefaultConfig
import com.blaze.gam.custom_native.BlazeGAMCustomNativeAdsDelegate
import com.blaze.ima.BlazeIMA
import com.blaze.ima.BlazeIMADelegate
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.blaze_sample_android.core.ui.R.string
import com.wscsports.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.ads.databinding.ActivityAdsSampleBinding

/**
 * This activity demonstrates how to enable Blaze SDK ads in your app.
 * For more information on the ads configuration, refer to https://dev.wsc-sports.com/docs/android-ads#/
 */
class AdsSampleActivity : AppCompatActivity(),
    BlazeIMADelegate by BlazeIMADelegateImpl(),
    BlazeGAMCustomNativeAdsDelegate by BlazeGAMCustomNativeAdsDelegateImpl(),
    BlazeGAMBannerAdsDelegate by BlazeGAMBannerAdsDelegateImpl(),
    BlazeWidgetDelegate by WidgetDelegateImpl() {

    private val binding by viewBinding(ActivityAdsSampleBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding()
        setupAppbar()
        enableBlazeSDKAds()
        initWidgets()
    }

    private fun setupAppbar() {
        binding.appbar.setupView(getString(string.ads_title)) {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Enable Blaze SDK ads.
     * Note: Ads can also be enabled in the SDK.init() completion block.
     * Once enabled, they remain active for the entire app lifecycle.
     */
    private fun enableBlazeSDKAds() {
        BlazeIMA.enableAds(this)
        BlazeGAM.enableCustomNativeAds(
            context = applicationContext,
            defaultAdsConfig =  BlazeGAMCustomNativeAdsDefaultConfig(
                "/123456789/CustomNativeUnitTest",
                "123456"
            ),
            delegate = this)
        BlazeGAM.enableBannerAds(context = applicationContext, delegate = this)
    }

    private fun initWidgets() {
        initStoriesRowWidget()
        initMomentsRowWidget()
        initStoriesGridWidget()
    }

    /**
     * Initialize the stories row widget.
     * For more information on the ads configuration, refer to
     * https://dev.wsc-sports.com/docs/android-blaze-ads-config-type#/blazestoriesadsconfigtype
     */
    private fun initStoriesRowWidget() {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel("live-stories")
        )
        binding.storiesRowWidgetView.updateAdsConfigType(BlazeStoriesAdsConfigType.EVERY_X_STORIES)
        binding.storiesRowWidgetView.initWidget(
            widgetLayout = BlazeWidgetLayout.Presets.StoriesWidget.Row.circles,
            dataSource = dataSource,
            widgetId = "ads-stories-row-id",
            widgetDelegate = this
        )
    }

    /**
     * Initialize the moments row widget.
     * For more information on the ads configuration, refer to
     * https://dev.wsc-sports.com/docs/android-blaze-ads-config-type#/blazemomentsadsconfigtype
     */
    private fun initMomentsRowWidget() {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel("moments")
        )
        binding.momentsRowWidgetView.updateAdsConfigType(BlazeMomentsAdsConfigType.EVERY_X_MOMENTS)
        binding.momentsRowWidgetView.initWidget(
            widgetLayout = BlazeWidgetLayout.Presets.MomentsWidget.Row.verticalAnimatedThumbnailsRectangles,
            dataSource = dataSource,
            widgetId = "ads-moments-row-id",
            widgetDelegate = this
        )
    }

    private fun initStoriesGridWidget() {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel("top-stories")
        )
        binding.storiesGridWidgetView.updateAdsConfigType(BlazeStoriesAdsConfigType.FIRST_AVAILABLE_ADS_CONFIG)
        binding.storiesGridWidgetView.initWidget(
            widgetLayout = BlazeWidgetLayout.Presets.StoriesWidget.Grid.twoColumnsVerticalRectangles,
            dataSource = dataSource,
            widgetId = "ads-stories-grid-id",
            widgetDelegate = this
        )
    }

    /**
     * Optional - Disable Blaze SDK ads.
     * Ads can be disabled at any time, not necessarily in onDestroy.
     * To re-enable the ads, simply call the enableBlazeSDKAds() method.
     */
    private fun disableBlazeSDKAds() {
        BlazeIMA.disableAds()
        BlazeGAM.disableCustomNativeAds()
        BlazeGAM.disableBannerAds()
    }

    override fun onDestroy() {
        super.onDestroy()
        disableBlazeSDKAds()
    }
}