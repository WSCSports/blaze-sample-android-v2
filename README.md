# Blaze SDK Android Sample App

## Overview

The Blaze SDK Android Sample App provides practical examples of how to integrate WSC Sports' BlazeSDK into your Android application. It includes multiple sample modules that demonstrate different aspects of the SDK, from basic widget implementation to advanced features like universal links, push notifications, and custom styling.

## Setup Instructions

### 1. Clone the Repository

Download the project zip file or clone the repository using git:

```bash
git clone https://github.com/WSCSports/blaze-sample-android-v2.git
cd blaze-sample-android-v2
```

### 2. Configure API Key

The BlazeSDK requires an API key to function. You need to add your API key to the `local.properties` file:

1. Open or create `local.properties` in the root directory
2. Add your BlazeSDK API key:

```properties
# BlazeSDK API Key
BLAZE_API_KEY="your_api_key_here"
```

**Note**: The `local.properties` file is ignored by git for security reasons. Never commit your API keys to version control.

### 3. Sync and Build

1. Open the project in Android Studio
2. Click "Sync Project with Gradle Files"

### 4. Run the Application

1. Connect an Android device or start an emulator
2. Run the app using the play button

## BlazeSDK Integration Guide

### Initialize the SDK

The BlazeSDK needs to be initialized in your Application class or main activity. Here's example for initialization pattern:

```kotlin
// In your Application class onCreate() or Activity
BlazeSDK.init(
    // Add your own API key in local.properties file as BLAZE_API_KEY=YOUR_API_KEY
    apiKey = BuildConfig.BLAZE_API_KEY,
    cachingLevel = BlazeCachingLevel.DEFAULT,
    cachingSize = 512,
    sdkDelegate = Delegates.globalDelegate,
    playerEntryPointDelegate = Delegates.playerEntryPointDelegate,
    completionBlock = {
        Log.d(TAG, "BlazeSDK.init success completionBlock..")
    },
    errorBlock = { error ->
        Log.e(TAG, "BlazeSDK.init errorBlock -> , Init Error = $error")
    },
    externalUserId = null,
    geoLocation = null,
    forceLayoutDirection = null
)
```

### Dependencies

The BlazeSDK is declared in the `libs.versions.toml` file and included as a dependency in the app's `build.gradle.kts`:

```kotlin
dependencies {
    // BlazeSDK
    implementation(libs.blazesdk)
    
    // Other required dependencies...
}
```

## Available Samples

### 🎛️ Widgets Sample (`samples/widgets/`)

**What it demonstrates**:
- Implementation of various widget types (Stories, Moments, Videos)
- Row and Grid layout options
- Mixed widgets (multiple widgets on the same screen)
- Runtime editing capabilities:
  - Data source editing (label names, order types)
  - Layout style customization
  - Real-time UI updates

**Key Features**:
- Custom appearance settings
- Status indicators
- Badge management
- Item style overrides

### 🚀 Entry Point Sample (`samples/entrypoint/`)

**What it demonstrates**:
- Universal links handling
- Push notification integration
- Direct content playback (stories, moments, videos)
- Firebase Cloud Messaging integration

**Key Features**:
- Universal link simulation
- Play content by label expression
- Play single items by ID
- FCM service implementation

### 🎨 Compose Sample (`samples/compose/`)

**What it demonstrates**:
- Jetpack Compose integration with BlazeSDK
- Modern UI implementation patterns
- Declarative UI approach for Blaze widgets

### 📺 Ads Sample (`samples/ads/`)

**What it demonstrates**:
- Advertisement integration within Blaze content
- Ad placement and management

**Key Features**:
- Google Ad Manager integration for custom native and banner ads (GAM)
- Interactive Media Ads integration (GAM)

### 🎬 Player Style Sample (`samples/playerstyle/`)

**What it demonstrates**:
- Custom player appearance
- Style overrides and theming
- Player behavior customization

### 📱 Moments Container Sample (`samples/momentscontainer/`)

**What it demonstrates**:
- Moments container implementation
- Container-specific features and customization

### ⚙️ Global Settings Sample (`samples/globalsettings/`)

**What it demonstrates**:
- SDK-wide configuration options
- Global behavior settings
- Centralized customization

## Troubleshooting

### Common Issues

1. **API Key Not Working**:
   - Verify the API key is correctly added to `local.properties`
   - Ensure the key format matches: `BLAZE_API_KEY="your_key_here"`
   - Check that the key is valid and not expired

2. **Build Errors**:
   - Ensure Android SDK is updated to API 35
   - Sync Gradle files after any configuration changes
   - Check internet connection for dependency downloads

3. **Runtime Errors**:
   - Verify the BlazeSDK is properly initialized
   - Check logcat for specific error messages
   - Ensure minimum SDK requirements are met

### Getting Help

- Check individual sample README files for module-specific guidance
- Review the BlazeSDK documentation
- Contact WSC Sports support for API key issues

## License

This project is licensed under the terms specified in the LICENSE file.

---

**Note**: Each sample module contains its own detailed README with specific implementation details and usage instructions. Navigate to the respective sample directories for more information. 
