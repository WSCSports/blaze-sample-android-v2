package com.wscsports.blaze_sample_android.samples.inlinevideos.composeview

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosViewModel
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.showcases.column.ColumnShowcaseScreen
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.showcases.lazy.LazyColumnShowcaseScreen
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.showcases.methods.MethodsShowcaseScreen
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.showcases.pagination.PaginationShowcaseScreen

/**
 * Navigation setup for Compose inline video showcases.
 * 
 * Handles navigation between the main list screen and individual showcase screens.
 * Each showcase demonstrates different aspects of BlazeVideosInlinePlayerCompose integration.
 */
@Composable
fun InlineVideoComposeNavigation(
    navController: NavHostController,
    viewModel: InlineVideosViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "showcase_list",
        modifier = modifier
    ) {
        // Main list screen showing all available showcases
        composable("showcase_list") {
            InlineVideoComposeListScreen(
                onNavigateToShowcase = { screenType ->
                    navController.navigate(screenType.route)
                }
            )
        }
        
        // Methods Showcase - Interactive player with method buttons
        composable(InlineVideoComposeScreenType.METHODS_SHOWCASE.route) {
            MethodsShowcaseScreen(viewModel = viewModel)
        }
        
        // Column Showcase - Multiple players in Column
        composable(InlineVideoComposeScreenType.COLUMN_SHOWCASE.route) {
            ColumnShowcaseScreen(viewModel = viewModel)
        }
        
        // Lazy Column Showcase - Efficient scrolling with recycling
        composable(InlineVideoComposeScreenType.LAZY_COLUMN_SHOWCASE.route) {
            LazyColumnShowcaseScreen(viewModel = viewModel)
        }
        
        // Pagination Showcase - Infinite scroll with pagination
        composable(InlineVideoComposeScreenType.PAGINATION_SHOWCASE.route) {
            PaginationShowcaseScreen(viewModel = viewModel)
        }
    }
}

/**
 * Temporary placeholder screen for showcases that haven't been implemented yet.
 * This allows the navigation structure to be tested while individual screens are being developed.
 */
@Composable
private fun PlaceholderScreen(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🚧 $title",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Implementation coming soon...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
