# Compose Module

This module demonstrates how to use Jetpack Compose with the Blaze SDK in the Blaze Sample Android application. It showcases the integration of Blaze widgets and player containers using modern Compose UI patterns, including navigation, theming, and state management.

## Main Components

### ComposeActivity
- The main entry point for the compose module.
- Sets up the Compose UI, navigation, and theming.
- Handles volume key events for the Moments container.

### Screens
- **WidgetsFeedScreen**: Displays Blaze Stories Row, Moments Row, and Stories Grid widgets using Compose.
- **MomentsContainerScreen**: Displays the Blaze Moments Player Container using Compose.

### ComposeViewModel
- Manages state and handlers for Blaze widgets and player containers.
- Provides state handlers for stories row, moments row, stories grid, and the moments player container.

### ComposeNavHost
- Sets up navigation between screens using Jetpack Compose Navigation.
- Defines two main screens: Widgets Feed and Moments Container.

## Usage

1. **Launch**: The module is launched via `ComposeActivity`.
2. **Navigate**: Use the bottom navigation bar to switch between the Widgets Feed and Moments Container screens.
3. **Interact**: View and interact with Blaze widgets and player containers, all rendered using Compose.
