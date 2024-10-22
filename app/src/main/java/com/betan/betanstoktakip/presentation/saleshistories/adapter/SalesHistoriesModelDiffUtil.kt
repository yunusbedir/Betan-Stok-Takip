package com.betan.betanstoktakip.presentation.saleshistories.adapter

import androidx.recyclerview.widget.DiffUtil
import com.betan.betanstoktakip.domain.model.SalesHistoriesModel

class SalesHistoriesModelDiffUtil : DiffUtil.ItemCallback<SalesHistoriesModel>() {
    override fun areItemsTheSame(
        oldItem: SalesHistoriesModel,
        newItem: SalesHistoriesModel
    ): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(
        oldItem: SalesHistoriesModel,
        newItem: SalesHistoriesModel
    ): Boolean {
        return oldItem == newItem
    }
}