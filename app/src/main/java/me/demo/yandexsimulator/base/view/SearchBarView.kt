package me.demo.yandexsimulator.base.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import me.demo.yandexsimulator.R
import me.demo.yandexsimulator.databinding.ViewSearchBarBinding

class SearchBarView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null
) : MaterialCardView(context, attr) {

    private val binding by lazy(LazyThreadSafetyMode.NONE) { ViewSearchBarBinding.bind(this) }

    init {
        inflate(context, R.layout.view_search_bar, this)
    }

}