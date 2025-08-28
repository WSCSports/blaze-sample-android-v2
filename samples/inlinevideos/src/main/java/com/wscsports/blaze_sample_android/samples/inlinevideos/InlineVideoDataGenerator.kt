package com.wscsports.blaze_sample_android.samples.inlinevideos

/**
 * Shared utility for generating consistent data across inline video examples.
 * 
 * **Important for Clients:**
 * In a real implementation, you would replace these with your actual:
 * - Video content labels from your CMS
 * - Unique IDs from your data source
 * - Content metadata (titles, descriptions, etc.)
 * 
 * This class demonstrates the data structure and patterns needed for inline video integration.
 */
object InlineVideoDataGenerator {

    // Constants for consistent data generation
    private const val ITEM_COUNT = 10
    private const val ID_PREFIX = "video"
    private const val LABEL_PREFIX = "inline-video-"
    private const val TITLE_PREFIX = "Video Content "
    private const val DESCRIPTION = "Sample inline video content for testing player integration and performance"
    


    /**
     * Generates a list of video labels for testing.
     * 
     * **Client Implementation:**
     * Replace this with your actual video content labels from your CMS or backend.
     * Each label should correspond to a video collection in your Blaze configuration.
     */
    fun generateVideoLabels(count: Int = ITEM_COUNT): List<String> {
        return (1..count).map { index ->
            "$LABEL_PREFIX$index"
        }
    }

    /**
     * Generates unique IDs for video players.
     * 
     * **Critical for Clients:**
     * Each inline video player MUST have a unique ID for proper lifecycle management.
     * In your app, these could be:
     * - Database record IDs
     * - Content UUIDs 
     * - Position-based IDs (e.g., "list_item_${position}")
     */
    fun generateUniqueId(prefix: String = ID_PREFIX, index: Int): String {
        return "${prefix}_${index}_${System.currentTimeMillis()}"
    }

    /**
     * Generates sample video item data for display.
     * 
     * **Client Implementation:**
     * Replace with your actual video metadata from your backend/CMS.
     */
    data class InlineVideoItem(
        val id: String,
        val label: String,
        val title: String,
        val description: String
    )

    fun generateVideoItems(
        count: Int = ITEM_COUNT, 
        prefix: String = ID_PREFIX
    ): List<InlineVideoItem> {
        return (1..count).map { index ->
            InlineVideoItem(
                id = generateUniqueId(prefix, index),
                label = "$LABEL_PREFIX$index",
                title = "$TITLE_PREFIX$index",
                description = DESCRIPTION
            )
        }
    }

    /**
     * Generates a single video item for specific use cases like pagination.
     * 
     * @param index The index/position of the item
     * @param prefix Prefix for the ID generation
     */
    fun generateSingleItem(
        index: Int, 
        prefix: String = ID_PREFIX
    ): InlineVideoItem {
        return InlineVideoItem(
            id = generateUniqueId(prefix, index),
            label = "$LABEL_PREFIX${index + 1}",
            title = "$TITLE_PREFIX${index + 1}",
            description = DESCRIPTION
        )
    }
}
