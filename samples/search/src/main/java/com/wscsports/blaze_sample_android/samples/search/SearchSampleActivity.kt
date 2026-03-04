package com.wscsports.blaze_sample_android.samples.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.wscsports.blaze_sample_android.samples.search.compose.SearchDefaults
import com.wscsports.blaze_sample_android.samples.search.compose.SearchScreen

/**
 * Sample activity demonstrating the search screen implementation.
 * Provides a full-screen search experience with suggestions and search results.
 */
class SearchSampleActivity : ComponentActivity() {

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            var textFieldValue by rememberSaveable { mutableStateOf("") }

            MaterialTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = SearchDefaults.backgroundColor
                ) { innerPadding ->
                    SearchScreen(
                        uiState = uiState,
                        searchQuery = textFieldValue,
                        onSearchQueryChange = { query ->
                            textFieldValue = query
                            if (query.isEmpty()) viewModel.clearSearch()
                        },
                        onSearch = viewModel::performSearch,
                        onClear = { textFieldValue = ""; viewModel.clearSearch() },
                        onBackPressed = { finish() },
                        widgetDelegate = viewModel.widgetDelegate,
                        contentPadding = innerPadding
                    )
                }
            }
        }
    }
}
