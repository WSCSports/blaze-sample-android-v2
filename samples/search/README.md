# Search Module

This module demonstrates how to build a custom search experience on top of BlazeSDK widgets using Jetpack Compose. Unlike the SDK's built-in search screen (shown in the [Entry Point module](../entrypoint/)), this sample gives you full control over the UI while leveraging the SDK's data layer.

## Main Components

### SearchSampleActivity
- A `ComponentActivity` that hosts the Compose-based search screen.
- Uses `SearchViewModel` to manage UI state and delegates widget load callbacks.
- Applies edge-to-edge rendering for a modern full-screen look.

### SearchViewModel
- Manages the `SearchUIState` flow that drives the UI.
- Provides a `BlazeWidgetDelegate` that tracks when each widget (stories, moments, videos) finishes loading, then decides whether to show results, a "no results" message, or an error state.
- Exposes `performSearch(query)` and `clearSearch()` to control the search lifecycle.

### SearchUIState
A sealed class representing the four possible screen states:
- **Suggestions** -- Default state when the search field is empty. Displays a moments grid powered by a label-based data source.
- **Results** -- Active search results. Each content type (stories, moments, videos) gets its own section. Sections with no matching content are hidden automatically.
- **NoResults** -- Shown when all three content types return empty for the query.
- **Error** -- Shown when all three content types fail to load.

### Compose UI (`compose/` package)

| File | Purpose |
|------|---------|
| `SearchScreen.kt` | Root composable that switches between Suggestions, Results, NoResults, and Error states. Creates and manages SDK widget state handlers. |
| `SearchHeader.kt` | Search bar with back button, text input, clear button, and keyboard "Search" action. |
| `SearchResultsContent.kt` | Scrollable column of three result sections (Stories, Quick Highlights, Full Videos), each wrapping an SDK Compose widget. Sections animate in/out based on content availability. |
| `SuggestionsGridWidget.kt` | Three-column moments grid shown as suggestions before a search is performed. |
| `SearchDefaults.kt` | Centralized color constants for the search UI. |

## How It Works

### Data Flow

1. The user types a query and taps the keyboard "Search" action.
2. `SearchViewModel.performSearch(query)` emits `SearchUIState.Results`, which causes three SDK widgets to compose with `BlazeDataSourceType.Search(searchText = query)`.
3. Each widget loads its data independently. The `BlazeWidgetDelegate` in the ViewModel tracks completion of all three.
4. Once all widgets report back, the ViewModel updates the state:
   - If any widget has content, sections without content are hidden.
   - If all widgets return empty, the state transitions to `NoResults`.
   - If all widgets fail, the state transitions to `Error`.

### Widget State Handlers

The sample uses `BlazeComposeWidgetStoriesStateHandler`, `BlazeComposeWidgetMomentsStateHandler`, and `BlazeComposeWidgetVideosStateHandler` to manage widget lifecycle in Compose. These handlers are created once per Results session and reused across searches via `updateDataSource()`, avoiding unnecessary recomposition.

### Suggestions

When the search field is empty, the screen displays a `BlazeComposeMomentsWidgetGridView` configured with a label-based data source. The grid uses a custom layout with no spacing, no corner radius, and hidden overlays to create a clean tile appearance.

## Usage

1. **Launch**: The module is launched via `SearchSampleActivity`.
2. **Browse Suggestions**: Before searching, scroll through the moments grid to discover content.
3. **Search**: Type a query and tap the keyboard search button. Results appear grouped by content type.
4. **Clear**: Tap the clear button to return to the suggestions view.
