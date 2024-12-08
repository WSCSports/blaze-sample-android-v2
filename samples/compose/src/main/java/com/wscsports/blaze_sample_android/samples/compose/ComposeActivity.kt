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
import androidx.navigation.compose.rememberNavController
import com.wscsports.blaze_sample_android.samples.compose.ui.theme.BlazeSampleTheme

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

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            viewModel.onVolumeChanged()
            true
        } else {
            super.onKeyUp(keyCode, event)
        }
    }
}