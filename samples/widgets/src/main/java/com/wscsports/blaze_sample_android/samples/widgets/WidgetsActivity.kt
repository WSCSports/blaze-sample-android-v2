package com.wscsports.blaze_sample_android.samples.widgets

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.wscsports.android.blaze.blaze_sample_android.core.ui.showView
import com.wscsports.android.blaze.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.widgets.databinding.ActivityWidgetsBinding
import com.wscsports.blaze_sample_android.samples.widgets.widget_screens.WidgetsViewModel
import com.wscsports.blaze_sample_android.samples.widgets.widget_screens.state.ChooseDataStateBottomSheetFragment
import com.wscsports.blaze_sample_android.samples.widgets.widget_screens.state.ChooseLayoutStyleBottomSheetFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WidgetsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityWidgetsBinding::inflate)
    private val viewModel: WidgetsViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAppbar()
        setupNavController()
        setFabClickListener()
        subscribeObservers()
    }

    private fun setupAppbar() {
        binding.appbar.setupView("Widgets") {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupNavController() {
        navController = findNavController(R.id.nav_host_fragment_activity_main)
    }

    private fun setFabClickListener() {
        with(binding) {
            editWidgetFab.setOnClickListener {
                onEditWidgetFabClicked()
            }
            editStyleFab.setOnClickListener {
                showLayoutStyleBottomSheet()
            }
            editDataFab.setOnClickListener {
                showDataBottomSheet()
            }
        }
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            launch {
                viewModel.currWidgetType.collectLatest {
                    binding.appbar.setTitles(it?.title ?: "Widgets")
                }
            }
            viewModel.showEditWidgetFab.collectLatest {
                showEditFabViews(it)
            }
        }
    }

    private fun ActivityWidgetsBinding.onEditWidgetFabClicked() {
        editDataTextCard.showView(!editStyleFab.isShown)
        editStyleTextCard.showView(!editStyleFab.isShown)
        if (editStyleFab.isShown) {
            editStyleFab.hide()
            editDataFab.hide()
            editWidgetFab.setImageDrawable(
                ContextCompat.getDrawable(
                    this@WidgetsActivity,
                    R.drawable.ic_edit_24
                )
            )
        } else {
            editStyleFab.show()
            editDataFab.show()
            editWidgetFab.setImageDrawable(
                ContextCompat.getDrawable(
                    this@WidgetsActivity,
                    R.drawable.ic_close_24
                )
            )
        }
    }

    private fun showLayoutStyleBottomSheet() {
        val stateBottomSheetView = ChooseLayoutStyleBottomSheetFragment.newInstance()
        stateBottomSheetView.show(supportFragmentManager, ChooseLayoutStyleBottomSheetFragment.TAG)
    }

    private fun showDataBottomSheet() {
        val dataStateDialog = ChooseDataStateBottomSheetFragment.newInstance()
        dataStateDialog.show(supportFragmentManager, ChooseDataStateBottomSheetFragment.TAG)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun showEditFabViews(showFab: Boolean) {
        with(binding) {
            editStyleFab.hide()
            editDataFab.hide()
            editDataTextCard.showView(false)
            editStyleTextCard.showView(false)
            if (showFab) {
                editWidgetFab.show()
                editWidgetFab.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@WidgetsActivity,
                        R.drawable.ic_edit_24
                    )
                )
            } else {
                editWidgetFab.hide()
            }
        }
    }
}