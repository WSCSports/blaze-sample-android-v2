package com.wscsports.blaze_sample_android.core.ui

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.wscsports.blaze_sample_android.core.ui.databinding.SampleAppbarBinding

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

    fun setTitles(title: String) {
        binding.textTitle.text = title
    }
}