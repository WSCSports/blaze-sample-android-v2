# PlayerStyle Module

This module demonstrates how to use and customize Blaze player styles for Stories and Moments widgets in the Blaze Sample Android application. It showcases both the default and custom player styles, providing a reference for integrating and customizing player experiences in your own app.

## Main Components

### PlayerStyleActivity
- The main entry point for the playerstyle module.
- Sets up the UI and initializes four widgets:
  - Default Stories Row Widget
  - Custom Stories Row Widget
  - Default Moments Row Widget
  - Custom Moments Row Widget
- Each widget demonstrates either the default or a fully customized player style for stories or moments.

### PlayerStyleViewModel
- Provides the data sources, widget layouts, and player style parameters for the activity.
- Separates default and custom player style logic for both stories and moments.

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

## Usage

1. **Launch**: The module is launched via `PlayerStyleActivity`.
2. **View**: The screen displays both default and custom player style widgets for stories and moments, side by side.
3. **Customization**: Review the code in `CustomStoryPlayerStyleParams.kt` and `CustomMomentsPlayerStyleParams.kt` to see how each player style property can be customized using the Blaze SDK.
