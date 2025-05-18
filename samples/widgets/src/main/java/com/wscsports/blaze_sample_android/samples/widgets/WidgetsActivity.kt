package com.wscsports.blaze_sample_android.samples.widgets

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import com.wscsports.blaze_sample_android.core.ui.R.*
import com.wscsports.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.blaze_sample_android.core.ui.showView
import com.wscsports.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.widgets.databinding.ActivityWidgetsBinding
import com.wscsports.blaze_sample_android.samples.widgets.edit.ChooseDataStateBottomSheetFragment
import com.wscsports.blaze_sample_android.samples.widgets.edit.ChooseLayoutStyleBottomSheetFragment
import com.wscsports.blaze_sample_android.samples.widgets.edit.EditMenuItem
import com.wscsports.blaze_sample_android.samples.widgets.edit.EditWidgetMenuBottomSheetFragment
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetLayoutStyleState
import com.wscsports.blaze_sample_android.samples.widgets.screens.WidgetsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
/**
 * WidgetsActivity is the main activity for handling widget-related operations.
 * It sets up the app bar, navigation controller, and floating action buttons (FABs).
 * It also subscribes to ViewModel observers to update the UI based on the widget state.
 */
class WidgetsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityWidgetsBinding::inflate)
    private val viewModel: WidgetsViewModel by viewModels()
    private lateinit var navController: NavController
    private var editMenuBottomSheet: EditWidgetMenuBottomSheetFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding()
        setupAppbar()
        setupNavController()
        setOnClickListeners()
        subscribeObservers()
    }

    private fun setupAppbar() {
        binding.appbar.setupView(getString(string.widgets_title)) {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupNavController() {
        navController = findNavController(R.id.nav_host_fragment_activity_main)
    }

    private fun setOnClickListeners() {
        binding.btnEditOptions.setOnClickListener {
            editMenuBottomSheet = EditWidgetMenuBottomSheetFragment.newInstance()
            editMenuBottomSheet?.show(supportFragmentManager, EditWidgetMenuBottomSheetFragment.TAG)
        }
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            launch {
                viewModel.currWidgetType.collectLatest {
                    binding.appbar.setTitles(it?.title ?: getString(string.widgets_title))
                }
            }

            launch {
                viewModel.showEditWidgetMenuButton.collectLatest { shouldShow ->
                    binding.editOptionsContainer.showView(shouldShow)
                }
            }

            launch {
                viewModel.showSelectedStyleChipsContainer.collectLatest { shouldShow ->
                    binding.chipsContainerLayout.showView(shouldShow)
                }
            }

            launch {
                viewModel.editWidgetMenuItemEvent.collectLatest { menuItem ->
                    when (menuItem) {
                        EditMenuItem.DATA_SOURCE -> displayEditDataSourceBottomSheet()
                        EditMenuItem.CUSTOMIZATION_LAYOUT_STYLE -> displayEditCustomizationBottomSheet()
                    }
                    editMenuBottomSheet?.dismiss()
                }
            }
            launch {
                viewModel.widgetStyleState.collectLatest { styleState ->
                    updateChipsWidgetStyleState(styleState)
                }
            }
        }
    }

    private fun updateChipsWidgetStyleState(styleState: WidgetLayoutStyleState) {
        binding.chipGroup.removeAllViews()
        if (styleState.isCustomAppearance) addCustomizationAppliedChip("Appearance")
        if (styleState.isCustomStatusIndicator) addCustomizationAppliedChip("Indicator")
        if (styleState.isCustomTitle) addCustomizationAppliedChip("Title")
        if (styleState.isCustomBadge) addCustomizationAppliedChip("Badge")
        if (styleState.isCustomItemStyleOverrides) addCustomizationAppliedChip("Item Override")
    }

    private fun addCustomizationAppliedChip(text: String) {
        val chip = Chip(this@WidgetsActivity).apply {
            this.text = text
            textSize = 12f
            isCheckable = false

            setChipBackgroundColorResource(color.chip_background_color)
            chipStrokeColor = resources.getColorStateList(color.chip_background_color, null)
            setTextColor(resources.getColorStateList(color.text_color_primary, null))

            chipCornerRadius = (4 * resources.displayMetrics.density).toFloat()

            val paddingInDp = 4
            val paddingInPx = (paddingInDp * resources.displayMetrics.density).toFloat()
            textStartPadding = paddingInPx
            textEndPadding = paddingInPx
            chipEndPadding = paddingInPx
            chipStartPadding = paddingInPx

            chipMinHeight = (24 * resources.displayMetrics.density)
        }
        binding.chipGroup.addView(chip)
    }

    private fun displayEditDataSourceBottomSheet() {
        val dataStateBottomSheetView = ChooseDataStateBottomSheetFragment.newInstance()
        dataStateBottomSheetView.show(supportFragmentManager, ChooseDataStateBottomSheetFragment.TAG)
    }

    private fun displayEditCustomizationBottomSheet() {
        val customizationBottomSheetView = ChooseLayoutStyleBottomSheetFragment.newInstance()
        customizationBottomSheetView.show(supportFragmentManager, ChooseLayoutStyleBottomSheetFragment.TAG)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        editMenuBottomSheet = null
    }

}