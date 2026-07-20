package com.wscsports.blaze_sample_android.samples.widgets.edit

import com.blaze.blazesdk.data_source.BlazeCompositeDataSourceConfig
import com.blaze.blazesdk.data_source.BlazeCompositeDataSourceEntry
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeOrderType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.wscsports.blaze_sample_android.core.Constants.DATA_SOURCE_NO_CONTENT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_SAMPLE_IDS
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.STORIES_SAMPLE_IDS
import com.wscsports.blaze_sample_android.core.Constants.STORIES_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.VIDEOS_SAMPLE_IDS
import com.wscsports.blaze_sample_android.core.Constants.VIDEOS_WIDGET_DEFAULT_LABEL

/**
 * The kind of content a widget shows. Each type carries the sample label and IDs its data
 * source examples use, so every example returns real content on every widget screen.
 */
enum class WidgetContentType(
    val mainLabel: String,
    val sampleIds: List<String>,
) {
    STORIES(mainLabel = STORIES_WIDGET_DEFAULT_LABEL, sampleIds = STORIES_SAMPLE_IDS),
    MOMENTS(mainLabel = MOMENTS_WIDGET_DEFAULT_LABEL, sampleIds = MOMENTS_SAMPLE_IDS),
    VIDEOS(mainLabel = VIDEOS_WIDGET_DEFAULT_LABEL, sampleIds = VIDEOS_SAMPLE_IDS),
}

/**
 * The data source examples available in the "Edit data source" bottom sheet.
 * Each option maps to a different [BlazeDataSourceType] - see [WidgetDataState.toDataSource].
 */
enum class DataSourceExample(val title: String, val description: String) {
    LABELS(
        title = "Labels",
        description = "Content tagged with the widget's default label",
    ),
    IDS(
        title = "IDs",
        description = "Two specific content items fetched by their IDs",
    ),
    COMPLEX_LABELS(
        title = "Complex labels expression",
        description = "A nested label expression with a labels priority",
    ),
    COMPOSITE(
        title = "Composite",
        description = "Merges an empty optional source with a mandatory label source, deduplicated",
    ),
}

data class WidgetDataState(
    val contentType: WidgetContentType = WidgetContentType.STORIES,
    val selectedExample: DataSourceExample = DataSourceExample.LABELS,
    val orderType: BlazeOrderType = BlazeOrderType.MANUAL,
) {

    // The widget's main label, e.g. "stories-main" / "moments-main" / "video-long-form".
    private val mainLabel: String get() = contentType.mainLabel

    /**
     * Builds the [BlazeDataSourceType] for the selected example.
     * Note: [orderType] is only relevant for the Labels and IDs examples.
     */
    fun toDataSource(): BlazeDataSourceType = when (selectedExample) {
        // Content tagged with a single label.
        DataSourceExample.LABELS -> BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(mainLabel),
            orderType = orderType,
        )

        // Specific content items fetched by their IDs.
        DataSourceExample.IDS -> BlazeDataSourceType.Ids(
            ids = contentType.sampleIds,
            orderType = orderType,
        )

        // A nested label expression combined with a labels priority.
        DataSourceExample.COMPLEX_LABELS -> BlazeDataSourceType.Labels(
            blazeWidgetLabel = complexLabelExpression,
            labelsPriority = simpleLabelsPriority,
            orderType = orderType
        )

        // Combines multiple data sources into a single feed.
        DataSourceExample.COMPOSITE -> BlazeDataSourceType.Composite(
            dataSources = listOf(
                optionalEmptySourceEntry,
                mandatoryLabelSourceEntry
            )
        )
    }

    // Builds "<mainLabel> OR (B AND C) OR D":
    // content matches if it has the main label, OR both B and C, OR D.
    // atLeastOneOf = logical OR, mustInclude = logical AND - they nest to form complex expressions.
    // The main label is a top-level OR operand, so content tagged with it is returned on its own -
    // that keeps the widget populated even though the sample B/C/D labels match nothing.
    private val complexLabelExpression: BlazeWidgetLabel
        get() = BlazeWidgetLabel.atLeastOneOf(
            BlazeWidgetLabel.singleLabel(mainLabel),
            BlazeWidgetLabel.mustInclude("B", "C"),
            BlazeWidgetLabel.singleLabel("D"),
        )

    // Orders the results by label priority: items labeled B come first, then C, then D.
    private val simpleLabelsPriority: List<BlazeWidgetLabel>
        get() = listOf(
            BlazeWidgetLabel.singleLabel("B"),
            BlazeWidgetLabel.singleLabel("C"),
            BlazeWidgetLabel.singleLabel("D"),
        )

    // An entry pointing at a label with no matching content. It is not mandatory, so when the
    // SDK fetches all entries in parallel and merges them (in declaration order, deduplicated
    // by item ID), this entry contributes nothing and is skipped silently.
    private val optionalEmptySourceEntry: BlazeCompositeDataSourceEntry
        get() = BlazeCompositeDataSourceEntry(
            dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(DATA_SOURCE_NO_CONTENT_LABEL)),
            config = BlazeCompositeDataSourceConfig(isMandatory = false)
        )

    // An entry pointing at the widget's real label. It is mandatory, so if this entry's fetch
    // fails, the whole composite fetch fails - regardless of whether other entries succeeded.
    private val mandatoryLabelSourceEntry: BlazeCompositeDataSourceEntry
        get() = BlazeCompositeDataSourceEntry(
            dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(mainLabel)),
            config = BlazeCompositeDataSourceConfig(isMandatory = true)
        )
}
