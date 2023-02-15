package me.demo.yandexsimulator.base.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.isVisible
import me.demo.yandexsimulator.R
import me.demo.yandexsimulator.databinding.ViewLocationAmountTextBinding

class LocationInfoTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val binding by lazy(LazyThreadSafetyMode.NONE) { ViewLocationAmountTextBinding.bind(this) }

    var statusText: String = ""
        set(value) {
            field = value
            binding.statusTv.text = value
        }

    var locationNameText: String = ""
        set(value) {
            field = value
            binding.locationNameTv.text = value
        }

    var isInLoadingState: Boolean = true
        set(value) {
            field = value
            if (value) {
                binding.progress.isVisible = true
                binding.locationNameTv.isVisible = false
            }else{
                binding.progress.isVisible = false
                binding.locationNameTv.isVisible = true
            }
        }

    init {
        orientation = VERTICAL
        inflate(context, R.layout.view_location_amount_text, this)
    }


}