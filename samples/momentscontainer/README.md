# MomentsContainer Module

This module demonstrates how to use and customize the BlazeMomentsPlayerContainer in the Blaze Sample Android application. It features a simple navigation flow between a home screen and a moments playback screen, providing a reference for integrating and customizing moments playback experiences in your own app.

## Main Components

### MomentsContainerActivity
- The main entry point for the momentscontainer module.
- Sets up the UI and manages navigation between fragments using a bottom navigation bar.
- Handles volume key events to interact with the SDK.

### HomeFragment
- Serves as the app's home screen within this module.
- Provides navigation to the MomentsFragment via a button.

### MomentsFragment
- Demonstrates the use of `BlazeMomentsPlayerContainer` to play moments.
- Instantiates and starts playback of moments when the fragment is created.
- Observes volume changes and updates the SDK accordingly.

### MomentsContainerViewModel
- Provides the player style parameters and manages UI events (such as tab selection and volume changes).
- Demonstrates how to customize the BlazeMomentsPlayerStyle for the container.

## BlazeMomentsPlayerContainer Usage

- The `MomentsFragment` contains a `BlazeMomentsPlayerContainer` instance, which is responsible for displaying and playing moments content.
- The container is started as soon as the fragment is created, and is styled using parameters from the ViewModel.

## Player Style Customization

The player style for the moments container can be customized using the Blaze SDK. Example customizations include:
- **Buttons**: Show/hide mute and exit buttons.
- **Seek Bar**: Adjust corner radius, thumb visibility, margins, and more for both playing and paused states.
- **CTA Button**: Change layout positioning and appearance.

All customizations are managed in the ViewModel and applied to the BlazeMomentsPlayerContainer instance.

## Usage

1. **Launch**: The module is launched via `MomentsContainerActivity`.
2. **Navigation**: The user starts on the HomeFragment and can navigate to the MomentsFragment to view moments playback.
3. **Customization**: Review the code in `MomentsContainerViewModel.kt` to see how player style properties can be customized using the Blaze SDK.
