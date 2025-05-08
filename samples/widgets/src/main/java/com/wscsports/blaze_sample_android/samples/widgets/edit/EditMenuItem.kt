package com.wscsports.blaze_sample_android.samples.widgets.edit


enum class EditMenuItem(
    val title: String,
    val description: String
) {
    DATA_SOURCE(
        title = "Edit data source",
        description = "Configure the data source and display order",
    ),
    CUSTOMIZATION_LAYOUT_STYLE(
        title = "Customization examples",
        description = "Demo of some available custom options",
    )
}