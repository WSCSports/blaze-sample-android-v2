package com.wscsports.blaze_sample_android.samples.inlinevideos

import com.wscsports.blaze_sample_android.core.ui.R

/**
 * Enum representing the different types of inline videos implementations available in the sample app.
 * Each type corresponds to a different approach for implementing inline video functionality.
 */
enum class InlineVideosType(
    val title: String,
    val description: String,
    val imageResourceId: Int,
    val classPathSuffix: String
) {
    /**
     * Native Android Views implementation of inline videos.
     * Demonstrates traditional Android View-based approach with:
     * - Interactive inline videos with method buttons
     * - ScrollView implementation with manager
     * - RecyclerView implementation with manager
     */
    NATIVE_VIEW(
        title = "Native View",
        description = "Traditional Android Views implementation with ScrollView, RecyclerView, and interactive examples",
        imageResourceId = R.drawable.ic_widgets,
        classPathSuffix = "nativeview.InlineVideosNativeActivity"
    ),

    /**
     * Jetpack Compose implementation of inline videos.
     * Demonstrates modern Compose-based approach with:
     * - Interactive inline videos with method buttons
     * - LazyColumn implementation
     * - Pagination examples
     */
    COMPOSE_VIEW(
        title = "Compose View", 
        description = "Modern Jetpack Compose implementation with LazyColumn, pagination, and interactive examples",
        imageResourceId = R.drawable.ic_compose,
        classPathSuffix = "composeview.InlineVideosComposeActivity"
    );
}
