package com.wscsports.blaze_sample_android.samples.follow

import com.wscsports.blaze_sample_android.core.ui.R

/**
 * The two implementations of the Moments Follow Tabs example,
 * listed on the sample's entry screen.
 */
enum class FollowImplementationType(
    val title: String,
    val description: String,
    val imageResourceId: Int,
) {
    NATIVE_VIEW(
        title = "Native View",
        description = "Moments Follow Tabs built with Android Views",
        imageResourceId = R.drawable.ic_widgets,
    ),
    COMPOSE_VIEW(
        title = "Compose View",
        description = "Moments Follow Tabs built with Jetpack Compose",
        imageResourceId = R.drawable.ic_compose,
    );
}
