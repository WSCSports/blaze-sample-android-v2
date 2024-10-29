package com.wscsports.android.blaze.blaze_sample_android.samples.widgets

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.wscsports.android.blaze.blaze_sample_android.core.ui.viewBinding
import com.wscsports.android.blaze.blaze_sample_android.samples.widgets.databinding.ActivityWidgetsBinding
import com.wscsports.android.blaze.blaze_sample_android.samples.widgets.widget_screens.state.ChooseDataStateBottomSheetFragment
import com.wscsports.android.blaze.blaze_sample_android.samples.widgets.widget_screens.state.ChooseLayoutStyleBottomSheetFragment
import com.wscsports.android.blaze.blaze_sample_android.samples.widgets.widget_screens.WidgetsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WidgetsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityWidgetsBinding::inflate)
    private val viewModel: WidgetsViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAppbar()
        setupNavController()
        setFabClickListener()
        subscribeObservers()
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

    private fun setFabClickListener() {
        with(binding) {
            editWidgetFab.setOnClickListener {
                if (editStyleFab.isShown) {
                    editStyleFab.hide()
                    editDataFab.hide()
                } else {
                    editStyleFab.show()
                    editDataFab.show()
                }
            }
            editStyleFab.setOnClickListener {
                showLayoutStyleBottomSheet()
            }
            editDataFab.setOnClickListener {
                showDataBottomSheet()
            }
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

    private fun setupAppbar() {
        binding.appbar.setupView("Widgets") {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupNavController() {
        navController = findNavController(R.id.nav_host_fragment_activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun showEditFabViews(showFab: Boolean) {
        with(binding) {
            editStyleFab.hide()
            editDataFab.hide()
            if (showFab) {
                binding.editWidgetFab.show()
            } else {
                binding.editWidgetFab.hide()
            }
        }
    }
}