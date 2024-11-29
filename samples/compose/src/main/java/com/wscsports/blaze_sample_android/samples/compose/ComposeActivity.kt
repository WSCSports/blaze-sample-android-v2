package com.wscsports.blaze_sample_android.samples.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.blaze.blazesdk.features.moments.widgets.compose.row.BlazeComposeMomentsWidgetRowView
import com.blaze.blazesdk.features.stories.widgets.compose.grid.BlazeComposeStoriesWidgetGridView
import com.blaze.blazesdk.features.stories.widgets.compose.row.BlazeComposeStoriesWidgetRowView
import com.wscsports.android.blaze.blaze_sample_android.core.ui.R
import com.wscsports.blaze_sample_android.samples.compose.ui.theme.BlazeSampleTheme

class ComposeActivity : ComponentActivity() {

    private val viewModel: ComposeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlazeSampleTheme {
                Scaffold(
                    topBar = {
                        SampleAppBar(
                            title = "Compose",
                            onBackPressed = { onBackPressedDispatcher.onBackPressed() }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MainScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: ComposeViewModel,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.verticalScroll(scrollState)) {
        Text(text = "Compose Stories Row ", modifier = Modifier.padding(16.dp))
        BlazeComposeStoriesWidgetRowView(
            modifier = Modifier
                .height(140.dp)
                .fillMaxWidth(),
            widgetStoriesStateHandler = viewModel.storiesRowStateHandler
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Compose Moments Row", modifier = Modifier.padding(16.dp))
        BlazeComposeMomentsWidgetRowView(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            widgetMomentsStateHandler = viewModel.momentsRowStateHandler
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Compose Stories Grid ", modifier = Modifier.padding(16.dp))
        BlazeComposeStoriesWidgetGridView(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            widgetStoriesStateHandler = viewModel.storiesGridStateHandler
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleAppBar(
    title: String,
    onBackPressed: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_button),
                    contentDescription = "Back"
                )
            }
        },
    )
}
