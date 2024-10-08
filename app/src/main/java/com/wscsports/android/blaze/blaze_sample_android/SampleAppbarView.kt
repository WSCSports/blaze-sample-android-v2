package com.wscsports.android.blaze.blaze_sample_android

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.blaze_sample_android.databinding.SampleAppbarBinding

class SampleAppbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: SampleAppbarBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater
        binding = SampleAppbarBinding.inflate(inflater, this, true)
    }

    fun setupView(title: String, onBackPressed: () -> Unit) {
        with(binding) {
            textTitle.text = title
            buttonBack.setOnClickListener {
                onBackPressed.invoke()
            }
        }
    }
}