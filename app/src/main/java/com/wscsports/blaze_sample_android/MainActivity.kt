package com.wscsports.blaze_sample_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blaze.blazesdk.shared.BlazeSDK
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.wscsports.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private var samplesAdapter: SampleListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding()
        initRecyclerView()
        binding.sampleButton.setOnClickListener{
            BlazeSDK.playMoment(
                "nbl",
                BlazeMomentsPlayerStyle.base()
            )
        }
    }

    private fun initRecyclerView() {
        samplesAdapter = SampleListAdapter { item ->
            item.classPathSuffix?.let { className ->
                val intent = Intent()
                intent.setClassName(this, "$ROOT_SAMPLE_PKG_NAME${className}")
                startActivity(intent)
            }
        }
        binding.sampleRecyclerView.adapter = samplesAdapter
        samplesAdapter?.submitList(SampleItem.entries)
    }

    companion object {
        const val ROOT_SAMPLE_PKG_NAME = "com.wscsports.blaze_sample_android.samples."
    }

}
