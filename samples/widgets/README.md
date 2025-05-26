# Widgets Module

This module demonstrates the implementation and customization of various widgets within the Blaze Sample Android application. It provides a showcase of different widget types, their layouts, and editing capabilities, serving as a reference for integrating and managing widgets in an Android app.

## Main Components

### WidgetsActivity
- The main entry point for the widgets module.
- Sets up navigation, app bar, and floating action buttons.
- Observes the `WidgetsViewModel` for UI updates and handles widget editing actions via bottom sheets.

### Widget Types
The module supports several widget types, each representing a different layout or data presentation:
- **Stories (Row/Grid)**
- **Moments (Row/Grid)**
- **Videos (Row/Grid)**
- **Mixed Widgets** (a feed with multiple widgets on the same screen with refresh data source loading capabilities)

## Editing Options

The widgets module provides robust runtime editing capabilities, allowing users to customize both the data source and the layout style of widgets directly from the UI. These options are accessible via the edit options button in each widget screen, which opens an edit menu with the following options:

### 1. Data Source Editing
- **Label Name**: Users can change the label (data source identifier) for the widget at runtime. This allows switching between different data feeds (e.g., `live-stories`, `top-stories`, `moments`, `videos`).
- **Order Type**: Users can select the order in which items are displayed using the `BlazeOrderType` enum (e.g., `MANUAL`, others as defined in the SDK). This is done via a dropdown spinner in the data source edit bottom sheet.
- **Workflow**: When the user applies changes, the new label and order type are immediately reflected in the widget, allowing for dynamic data reloading and presentation without restarting the activity.

### 2. Layout Style Editing
- Users can toggle various layout customization options for the widget, including:
  - **Custom Appearance**: Change the widget's preset or the visual style of widget items (e.g., image shapes, backgrounds).
  - **Status Indicator**: Show or hide custom status indicators on items.
  - **Title**: Toggle custom title display.
  - **Badge**: Enable or disable badges on items.
  - **Item Style Overrides**: Apply advanced style overrides to individual items.
- **Workflow**: These options are presented as checkboxes in the layout style edit bottom sheet. When applied, the widget UI updates in real time to reflect the selected customizations, demonstrating the flexibility of the Blaze SDK's styling system.

## Usage

1. **Launch**: The module is launched via `WidgetsActivity`.
2. **Navigation**: Users can navigate between different widget screens (Moments, Stories, Videos, Mixed).
3. **Editing**: Use the edit option button to open the edit menu and select either data source or layout style customization. Apply changes to see immediate updates in the widget.
4. **Customization**: The UI reflects all customizations live, making it easy to experiment with different data and styles.
