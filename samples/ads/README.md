# Ads Module

This module demonstrates how to integrate and configure ads using the Blaze SDK in the Blaze Sample Android application. It showcases enabling Blaze ad handlers for IMA, GAM custom native, and GAM banner ads, and provides sample delegate implementations for handling ad events.

## Main Components

### AdsSampleActivity
- The main entry point for the ads module.
- Demonstrates how to enable Blaze SDK ads for IMA, GAM custom native, and GAM banner ads.
- Initializes Blaze widgets (Stories Row, Moments Row, Stories Grid) with ads configuration.
- Implements delegate interfaces for ad event handling.
- Provides methods to enable and disable ads for the app lifecycle.

## Blaze Ad Handler Initialization
- **Enable Ads**: In `AdsSampleActivity`, Blaze IMA, GAM custom native, and GAM banner ads are enabled using their respective `enableAds` methods. Delegates are provided for event handling.
- **Configure Widgets**: Widgets are initialized with specific ads config types (e.g., `EVERY_X_STORIES`, `EVERY_X_MOMENTS`, `FIRST_AVAILABLE_ADS_CONFIG`).
- **Disable Ads**: Ads can be disabled at any time using the corresponding `disableAds` methods.
- **Key Points**:
  - Ad handlers must be enabled before widget initialization.
  - Delegates provide hooks for logging, error handling, and advanced customization.
  - Ads remain active for the app lifecycle unless explicitly disabled.

## Usage

1. **Launch**: The module is launched via `AdsSampleActivity`.
2. **Enable Ads**: Ads are enabled on activity creation and remain active until disabled.
3. **View Widgets**: Interact with Blaze widgets configured to display ads.
4. **Handle Events**: Review delegate implementations for logging and customization.
