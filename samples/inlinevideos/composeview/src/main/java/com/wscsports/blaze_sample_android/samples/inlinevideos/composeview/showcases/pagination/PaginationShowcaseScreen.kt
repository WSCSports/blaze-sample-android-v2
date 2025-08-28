package com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.showcases.pagination

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideoDataGenerator
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosViewModel
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.managers.LazyColumnScrollVisibilityCenterActivation
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.shared.FeedItem
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.shared.PlayerConfig
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.shared.VideoItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

/**
 * PaginationShowcaseScreen demonstrates inline videos with Paging 3 library.
 * 
 * This showcase shows how to implement inline videos with proper pagination using
 * Android's Paging 3 library. This is the recommended approach for real apps
 * as it handles loading states, error handling, and performance automatically.
 * 
 * Key features demonstrated:
 * - Paging 3 library integration with LazyPagingItems
 * - PagingSource implementation for data loading
 * - Automatic loading states (initial, append, refresh)
 * - Visibility-based player management with one active player
 * - Preview player mode designed for feeds
 * - Proper error handling and retry mechanisms
 * 
 * Implementation notes:
 * - Uses Paging 3 for realistic pagination implementation
 * - PagingSource handles page loading with proper error states
 * - LazyPagingItems provides automatic loading indicators
 * - Only one player active at a time for performance
 * - Each player has unique containerId across all pages
 * - Simulates network delay for realistic loading experience
 */

// Constants for the showcase
private const val CONTAINER_PREFIX = "pagination_showcase"
private const val PAGE_SIZE = 5
private val PLAYER_CONFIG = PlayerConfig.PREVIEW

@Composable
fun PaginationShowcaseScreen(
    viewModel: InlineVideosViewModel,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var activePlayerIndex by remember { mutableStateOf(-1) }
    
    // Create paging data flow
    val pagingDataFlow: Flow<PagingData<FeedItem>> = remember {
        createPagingDataFlow()
    }
    
    val feedItems: LazyPagingItems<FeedItem> = pagingDataFlow.collectAsLazyPagingItems()
    
    // Loading states
    val isLoading = feedItems.loadState.refresh == LoadState.Loading
    val isAppending = feedItems.loadState.append == LoadState.Loading
    
    // Visibility management for one active player
    LazyColumnScrollVisibilityCenterActivation(
        listState = listState,
        onActiveIndexChanged = { index ->
            activePlayerIndex = index
        }
    )

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Video items
        items(
            count = feedItems.itemCount,
            key = feedItems.itemKey { it.id }
        ) { index ->
            val item = feedItems[index]
            if (item != null) {
                VideoItem(
                    item = item,
                    modifier = Modifier.fillMaxWidth(),
                    isActive = index == activePlayerIndex,
                    viewModel = viewModel
                )
            }
            // Empty placeholder for null items during loading
        }
        
        // Loading indicator for append
        if (isAppending) {
            item(key = "loading_indicator") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

/**
 * Creates a Paging data flow for the video feed.
 * 
 * This function sets up the Paging 3 infrastructure with a custom PagingSource
 * that simulates loading video data from a backend API. In real implementations,
 * this would connect to your actual data source.
 * 
 * @return Flow of PagingData containing FeedItem objects
 */
private fun createPagingDataFlow(): Flow<PagingData<FeedItem>> {
    return Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = PAGE_SIZE * 2 // Load more items initially
        ),
        pagingSourceFactory = { VideoFeedPagingSource() }
    ).flow
}

/**
 * PagingSource implementation for loading video feed data.
 * 
 * This class handles the actual data loading logic for pagination. In real apps,
 * this would make network requests to your backend API and handle errors properly.
 * 
 * Key features:
 * - Simulates realistic network delays
 * - Generates unique video items with proper IDs
 * - Handles page-based loading with proper key management
 * - Includes error simulation for testing error states
 */
private class VideoFeedPagingSource : PagingSource<Int, FeedItem>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FeedItem> {
        return try {
            val page = params.key ?: 0
            
            // Simulate network delay
            delay(1000)
            
            // Generate items for this page
            val items = generatePageItems(page, params.loadSize)
            
            LoadResult.Page(
                data = items,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, FeedItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    
    private fun generatePageItems(page: Int, loadSize: Int): List<FeedItem> {
        return (0 until loadSize).map { itemIndex ->
            val globalIndex = page * PAGE_SIZE + itemIndex
            
            // Generate video data
            val videoData = InlineVideoDataGenerator.generateSingleItem(globalIndex, "${CONTAINER_PREFIX}_page_${page}")
            
            FeedItem(
                id = videoData.id,
                playerConfig = PLAYER_CONFIG,
                title = videoData.title,
                description = videoData.description,
                label = videoData.label
            )
        }
    }
}


