package com.betan.betanstoktakip.presentation.saleshistories.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.betan.betanstoktakip.core.extensions.inflate
import com.betan.betanstoktakip.core.extensions.orZero
import com.betan.betanstoktakip.core.extensions.toMoney
import com.betan.betanstoktakip.core.extensions.toStandardUiFormat
import com.betan.betanstoktakip.databinding.ItemSalesHistoriesBinding
import com.betan.betanstoktakip.domain.model.SalesHistoriesModel

class SalesHistoriesAdapter :
    ListAdapter<SalesHistoriesModel, SalesHistoriesAdapter.ItemViewHolder>(
        SalesHistoriesModelDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(
        val binding: ItemSalesHistoriesBinding
    ) : ViewHolder(binding.root) {

        fun bind(item: SalesHistoriesModel) {
            with(binding) {
                textViewDate.text = item.date?.toStandardUiFormat().orEmpty()
                textViewTotalPrice.text = item.totalPrice.orZero().toMoney()
                textViewTotalPaidPrice.text = item.paidPrice.orZero().toMoney()
                textViewDiscountCode.text = item.discountCode.orEmpty()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val binding = parent.inflate(ItemSalesHistoriesBinding::inflate)
                return ItemViewHolder(binding)
            }
        }
    }
}