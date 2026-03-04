package com.wscsports.blaze_sample_android.samples.search.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.blaze.blazesdk.features.moments.widgets.compose.BlazeComposeWidgetMomentsStateHandler
import com.blaze.blazesdk.features.moments.widgets.compose.grid.BlazeComposeMomentsWidgetGridView

/**
 * Suggestions grid composable that uses the existing [BlazeComposeMomentsWidgetGridView]
 * with a 3-column vertical rectangles layout.
 */
@Composable
fun SuggestionsGridWidget(
    stateHandler: BlazeComposeWidgetMomentsStateHandler,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        BlazeComposeMomentsWidgetGridView(
            modifier = Modifier.fillMaxWidth(),
            widgetMomentsStateHandler = stateHandler
        )
    }
}
