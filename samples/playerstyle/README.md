# PlayerStyle Module

This module demonstrates how to use and customize Blaze player styles for Stories, Moments and Videos widgets in the Blaze Sample Android application. It showcases both the default and custom player styles, providing a reference for integrating and customizing player experiences in your own app.

## Main Components

### PlayerStyleActivity
- The main entry point for the playerstyle module.
- Sets up the UI and initializes six widgets:
  - Default Stories Row Widget
  - Custom Stories Row Widget
  - Default Moments Row Widget
  - Custom Moments Row Widget
  - Default Videos Row Widget
  - Custom Videos Row Widget
- Each widget demonstrates either the default or a fully customized player style for stories, moments or videos.

### PlayerStyleViewModel
- Provides the data sources, widget layouts, and player style parameters for the activity.
- Separates default and custom player style logic for stories, moments and videos.

## Player Style Customization

The module demonstrates two approaches for each widget type:
- **Default Player Style**: Uses the Blaze SDK's base player style for stories or moments.
- **Custom Player Style**: Applies extensive customizations to the player style, including:

### Customization Options

#### For Stories Player Style
- **Background Color**: Set a custom background color for the player.
- **Title & Last Update**: Customize text color, size, and font.
- **Player Buttons**: Configure visibility, color, size, images, and order for exit, share, mute, captions, and custom action buttons.
- **Chips**: Style the 'LIVE' and 'AD' chips (text, color, background, padding).
- **CTA Button**: Customize corner radius, text size, and font.
- **Header Gradient**: Enable and set gradient colors.
- **Progress Bar**: Set background and progress colors.
- **First Time Slide**: Fully customize the onboarding overlay, including CTA, main title, subtitle, instructions, and custom instructions with icons.

#### For Moments Player Style
- **Background Color**: Set a custom background color for the player.
- **Heading & Body Text**: Customize text color, size, font, and visibility.
- **Player Buttons**: Configure exit, share, mute, like, play, captions, and custom action buttons (visibility, color, size, images, and order).
- **Chips**: Style the 'AD' chip (text, color, background, padding).
- **CTA Button**: Customize corner radius, text size, font, icon, and alignment.
- **Header & Footer Gradients**: Enable and set gradient colors and positioning.
- **Seek Bar**: Customize appearance for playing and paused states (colors, size, thumb, corner radius).
- **First Time Slide**: Fully customize the onboarding overlay, including CTA, main title, subtitle, instructions (next, previous, pause, play), and custom instructions with icons.

#### For Videos Player Style
- **Background Color**: Set a custom background color for the player.
- **Player Buttons**: Configure exit, share, mute, like, play/pause, and captions buttons (visibility, color, size, images).
- **CTA Button**: Customize corner radius, text size, size, and icon.
- **Seek Bar**: Customize appearance for playing and paused states (colors, size, thumb, corner radius).
- **Captions**: Customize text size and on-screen positioning.
- **Overlay Hide Time**: `overlayVisibilityThreshold` — how long (in milliseconds) the controls overlay stays visible before auto-hiding. The SDK default is 3000ms (`DEFAULT_OVERLAY_VISIBILITY_THRESHOLD_MS`); non-positive values fall back to that default. The custom style keeps the controls visible longer (6s) — see `applyCustomOverlayVisibility()`.

## Usage

1. **Launch**: The module is launched via `PlayerStyleActivity`.
2. **View**: The screen displays both default and custom player style widgets for stories, moments and videos, side by side.
3. **Customization**: Review the code in `CustomStoryPlayerStyleParams.kt`, `CustomMomentsPlayerStyleParams.kt` and `CustomVideosPlayerStyleParams.kt` to see how each player style property can be customized using the Blaze SDK.
