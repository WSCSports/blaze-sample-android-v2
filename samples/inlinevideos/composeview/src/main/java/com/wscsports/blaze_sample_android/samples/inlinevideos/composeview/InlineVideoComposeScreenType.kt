package com.wscsports.blaze_sample_android.samples.inlinevideos.composeview

/**
 * Enum defining the different Compose inline video showcase screens available.
 * Each entry contains display information and navigation route for the showcase.
 */
enum class InlineVideoComposeScreenType(
    val title: String,
    val description: String,
    val route: String
) {
    METHODS_SHOWCASE(
        title = "Methods Showcase",
        description = "Interactive player demonstrating all BlazeVideosInlinePlayerCompose methods and capabilities",
        route = "methods_showcase"
    ),
    
    COLUMN_SHOWCASE(
        title = "Column Showcase", 
        description = "Multiple inline players in a Column with one active player management",
        route = "column_showcase"
    ),
    
    LAZY_COLUMN_SHOWCASE(
        title = "Lazy Column Showcase",
        description = "Lazy Column implementation with efficient player recycling and visibility management", 
        route = "lazy_column_showcase"
    ),
    
    PAGINATION_SHOWCASE(
        title = "Pagination Showcase",
        description = "Paginated video feed with simple counter and infinite scroll behavior",
        route = "pagination_showcase"
    )
}
