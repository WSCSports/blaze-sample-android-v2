# Follow Entities Module

This module demonstrates the Follow Entities feature end to end: a follow button in the moments player, local persistence of the followed entities, and a personalized "Your Picks" tab whose content is built from them. The same example is implemented twice — with the View system and with Jetpack Compose — so you can compare the two approaches against the same SDK integration.

## Main Components

### FollowActivity
- The entry point of the module. Shows a short list where the user picks the **Native View** or the **Compose View** implementation, then launches the matching screen.
- Backed by `FollowImplementationType` (the two options) and `FollowImplementationListAdapter`.

### BlazeFollowSynchronizer
- Owns the SDK integration and is started once from the app's `Application` class, right after `BlazeSDK.init` succeeds.
- Installs the app-wide `BlazeFollowEntitiesDelegate`, seeds the SDK with the persisted follows on launch, and records every follow click back into the repository.
- Clicks are drained by a single consumer so writes always land in click order.

### FollowRepository (`data/` package)
- The single source of truth for the followed entities, persisted locally with Room (`FollowDatabase`, `FollowedEntity`, `FollowedEntityDao`) so they survive app restarts.
- Exposes a `Flow` of followed entity ids (most recently followed first) and a `setFollowed(entityId, isFollowed)` call. The Room types stay internal — consumers only deal with plain entity ids.

### FollowViewModel
- Builds the "Your Picks" data source from the persisted follows: a `BlazeDataSourceType.Labels` that blends the followed-entity labels with the general highlights label, ranked most-recently-followed first (`labelsPriority`, `orderType = RECENTLY_UPDATED_FIRST`).
- Shared by both variants.

### FollowTabsConfiguration
- Shared builder for the three-tab moments widget configuration (**Trending**, **For you**, **Your Picks**) and the moments player style with the follow button. Keeps the tab setup identical across both variants.

### Native View variant (`views/` package)

| File | Purpose |
|------|---------|
| `FollowTabsViewActivity.kt` | Hosts the View-system screen. |
| `FollowTabsFragment.kt` | Initializes the tabs-backed moments widget and drives the follow-change refresh. |

### Compose variant (`compose/` package)

| File | Purpose |
|------|---------|
| `FollowTabsComposeActivity.kt` | Hosts the Compose screen. |
| `FollowTabsScreen.kt` | The same widget built with `BlazeComposeWidgetMomentsStateHandler`, with the refresh choreography kept in a small state holder. |

## How It Works

### Ownership split

The SDK owns the **in-session** follow state — it updates the player's follow button optimistically the moment it is tapped — while the repository owns the **persisted** state. On launch the synchronizer seeds the SDK once from the repository; after that every click is written back. There is no continuous mirroring from the database into the SDK, which would fight the SDK's own optimistic updates.

### The "Your Picks" tab

"Your Picks" is a `Labels` data source built from the followed entity ids plus the general highlights label as a fallback, so it practically never loads empty. When a follow changes, the data source is rebuilt from the new set and the tab is refreshed:
- while the widget is on screen, it is rebuilt with a fresh configuration right away;
- while the moments player is open, the non-active tabs are refreshed in the background (the tab currently being watched is never reloaded mid-playback), and the full rebuild is deferred until the user returns.

## Usage

1. **Launch**: The module is launched via `FollowActivity`.
2. **Pick an implementation**: Choose the Native View or the Compose View variant.
3. **Follow**: Open the moments player and tap the follow button — the followed entity resolves in a `Player -> Team -> Property` fallback order.
4. **See personalized content**: The "Your Picks" tab reflects the followed entities, most recently followed first, and the follows persist across app restarts.
