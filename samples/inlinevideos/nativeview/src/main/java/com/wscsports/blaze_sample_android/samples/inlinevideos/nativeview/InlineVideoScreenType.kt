package com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview

/**
 * Enum representing the different types of inline video showcases available in the native view module.
 * Each type demonstrates a specific use case and implementation pattern for BlazeVideosInlinePlayer.
 */
enum class InlineVideoScreenType(
    val title: String,
    val description: String,
    val navDestinationId: Int
) {
    /**
     * Inline Video Methods Showcase - like a video platform demo.
     * Shows a standalone inline player with buttons for all available inline video methods.
     * Think of this as a methods playground where you can test every inline video capability.
     * 
     * **Key Features for Clients:**
     * - All BlazeVideosInlinePlayer methods exposed as buttons
     * - Inline video player initialization and setup methods
     * - Inline video lifecycle management methods
     * - Delegate implementation patterns
     * - Real inline video method demonstrations
     */
    METHODS_SHOWCASE(
        title = "Methods Showcase",
        description = "Video platform-style demo with all inline video methods and capabilities as buttons",
        navDestinationId = R.id.action_navigation_inline_video_list_to_methodsShowcaseFragment
    ),

    /**
     * ScrollView demonstration showcase.
     * Shows how to implement inline video players in a scrollable container.
     * 
     * **Key Features for Clients:**
     * - Single active player pattern (performance optimization)
     * - Scroll-based lifecycle handling
     * - Inline video integration in ScrollView
     * - Memory optimization techniques
     * - Proper disposal when items go off-screen
     */
    SCROLLVIEW_SHOWCASE(
        title = "ScrollView Showcase",
        description = "Inline video players in ScrollView - demonstrates single active player and lifecycle handling",
        navDestinationId = R.id.action_navigation_inline_video_list_to_scrollViewFragment
    ),

    /**
     * RecyclerView demonstration showcase.
     * Shows how to integrate inline video players with RecyclerView for large datasets.
     * 
     * **Key Features for Clients:**
     * - View recycling with video players
     * - Inline video integration in RecyclerView
     * - Efficient memory usage
     * - ViewHolder pattern integration
     * - Smooth scrolling performance
     */
    RECYCLERVIEW_SHOWCASE(
        title = "RecyclerView Showcase",
        description = "Inline video players in RecyclerView - demonstrates efficient view recycling for large lists",
        navDestinationId = R.id.action_navigation_inline_video_list_to_recyclerViewFragment
    );
}
