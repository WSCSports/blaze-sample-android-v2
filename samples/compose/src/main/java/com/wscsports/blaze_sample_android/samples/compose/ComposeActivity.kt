package com.wscsports.blaze_sample_android.samples.compose

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.wscsports.blaze_sample_android.core.ui.theme.BlazeSampleTheme

/**
 * The main activity for the Compose sample.
 * Important: To use the Moments container, you must inherit from [AppCompatActivity] rather than [ComponentActivity].
 * For more information, see https://dev.wsc-sports.com/docs/android-compose-widgets#/
 * Also, https://dev.wsc-sports.com/docs/android-player-container-compose#/
 */
class ComposeActivity : AppCompatActivity() {

    private val viewModel: ComposeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlazeSampleTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { ComposeBottomBar(navController = navController) },
                    containerColor = Color.White,
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    ComposeNavHost(
                        navController = navController,
                        viewModel = viewModel,
                        onTopBarBackPressed = { onBackPressedDispatcher.onBackPressed() },
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()))
                }
            }
        }
    }

    /**
     * Necessary only for the Moments container to handle volume changes.
     * Observing user increasing/decreasing volume, and updating the SDK.
     */
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            viewModel.onVolumeChanged()
            true
        } else {
            super.onKeyUp(keyCode, event)
        }
    }
}