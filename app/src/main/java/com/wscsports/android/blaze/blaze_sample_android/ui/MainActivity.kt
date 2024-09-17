package com.wscsports.android.blaze.blaze_sample_android.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blaze_sample_android.R
import com.example.blaze_sample_android.databinding.ActivityMainBinding
import com.wscsports.android.blaze.blaze_sample_android.model.SampleItem

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
            Log.d("appDebug", "MainActivity: initRecyclerView: item: $item")
        }
        with(binding.sampleRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = samplesAdapter
        }
        samplesAdapter?.submitList(SampleItem.entries)
    }

}
