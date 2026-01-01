package com.wscsports.blaze_sample_android.samples.widgets.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.delegates.models.BlazeWidgetItemClickHandlerState
import com.blaze.blazesdk.delegates.models.BlazeWidgetItemClickParams
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.blaze.blazesdk.widgets.models.BlazeWidgetPlayFrom
import com.wscsports.blaze_sample_android.core.Constants.STORIES_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.blaze_sample_android.samples.widgets.R
import com.wscsports.blaze_sample_android.samples.widgets.databinding.FragmentMethodsDelegatesBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

/**
 * MethodsDelegatesFragment demonstrates the three variations of the `play()` method
 * available in Blaze widgets and the `onWidgetItemClickHandler` delegate.
 *
 * ## Play Method Variations:
 *
 * 1. **play()** - Starts playback from the first item (index 0)
 *
 * 2. **play(from: BlazeWidgetPlayFrom.Index(index))** - Starts playback from a specific
 *    zero-based index in the widget's content list
 *
 * 3. **play(from: BlazeWidgetPlayFrom.ContentId(contentId))** - Starts playback from a
 *    specific content ID
 *
 * ## onWidgetItemClickHandler Delegate:
 *
 * The `onWidgetItemClickHandler` allows the app to intercept widget item clicks and
 * decide whether the SDK should handle them (SDK_SHOULD_HANDLE) or the app will handle
 * them manually (HANDLED_BY_APP). When returning HANDLED_BY_APP, you can use `play(from:)`
 * to manually trigger playback.
 *
 * **Note:** The same implementation pattern applies to Moments and Videos widgets:
 * - `BlazeMomentsWidgetRowView` / `BlazeMomentsWidgetGridView`
 * - `BlazeVideosWidgetRowView` / `BlazeVideosWidgetGridView`
 *
 * All widget types support the same `play()`, `play(from:)` methods and
 * `onWidgetItemClickHandler` delegate.
 */
class MethodsDelegatesFragment : Fragment(R.layout.fragment_methods_delegates),
    BlazeWidgetDelegate by WidgetDelegateImpl() {

    private val binding by viewBinding(FragmentMethodsDelegatesBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWidget()
        setupPlayButtons()
    }

    /**
     * Initialize the stories widget with a data source and click handler.
     *
     * The `onWidgetItemClickHandler` parameter allows you to intercept clicks on widget items.
     * When provided, it receives [BlazeWidgetItemClickParams] containing:
     * - widgetId: The ID of the widget
     * - contentIndex: The zero-based index of the clicked item
     * - contentId: The unique content ID of the clicked item
     *
     * Return [BlazeWidgetItemClickHandlerState.SDK_SHOULD_HANDLE] to let the SDK handle the click,
     * or [BlazeWidgetItemClickHandlerState.HANDLED_BY_APP] to handle it yourself.
     */
    private fun initWidget() {
        val widgetLayout: BlazeWidgetLayout = BlazeWidgetLayout.Presets.StoriesWidget.Row.circles
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(STORIES_WIDGET_DEFAULT_LABEL)
        )

        binding.storiesWidgetView.initWidget(
            widgetLayout = widgetLayout,
            dataSource = dataSource,
            widgetId = "methods-delegates-stories-widget",
            widgetDelegate = this,
            shouldOrderWidgetByReadStatus = true,
            onWidgetItemClickHandler = { params -> handleWidgetItemClick(params) }
        )
    }

    /**
     * Handle widget item clicks.
     *
     * This handler intercepts widget item clicks and manually triggers playback
     * using the content ID from the click params.
     */
    private fun handleWidgetItemClick(params: BlazeWidgetItemClickParams): BlazeWidgetItemClickHandlerState {
        // Use a small delay to mock app doing some background work
        Handler(Looper.getMainLooper()).postDelayed({
            // Play from the clicked item's content ID
            binding.storiesWidgetView.play(
                from = BlazeWidgetPlayFrom.ContentId(params.contentId)
            )
        }, 500)

        return BlazeWidgetItemClickHandlerState.HANDLED_BY_APP
    }

    private fun setupPlayButtons() {
        // Button 1: play() - Start from first item (default behavior)
        binding.btnPlayDefault.setOnClickListener {
            binding.storiesWidgetView.play()
        }

        // Button 2: play(from: Index) - Start from specific index
        binding.btnPlayFromIndex.setOnClickListener {
            // The index can be customized based on your needs
            val index = 3
            binding.storiesWidgetView.play(
                from = BlazeWidgetPlayFrom.Index(index)
            )
        }

        // Button 3: play(from: ContentId) - Start from specific content ID
        binding.btnPlayFromContentId.setOnClickListener {
            // The content ID can be customized based on your needs
            val contentId = "65631c2f182ca9d8338f6b99"
            binding.storiesWidgetView.play(
                from = BlazeWidgetPlayFrom.ContentId(contentId)
            )
        }
    }
}