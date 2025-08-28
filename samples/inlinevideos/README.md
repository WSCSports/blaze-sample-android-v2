# Inline Videos Module

This module demonstrates the implementation of inline video functionality using the Blaze SDK. It provides comprehensive examples for integrating `BlazeVideosInlinePlayer` and `BlazeVideosInlinePlayerCompose` in Android applications.

## Module Structure

The inline videos module is organized into three main components:

### 🏠 Parent Module (`samples/inlinevideos/`)
- **InlineVideosActivity**: Main launcher activity that presents implementation options
- **InlineVideosType**: Enum defining available implementation approaches  
- **InlineVideosListAdapter**: Adapter for displaying implementation choices

### 📱 Native View Module (`samples/inlinevideos/nativeview/`)
Traditional Android Views implementation featuring:
- **Interactive Inline Videos**: Demonstration of different initialization methods and configurations
- **ScrollView Implementation**: Single active player management with proper lifecycle handling
- **RecyclerView Implementation**: Efficient view recycling with manager pattern for multiple players

Key Classes:
- `InlineVideosNativeActivity`: Main entry point for native examples
- Integration with `BlazeVideosInlinePlayer`
- Manager pattern for controlling multiple players
- Proper disposal and memory management

### 🎛️ Compose View Module (`samples/inlinevideos/composeview/`)
Modern Jetpack Compose implementation featuring:
- **Interactive Inline Videos**: Compose-based method demonstrations with StateHandler patterns
- **LazyColumn Implementation**: Efficient scrolling with Compose lifecycle integration
- **Pagination Examples**: Advanced use cases with dynamic content loading

Key Classes:
- `InlineVideosComposeActivity`: Main entry point for Compose examples
- Integration with `BlazeVideosInlinePlayerCompose`
- `BlazeVideosInlinePlayerComposeStateHandler` usage patterns
- Modern declarative UI approach

## Implementation Approaches

### Native View Approach
```kotlin
// Traditional Android Views with BlazeVideosInlinePlayer
val inlinePlayer = BlazeVideosInlinePlayer(context)
inlinePlayer.setup(uniqueId = "video_1", ...)
// Manager pattern for multiple players
// Proper lifecycle management and disposal
```

### Compose Approach  
```kotlin
// Modern Compose with StateHandler
@Composable
fun VideoScreen() {
    BlazeVideosInlinePlayerCompose(
        stateHandler = stateHandler,
        uniqueId = "video_1",
        ...
    )
}
```

## Key Features Demonstrated

### 🔧 Core Functionality
- Inline video player initialization and configuration
- Unique ID management for multiple players
- Proper lifecycle handling and disposal
- State management patterns

### 📊 Performance Optimization
- Single active player management (Native ScrollView/Compose Column)
- Efficient view recycling (Native RecyclerView/Compose LazyColumn) 
- Memory optimization techniques
- Scroll-based lifecycle handling

### 🎮 Interactive Examples
- Various player configuration options
- Method demonstrations with buttons
- Real-time configuration changes
- Pagination and dynamic loading

## Usage

1. **Launch the module**: Select "Inline Videos" from the main sample app
2. **Choose implementation**: Select either "Native View" or "Compose View"  
3. **Explore examples**: Navigate through different implementation patterns
4. **Study the code**: Examine source code for integration patterns

## Important Notes

- **Unique IDs**: Each inline player must have a unique identifier
- **Disposal**: Proper disposal is crucial for memory management
- **Manager Pattern**: Use manager for controlling multiple players
- **Lifecycle**: Integrate with Android/Compose lifecycle appropriately
- **Performance**: Consider single active player for large lists

## For Clients

This module serves as a comprehensive reference for:
- Understanding different implementation approaches
- Learning best practices for inline video integration
- Exploring performance optimization techniques
- Seeing real-world usage patterns with the Blaze SDK

Each example includes detailed comments explaining the implementation choices and their benefits.
