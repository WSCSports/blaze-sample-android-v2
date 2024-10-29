package com.wscsports.android.blaze.blaze_sample_android

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.blaze_sample_android.R
import com.example.blaze_sample_android.databinding.ActivityMainBinding
import com.wscsports.android.blaze.blaze_sample_android.core.ui.viewBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private var samplesAdapter: SampleListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        samplesAdapter = SampleListAdapter { item ->
            item.activityClass?.let { cls ->
                startActivity(Intent(this, cls))
            }
            when (item) {
                SampleItem.WIDGETS -> {
                    val intent = Intent()
                    intent.setClassName(this, "com.wscsports.android.blaze.blaze_sample_android.samples.widgets.WidgetsActivity")
                    startActivity(intent)
                }
                else -> Unit
            }
        }
        binding.sampleRecyclerView.adapter = samplesAdapter
        samplesAdapter?.submitList(SampleItem.entries)
    }

}
