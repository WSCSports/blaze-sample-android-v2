# Entrypoint Module

This module serves as the main entry point for Blaze SDK integration in the Blaze Sample Android application. It demonstrates how to handle universal links, push notifications, and direct playback of stories, moments, and videos without using widgets.

## Main Components

### EntryPointActivity
- The main activity for the entrypoint module.
- Handles universal links (via intent filters) and push notifications (via intent extras).
- Provides a UI for:
  - Simulating universal link handling.
  - Playing stories, moments, and videos by label expression.
  - Playing a single story, moment, or video by ID.
- Prepares Blaze players for stories and moments on launch.
- Demonstrates BlazeSDK methods for handling links, notifications, and direct playback.

### SampleFirebaseMessagingService
- Handles push notifications when the app is in the foreground.
- Passes Blaze-related payloads to the BlazeSDK for processing.

## Unique Implementation Details

### Firebase Cloud Messaging (FCM) Integration
- The module includes a sample implementation of `FirebaseMessagingService` (`SampleFirebaseMessagingService`) to handle push notifications.
- When a push notification is received while the app is in the foreground, the service checks if the payload is relevant for BlazeSDK and passes it for processing.
- To use FCM, ensure you add your own `google-services.json` file to the app module.

### Universal Links Scheme in Manifest
- The AndroidManifest.xml is configured with an intent filter for `EntryPointActivity` to support universal links.
- The intent filter includes the `VIEW` action, `DEFAULT` and `BROWSABLE` categories, and specifies the supported schemes (`http`, `https`) and host (e.g., `prime.mvp.fan`).
- This allows the app to be opened directly from supported links and route users to the appropriate content via BlazeSDK.

## UI Overview
- Simulate a universal link to test deep linking.
- Input fields and play buttons for stories, moments, and videos by label expression.
- Play buttons for single story, moment, or video by ID.
- All actions interact with the BlazeSDK directly.

## Usage

1. **Launch**: The module is launched via `EntryPointActivity`.
2. **Universal Links**: Open the app via a universal link or use the UI to simulate one that opens story/moment/video player directly.
3. **Push Notifications**: Send a push notification with Blaze payload; the service and activity will handle it.
4. **Direct Playback**: Use the UI to play stories, moments, or videos by label or ID.
