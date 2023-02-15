package me.demo.yandexsimulator.ui.search.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.demo.yandexsimulator.databinding.ViewDecodeAddressBinding
import me.demo.yandexsimulator.domain.model.LocationPoint


class DecodeAddressAdapter(
    private val onItemClicked: (LocationPoint) -> Unit
) : ListAdapter<LocationPoint, DecodeAddressAdapter.ViewHolder>(diffCallback) {

    var queryText: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewDecodeAddressBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.onBind(current)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
    }

    inner class ViewHolder(
        private val binding: ViewDecodeAddressBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.layoutParams = FrameLayout.LayoutParams(
                MATCH_PARENT, WRAP_CONTENT
            )
        }

        fun onBind(item: LocationPoint) = with(binding) {

            if (queryText.isNotBlank()) {
                changeSubTextColor(item.name)
            } else
                name.text = item.name

            description.text = item.formattedAddress
        }

        private fun changeSubTextColor(name: String) {
            val startIndex = name.indexOf(queryText, 0, true)
            if (startIndex != -1) {
                val str = SpannableString(name)
                str.setSpan(
                    ForegroundColorSpan(Color.BLACK),
                    startIndex,
                    queryText.length + startIndex,
                    0
                )
                binding.name.text = str
            } else {
                binding.name.text = name
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<LocationPoint>() {
            override fun areItemsTheSame(oldItem: LocationPoint, newItem: LocationPoint): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: LocationPoint,
                newItem: LocationPoint
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}