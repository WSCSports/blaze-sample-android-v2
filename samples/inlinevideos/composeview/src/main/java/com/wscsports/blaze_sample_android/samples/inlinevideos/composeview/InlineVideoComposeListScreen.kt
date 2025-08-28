package com.wscsports.blaze_sample_android.samples.inlinevideos.composeview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wscsports.blaze_sample_android.core.ui.R

/**
 * Main Compose screen displaying a list of inline video showcase options.
 * 
 * This screen provides navigation to different Compose-based implementations
 * of the BlazeVideosInlinePlayerCompose, each demonstrating specific use cases
 * and integration patterns.
 */
@Composable
fun InlineVideoComposeListScreen(
    onNavigateToShowcase: (InlineVideoComposeScreenType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(InlineVideoComposeScreenType.values()) { screenType ->
            ShowcaseListItem(
                screenType = screenType,
                onClick = { onNavigateToShowcase(screenType) }
            )
        }
    }
}

/**
 * Individual list item for a showcase option.
 */
@Composable
private fun ShowcaseListItem(
    screenType: InlineVideoComposeScreenType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = screenType.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = screenType.description,
                    fontSize = 12.sp,
                    color = Color(0xFF8E8E8E),
                    lineHeight = 16.sp
                )
            }
            
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right_24),
                contentDescription = "Navigate to ${screenType.title}",
                tint = Color(0xFF8E8E8E),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
