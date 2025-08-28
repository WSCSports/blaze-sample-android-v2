package com.wscsports.blaze_sample_android.samples.inlinevideos.composeview

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.wscsports.blaze_sample_android.core.ui.theme.BlazeSampleTheme
import com.wscsports.blaze_sample_android.core.ui.R
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosViewModel

/**
 * Main activity for Compose-based inline video showcases.
 * 
 * Provides a Compose UI for demonstrating BlazeVideosInlinePlayerCompose
 * integration patterns including methods showcase, column layouts,
 * lazy column implementations, and pagination scenarios.
 * 
 * Key Features:
 * - BlazeVideosInlinePlayerCompose usage
 * - Compose StateHandler patterns
 * - Modern declarative UI approach
 * - Proper disposal in Compose lifecycle
 * 
 * Important: Must inherit from AppCompatActivity (not ComponentActivity) for Blaze SDK compatibility.
 */
class InlineVideosComposeActivity : AppCompatActivity() {

    private val viewModel: InlineVideosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlazeSampleTheme {
                InlineVideosComposeApp(
                    viewModel = viewModel,
                    onFinishActivity = { finish() }
                )
            }
        }
    }

    /**
     * Handles volume key events to notify all inline video players.
     * 
     * When the user presses volume up/down keys, this propagates the volume
     * change to all active inline players in the current showcase.
     */
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP -> {
                viewModel.setOnVolumeChangedEvent()
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InlineVideosComposeApp(
    viewModel: InlineVideosViewModel,
    onFinishActivity: () -> Unit
) {
    val navController = rememberNavController()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.compose_view_title),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF181818),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { 
                            if (!navController.popBackStack()) {
                                // If no more destinations to pop, finish the activity
                                onFinishActivity()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_button),
                            contentDescription = "Back",
                            tint = Color(0xFF181818)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF181818),
                    navigationIconContentColor = Color(0xFF181818)
                )
            )
        }
    ) { paddingValues ->
        InlineVideoComposeNavigation(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}